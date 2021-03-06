package kharybdys.roborally.game.board;

public enum BasicElementType 
{
    BASIC, 
    STARTING_1( 1 ), 
    STARTING_2( 2 ), 
    STARTING_3( 3 ), 
    STARTING_4( 4 ), 
    STARTING_5( 5 ), 
    STARTING_6( 6 ), 
    STARTING_7( 7 ), 
    STARTING_8( 8 ), 
    REPAIR, 
    OPTION, 
    HOLE;

	private int number = 0;
	
	private BasicElementType()
	{
		// Do nothing
	}
	private BasicElementType( int number )
	{
		this.number = number;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	/**
	 * Returns whether this type represents a starting position.
	 * 
	 * @return Whether this type represents a starting position
	 */
	public boolean isStartingPosition() 
	{
		// If more types will use numbers, rewrite to actually do a check on type.
		return number > 0;
	}
}