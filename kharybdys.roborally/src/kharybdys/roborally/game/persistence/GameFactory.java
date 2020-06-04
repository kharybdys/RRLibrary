package kharybdys.roborally.game.persistence;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.script.api.UserException;

import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.board.GameBuilder;
import kharybdys.roborally.game.board.ImplementedScenario;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.MovementCardDefinition;

/**
 * Factory with helper methods that creates a Game object
 * Implements Singleton as it is also the holder of a cache of Game objects
 */
public class GameFactory extends AbstractDatabaseAccess {

    private static final Logger logger = LoggerFactory.getLogger( GameFactory.class );
    
    private final Map<Integer, Game> gameCache = new HashMap<Integer, Game>();

    private static GameFactory instance = null;

    /**
     * Private constructor given the Singleton pattern
     */
    private GameFactory()
    {
    	
    }
    
    /**
     * Access method to the singleton instance of this class
     * 
     * @return The instance of GameFactory we will use
     */
    public static GameFactory getInstance()
    {
    	if( instance == null )
    	{
    		instance = new GameFactory();
    	}
    	return instance;
    }
    
    /**
     * Should go to the database and instantiate the game object given the state of the database, unless we have it in cache

     * @param gameId the id of the game to instantiate
     * 
     * @return the Game object
     * 
     * @throws UserException If something went wrong 
     */
	public Game getGame( Integer gameId ) throws UserException 
	{
		if( !gameCache.containsKey( gameId ) )
		{
			instantiateGame( gameId );
		}

		return gameCache.get( gameId );
	}

	private void instantiateGame( Integer gameId ) throws UserException 
	{
		checkInitialization();
		
		final String getGameQuery = "SELECT scenarioName, currentRound FROM Game WHERE id = ?";
		sql.setQuery( getGameQuery );
		sql.setParameter( gameId );
		String scenarioName = (String) sql.getColumnValue( "scenarioName" );
		Integer currentRound = (Integer) sql.getColumnValue( "currentRound" );
		
		ImplementedScenario scenario = ImplementedScenario.valueOf( scenarioName );
		GameBuilder builder = scenario.getGameBuilder( currentRound != 0 );
		
		// Get the bots in random order in case they don't have an orderNumber. If they actually happen to have an orderNumber, it doesn't matter
		int currentOrderNumber = 1;
		final String getBotsQuery = "SELECT id, damage, lives, xCoord, yCoord, archiveXCoord, archiveYCoord, latestFlag, orderNumber, facingDirection FROM Bot WHERE game = ? ORDER BY rand()";
		sql.setQuery( getBotsQuery );
		sql.setParameter( gameId );
		
		for( int i = 0; i < sql.getRowCount(); i++ )
        {
            sql.setResultSetIndex( i );
            Integer botId         = (Integer) sql.getColumnValue( "id" );
            Integer damage        = (Integer) sql.getColumnValue( "damage" );
            Integer lives         = (Integer) sql.getColumnValue( "lives" );
            Integer xCoord        = (Integer) sql.getColumnValue( "xCoord" );
            Integer yCoord        = (Integer) sql.getColumnValue( "yCoord" );
            Integer archiveXCoord = (Integer) sql.getColumnValue( "archiveXCoord" );
            Integer archiveYCoord = (Integer) sql.getColumnValue( "archiveYCoord" );
            Integer latestFlag    = (Integer) sql.getColumnValue( "latestFlag" );
            Integer orderNumber   = (Integer) sql.getColumnValue( "orderNumber" );
            String facingDirValue = (String)  sql.getColumnValue( "facingDirection" );
            
            Direction facingDirection = Direction.valueOf( facingDirValue );
            // we assume if one doesn't have an orderNumber none of them will have one
            orderNumber = orderNumber == null ? currentOrderNumber : orderNumber;
            currentOrderNumber++;
            
            builder.addBot( botId, damage, lives, xCoord, yCoord, archiveXCoord, archiveYCoord, latestFlag, orderNumber, facingDirection );
        }
		
		final String getFlagsQuery = "SELECT id, xCoord, yCoord, archiveXCoord, archiveYCoord, orderNumber FROM Flag WHERE game = ? ORDER BY orderNumber ASC";
		sql.setQuery( getFlagsQuery );
		sql.setParameter( gameId );

		for( int i = 0; i < sql.getRowCount(); i++ )
        {
            sql.setResultSetIndex( i );
            Integer flagId        = (Integer) sql.getColumnValue( "id" );
            Integer xCoord        = (Integer) sql.getColumnValue( "xCoord" );
            Integer yCoord        = (Integer) sql.getColumnValue( "yCoord" );
            Integer archiveXCoord = (Integer) sql.getColumnValue( "archiveXCoord" );
            Integer archiveYCoord = (Integer) sql.getColumnValue( "archiveYCoord" );
            Integer orderNumber   = (Integer) sql.getColumnValue( "orderNumber" );
            
            builder.addFlag( flagId, xCoord, yCoord, archiveXCoord, archiveYCoord, orderNumber );
        }
		
	}
}
