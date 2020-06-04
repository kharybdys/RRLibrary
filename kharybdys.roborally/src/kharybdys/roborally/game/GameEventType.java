package kharybdys.roborally.game;

/**
 * Models the types of game events that can happen and have to be logged.
 *
 */
public enum GameEventType 
{
	BOT_PUSHES(             true,  true,  false ),
	CONVEYORBELT_STALL(     true,  true,  false ),
	BOT_SHOOTS(             true,  true,  false ),
	BOARD_SHOOTS(           false, true,  false ),
	BOT_DIES_DAMAGE(        false, true,  false ),
	BOT_DIES_HOLE(          false, true,  false ),
	ARCHIVEMARKER_MOVED(    true,  false, true  ),
	POWER_DOWN(             true,  false, false ),
	BOT_HITS_WALL(          true,  false, false ),
	BOT_HITS_UNMOVABLE_BOT( true,  true,  false );
	
	boolean actorRequired = false;
	boolean victimRequired = false;
	boolean optionalTextRequired = false;
	
	private GameEventType( boolean actorRequired, boolean victimRequired, boolean optionalTextRequired )
	{
		this.actorRequired = actorRequired;
		this.victimRequired = victimRequired;
		this.optionalTextRequired = optionalTextRequired;
	}
	
	public boolean isActorRequired()
	{
		return actorRequired;
	}
	
	public boolean isVictimRequired()
	{
		return victimRequired;
	}

	public boolean isOptionalTextRequired()
	{
		return optionalTextRequired;
	}
}