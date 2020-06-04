package kharybdys.roborally.game.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import org.slf4j.helpers.MessageFormatter;

import com.dexels.navajo.script.api.UserException;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.movement.Movement;
import kharybdys.roborally.game.movement.MovementCardDefinition;
import kharybdys.roborally.game.movement.MovementCardTransformer;
import kharybdys.roborally.game.options.OptionCardDefinition;

/**
 * Models the actions from and towards the database during the calculation of any given phase.
 *
 */
public class PhaseCalculationDatabaseAccess extends AbstractDatabaseAccess 
{
	public Collection<Movement> getMovements( Bot bot, int round, int phase ) throws UserException
	{
		checkInitialization();
		if( bot == null )
		{
			throw new UserException( "Cannot get movement cards for null bot" ); 
		}
		if( ! bot.getGame().equals( game ) )
		{
			throw new UserException( MessageFormatter.format( "Cannot get movement cards for bot {} as it is not involved in game {}", bot, game ).getMessage() ); 
		}
		if( round > game.getCurrentRound() )
		{
			throw new UserException( MessageFormatter.format( "Cannot get movement cards for round {} as the game {} has not progressed that far yet", round, game ).getMessage() ); 
		}
		
		final String getMovementCardsQuery = "SELECT cardDefinition, usesOptionCard FROM MovementCard WHERE bot = ? AND round = ? AND phase = ?";
		sql.setQuery( getMovementCardsQuery );
		sql.setParameter( bot.getId() );
		sql.setParameter( round );
		sql.setParameter( phase );
		
		Collection<MovementCardDefinition> cards = EnumSet.noneOf( MovementCardDefinition.class );
		Collection<Movement> movements = new ArrayList<Movement>();
		
		String usesOptionCard = null;
		
		for( int i = 0; i < sql.getRowCount(); i++ )
        {
            sql.setResultSetIndex( i );
            String cardDefinition = (String) sql.getColumnValue( "cardDefinition" );
            cards.add( MovementCardDefinition.valueOf( cardDefinition ));
            usesOptionCard = (String) sql.getColumnValue( "usesOptionCard" );
        }		
		
		MovementCardTransformer movementCardTransformer = null;
		if( usesOptionCard != null )
		{
			OptionCardDefinition optionCard = OptionCardDefinition.valueOf( usesOptionCard );
			movementCardTransformer = optionCard.getMovementCardTransformer();
		}
		else
		{
			movementCardTransformer = MovementCardTransformer.getDefaultMovementCardTransformer();
		}
		
		return movementCardTransformer.getMovements( cards, bot.getFacingDirection() );
	}

	/**
	 * Responsible for saving a snapshot of the current state of the board in the form of a png image to the database
	 * 
	 * @throws UserException 
	 */
	public void saveSnapshot() throws UserException
	{
		checkInitialization();
		
		final String saveBinaryQuery = "INSERT INTO Binary ( data ) VALUES ( ? )";
		final String getBinaryIdQuery = "SELECT LAST_INSERT_ID()";
		final String saveSnapshotQuery = "INSERT INTO History ( game, round, phase, snapshot ) VALUES ( ?, ?, ?, ? )";

		sql.setUpdate( saveBinaryQuery );
		sql.setParameter( game.getImage( Game.DEFAULT_FACTOR ) );
		sql.setDoUpdate( true );
		
		sql.setQuery( getBinaryIdQuery );
		Object binaryId = sql.getColumnValue( 0 );
		
		sql.setUpdate( saveSnapshotQuery );
		sql.setParameter( game.getId() );
		sql.setParameter( game.getCurrentRound() );
		sql.setParameter( game.getCurrentPhase() );
		sql.setParameter( binaryId );
		sql.setDoUpdate( true );
	}
}
