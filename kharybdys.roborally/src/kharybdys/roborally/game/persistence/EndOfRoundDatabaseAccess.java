package kharybdys.roborally.game.persistence;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.script.api.UserException;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.movement.MovementCardDefinition;
import kharybdys.roborally.game.options.OptionCardDefinition;

/**
 * Models the actions from and towards the database at the end of a round.
 * This includes firstSetup as that is basically the same as the end of round 0
 *
 */
public class EndOfRoundDatabaseAccess extends AbstractDatabaseAccess 
{
	private static final Logger logger = LoggerFactory.getLogger( EndOfRoundDatabaseAccess.class );

	/*
	 * Database access directly, grouped on triggered by
	 * 
	 * * Part of endOfRound calculation
	 * Write movement cards to the Database at start of next round
	 * Write movement cards to the Database at end of current round (locked cards etc)
	 * Update coordinates etc of bots  *!*
	 * Update coordinates etc of flags *!*
	 * 
	 * * Part of first setup
	 * Write movement cards to the Database at start of next round
	 * Update coordinates etc of bots *!*
	 * Insert flags                   *!*
	 */

	public void initializeCardsForNextRound() throws UserException
	{
		checkInitialization();
		
    	// Figure out the cards moving over from this round (for example because they were LOCKED last round or they're on a FLYWHEEL or such)
		// and register them in the Database
    	EnumSet<MovementCardDefinition> usedCards = moveCardsFromThisRoundToNext();
    	
    	// inverse the Set and change this to a list( so we have an order )
    	List<MovementCardDefinition> freeCards = new ArrayList<MovementCardDefinition>();
    	freeCards.addAll( EnumSet.complementOf( usedCards ) );
    	
    	// Shuffle the list
    	for( int i = 0; i < freeCards.size(); i++ )
    	{
    		int swapWith =  ThreadLocalRandom.current().nextInt( i + 1, freeCards.size() - 1 );
    		MovementCardDefinition swap = freeCards.get( i );
    		freeCards.set( i, freeCards.get( swapWith ) );
    		freeCards.set( swapWith, swap );
    	}
    	
        // now divvy out the cards
    	final String insertNewCardQuery = " INSERT INTO MovementCard ( round, bot, cardDefinition ) VALUES ( ?, ?, ? ) ";
    	int cardIndex = 0;
        for ( Bot bot : game.getBots() )
        {
            logger.debug( "Initializing cards for bot " + bot );
            
            int nrOfCardsToGet = Bot.INITIAL_HEALTH - bot.getDamage() - 1;

            if( bot.hasOptionCard( OptionCardDefinition.EXTRA_MEMORY ) )
            {
            	nrOfCardsToGet++;
            }
            
            for ( int i = 1; i <= nrOfCardsToGet; i++ )
            {
            	// add Card with index cardIndex to the bot, directly in the DB
            	sql.setUpdate( insertNewCardQuery );
            	sql.setParameter( game.getCurrentRound() + 1 );
            	sql.setParameter( bot.getId() );
            	sql.setParameter( freeCards.get( cardIndex ) );
            	cardIndex++;
            }
        }
	}
	
	
	private EnumSet<MovementCardDefinition> moveCardsFromThisRoundToNext() throws UserException
	{
		final String insertFlywheelCardForBotQuery = " INSERT INTO MovementCard ( round, bot, cardDefinition ) "
			                         	           + " SELECT round + 1 AS round, bot, cardDefinition          "
			                         	           + " FROM   MovementCard mc                                  "
			                         	           + " WHERE  mc.bot            = ?                            "
			                         	           + " AND    mc.round          = ?                            "
			                         	           + " AND    mc.usesOptionCard = 'FLYWHEEL'                   ";
	
		final String insertLockedCardsForBotQuery = " INSERT INTO MovementCard ( round, phase, bot, statusId, cardDefinition )    "
									  	          + " SELECT round + 1 AS round, phase, bot, 'LOCKED' AS statusId, cardDefinition "
									  	          + " FROM   MovementCard mc                                                      "
									  	          + " WHERE  mc.bot    = ?                                                        "
									  	          + " AND    mc.round  = ?                                                        "
									  	          + " AND    mc.phase >= ?                                                        ";


		for( Bot bot : game.getBots() )
		{
			sql.setUpdate( insertFlywheelCardForBotQuery );
			sql.setParameter( bot.getId() );
			sql.setParameter( game.getCurrentRound() );
			
			sql.setDoUpdate( true );
			
			// Efficiency, check if we even have to lock any registers
			if( Bot.INITIAL_HEALTH - bot.getDamage() <= Game.MAX_PHASE )
			{
				sql.setUpdate( insertLockedCardsForBotQuery );
				sql.setParameter( bot.getId() );
				sql.setParameter( game.getCurrentRound() );
				sql.setParameter( Bot.INITIAL_HEALTH - bot.getDamage() );
				
				sql.setDoUpdate( true );
			}
		}

		EnumSet<MovementCardDefinition> usedCards = EnumSet.noneOf( MovementCardDefinition.class );
		
		final String getUsedCardsQuery = " SELECT cardDefinition FROM MovementCard mc WHERE mc.round = ? AND EXISTS (SELECT 1 FROM Bot b WHERE b.id = mc.bot AND b.game = ? ) ";
		
		sql.setQuery( getUsedCardsQuery );
		sql.setParameter( game.getCurrentRound() + 1 );
		sql.setParameter( game.getId() );
		
		for( int i = 0; i < sql.getRowCount(); i++ )
        {
            sql.setResultSetIndex( i );
            String cardDefinition = (String) sql.getColumnValue( "cardDefinition" );
            
            MovementCardDefinition card = MovementCardDefinition.valueOf( cardDefinition );
            usedCards.add( card );
        }		
		
		return usedCards;
	}
	
	/**
	 * Marks this game as FINISHED, ie a bot has reached the final flag
	 * 
	 * @throws UserException
	 */
	public void finalizeGame() throws UserException
	{
		final String finalizeGameQuery = "UPDATE Game SET statusId = 'FINISHED', updateby = ? WHERE id = ?";
		sql.setUpdate( finalizeGameQuery );
		sql.setParameter( AbstractDatabaseAccess.internalUserName );
		sql.setParameter( game.getId() );
		
		sql.setDoUpdate( true );
	}

	/**
	 * Responsible for saving the state of the given game including bots and flags
	 * TODO: Should I validate on things? Maybe on all bots and flags having coordinates, phase being 1
	 * 
	 * @throws UserException 
	 */
	public void saveGame() throws UserException
	{
		checkInitialization();
		if( game.getId() == null || game.getId() == -1 )
		{
			throw new UnsupportedOperationException( "Cannot save a game without an identifier" );
		}
		
		for( AbstractMovingElement flag : game.getFlags() )
		{
			updateFlag( flag );
		}

		for( Bot bot : game.getBots() )
		{
			updateBot( bot );
		}
		
		updateGame( game.getId(), game.getCurrentRound() );
	}
	
	/**
	 * Helper method to update the fields of the game itself
	 * Does not use the class field game
	 * 
	 * @param id           The game id
	 * @param currentRound The game's current round
	 * 
	 * @throws UserException If something went wrong in the DB
	 */
	private void updateGame( int id, int currentRound ) throws UserException
	{
		final String updateGameQuery = "UPDATE Game SET round = ?, statusId = If( statusId = 'FINISHED', 'FINISHED', 'AVAILABLE' ), updatedBy = ? WHERE id = ?";
		sql.setUpdate( updateGameQuery );
		sql.setParameter( currentRound );
		sql.setParameter( AbstractDatabaseAccess.internalUserName );
		sql.setParameter( id );
		
		sql.setDoUpdate( true );
	}
	
	/**
	 * Helper method to update the fields of the given bot
	 * 
	 * @param bot The bot to update the record for
	 * 
	 * @throws UserException If something went wrong in the DB
	 */
	private void updateBot( Bot bot ) throws UserException
	{
		final String updateBotQuery = "UPDATE Bot SET damage = ?, lives = ?, xCoord = ?, yCoord = ?, archiveXCoord = ?, archiveYCoord = ?, latestFlag = ?, orderNumber = IFNULL( orderNumber, ? ), facingDirection = ?, updatedBy = ? WHERE id = ?";
		sql.setUpdate( updateBotQuery );
		sql.setParameter( bot.getDamage() );
		sql.setParameter( bot.getLives() );
		sql.setParameter( bot.getLocation().getXCoordinate() );
		sql.setParameter( bot.getLocation().getYCoordinate() );
		sql.setParameter( bot.getArchiveMarker().getXCoordinate() );
		sql.setParameter( bot.getArchiveMarker().getYCoordinate() );
		sql.setParameter( bot.getLatestFlag() );
		sql.setParameter( bot.getOrderNumber() );
		sql.setParameter( bot.getFacingDirection() );
		sql.setParameter( internalUserName );
		sql.setParameter( bot.getId() );
	}
	
	/**
	 * Helper method to update the fields of the given flag
	 * 
	 * @param flag The flag to update the record for
	 * 
	 * @throws UserException If something went wrong in the DB
	 */
	private void updateFlag( AbstractMovingElement flag ) throws UserException
	{
		final String updateFlagQuery = "UPDATE Flag SET xCoord = ?, yCoord = ?, archiveXCoord = ?, archiveYCoord = ?, updatedBy = ? WHERE id = ?";
		sql.setUpdate( updateFlagQuery );
		sql.setParameter( flag.getLocation().getXCoordinate() );
		sql.setParameter( flag.getLocation().getYCoordinate() );
		sql.setParameter( flag.getArchiveMarker().getXCoordinate() );
		sql.setParameter( flag.getArchiveMarker().getYCoordinate() );
		sql.setParameter( internalUserName );
		sql.setParameter( flag.getId() );
		
		sql.setDoUpdate( true );
	}
}
