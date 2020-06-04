package kharybdys.roborally.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.document.types.Binary;
import com.dexels.navajo.script.api.Access;
import com.dexels.navajo.script.api.Mappable;
import com.dexels.navajo.script.api.MappableException;
import com.dexels.navajo.script.api.UserException;

import kharybdys.roborally.game.persistence.EndOfRoundDatabaseAccess;
import kharybdys.roborally.game.persistence.GameFactory;
import kharybdys.roborally.game.persistence.PhaseCalculationDatabaseAccess;
import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Game;

public class RoboRallyGameAdapter implements Mappable {

	public int factor = Game.DEFAULT_FACTOR;
	public Integer gameId;
	public Integer transactionContext;

	private Access access;
	private Game game;

	// Value not really used
	public boolean performFirstSetup;
	public boolean calculateNextRound;
	public Integer performOptionRecompile;
	public Integer powerDown;

	private static final Logger logger = LoggerFactory.getLogger(RoboRallyGameAdapter.class);

	@Override
	public void load( Access access ) throws MappableException, UserException 
	{
		this.access = access;
	}

	@Override
	public void store() throws MappableException, UserException 
	{
	}

	@Override
	public void kill() 
	{
	}

	public boolean isPerformFirstSetup() 
	{
		return performFirstSetup;
	}

	public void setPerformFirstSetup( boolean performFirstSetup ) throws UserException 
	{
		this.performFirstSetup = performFirstSetup;
		if( performFirstSetup )
		{
			if( game.getCurrentRound() != 0 )
			{
				logger.warn( "Trying to first setup a game ( id = {} ) with currentRound unequal to 0, ignoring", gameId );
			}
			else
			{
		        EndOfRoundDatabaseAccess endOfRound = new EndOfRoundDatabaseAccess();
		        endOfRound.initialize( access, transactionContext, game );
				// Shuffle and deal movement cards
				endOfRound.initializeCardsForNextRound();
				// set us on round 1
				game.nextRound();
				// save the game
		        endOfRound.saveGame();
		        
		        endOfRound.close();
			}
		}
	}

	public boolean isCalculateNextRound() 
	{
		return calculateNextRound;
	}

	public void setCalculateNextRound( boolean calculateNextRound ) throws UserException 
	{
		this.calculateNextRound = calculateNextRound;
		if( calculateNextRound )
		{
			if( game.getCurrentRound() == 0 )
			{
				logger.warn( "Trying to calculate next round for a game ( id = {} ) with currentRound equal to 0, ignoring", gameId );
			}
			else
			{
		        logger.info( "Calculating next round for game {}", game );
		        
		        for ( int phase = 1; phase <= Game.MAX_PHASE; phase++ )
		        {
		            processOnePhase( phase );
		            logger.info( "Processed phase " + phase + " for game " + game );
		        }
		        
		        EndOfRoundDatabaseAccess endOfRound = new EndOfRoundDatabaseAccess();
		        endOfRound.initialize( access, transactionContext, game );

		        // repair bots where applicable & divvy out option cards to bots on those board elements 
		        
		        // revive bots where applicable
		        
		        // save the game
		        endOfRound.saveGame();
		        
		        // determine whether someone has won, if so set the game to FINISHED and stop there.
		        
		        // copy movement cards to next round where applicable & handle locked registers
		         
				// Shuffle and deal movement cards for the new round
			}
		}
	}
	
	private void processOnePhase(int phase) throws UserException 
	{
		PhaseCalculationDatabaseAccess phaseCalculation = new PhaseCalculationDatabaseAccess();
		phaseCalculation.initialize( access, transactionContext, game );
		// process bot movement
		// process board movement
		// process lasers shot, board then robots
		// process archivemarkers and flags touched
		
	}

	public Integer getPowerDown() 
	{
		return powerDown;
	}

	public void setPowerDown( Integer powerDown ) throws UserException 
	{
		this.powerDown = powerDown;
		if( powerDown != null )
		{
			// Power down means to sit out this turn doing nothing, in exchange setting damage to 0
			Bot bot = game.getBot( powerDown );
			// TODO
		}
	}

	public Integer getPerformOptionRecompile() 
	{
		return performOptionRecompile;
	}

	public void setPerformOptionRecompile( Integer performOptionRecompile ) throws UserException 
	{
		this.performOptionRecompile = performOptionRecompile;
		if( performOptionRecompile != null )
		{
			// Recompile means to get a new hand of cards & to get one damage
			Bot bot = game.getBot( performOptionRecompile );
			// TODO
		}
	}

	public Integer getGameId() 
	{
		return gameId;
	}

	public void setGameId( Integer gameId ) throws UserException 
	{
		this.gameId = gameId;
		// and initialize the game object belonging to this id if not null
		if( gameId != null )
		{
			GameFactory gameFactory = GameFactory.getInstance();
			gameFactory.initialize( access, transactionContext, null );
			game = GameFactory.getInstance().getGame( gameId );
			gameFactory.close();
		}
	}

	public Integer getFactor() 
	{
		return factor;
	}

	public void setFactor( Integer factor ) 
	{
		this.factor = factor;
	}

	public Integer getTransactionContext() 
	{
		return transactionContext;
	}

	public void setTransactionContext( Integer transactionContext ) 
	{
		this.transactionContext = transactionContext;
	}

	public Binary getBoardImage() 
	{
		return new Binary( game.getImage( factor ) );
	}
}
