package kharybdys.roborally.game.persistence;

import com.dexels.navajo.adapter.SQLMap;
import com.dexels.navajo.script.api.Access;
import com.dexels.navajo.script.api.MappableException;
import com.dexels.navajo.script.api.UserException;

import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.GameEventType;
import kharybdys.roborally.game.board.AbstractMovingElement;

import org.slf4j.helpers.MessageFormatter;

/**
 * Models access to the Database when needed straight from the Java code.
 * Base class, see subclasses for practical usage
 */
public abstract class AbstractDatabaseAccess 
{
	protected SQLMap sql = null;
	protected Game game = null;
	protected static final String internalUserName = "internal";
	
	public void logGameEvent( GameEventType type, AbstractMovingElement actor, AbstractMovingElement victim, String optionalText ) throws UserException
	{
		// validations
		checkInitialization();
		if( type.isActorRequired() && actor == null )
		{
			throw new UserException( MessageFormatter.format( "Logging event of type {} without an actor is not allowed!", type ).getMessage() ); 
		}

		if( type.isVictimRequired() && victim == null )
		{
			throw new UserException( MessageFormatter.format( "Logging event of type {} without a victim is not allowed!", type ).getMessage() ); 
		}

		if( type.isOptionalTextRequired() && optionalText == null )
		{
			throw new UserException( MessageFormatter.format( "Logging event of type {} without an optionalText is not allowed!", type ).getMessage() ); 
		}
		
		// let's save
		final String insertEventQuery = "INSERT INTO Event ( game, round, phase, type, actor, victim, optionalText ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";
		sql.setUpdate( insertEventQuery );
		sql.setParameter( game.getId() );
		sql.setParameter( game.getCurrentRound() );
		sql.setParameter( game.getCurrentPhase() );
		sql.setParameter( type.name() );
		sql.setParameter( actor == null ? null : actor.getId() );
		sql.setParameter( actor == null ? null : actor.getId() );
		sql.setParameter( optionalText );
		
		sql.setDoUpdate( true );
	}
	
	public void initialize( Access a, int transactionContext, Game game ) throws UserException
	{
		if( sql != null )
		{
			throw new UserException( "Trying to initialize while already initialized" );
		}
		
		try 
		{
			sql = new SQLMap();
			sql.load( a );
			sql.setTransactionContext( transactionContext );
		} 
		catch (MappableException e) 
		{
			throw new UserException( "Something went wrong initializing the SQLMap", e );
		}
	}
	
	public void close() throws UserException
	{
		if( sql != null )
		{
			try 
			{
				sql.store();
				sql = null;
				game = null;
			} 
			catch (MappableException e) 
			{
				throw new UserException( "Something went wrong storing the SQLMap", e );
			}
		}
	}
	
	protected void checkInitialization() throws UserException
	{
		if( sql == null )
		{
			throw new UserException( MessageFormatter.format( "Non-initalized {} being used for database access", this.getClass().getSimpleName() ).getMessage() ); 
		}
	}
}
