package kharybdys.roborally.game.definition;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Enum with added business logic encapsulating the cardinal directions
 */
public enum Direction {

    NORTH, EAST, SOUTH, WEST;

    /**
     * positive is clockwise, negative is counterclockwise
     * 
     * Returns the new direction when rotated the amount of turns
     * Inverse of {@link #getTurns(Direction)}
     * 
     * @param turnSteps The number of steps to turn
     * 
     * @return The new direction
     */
    public Direction processRotate( int turnSteps ) 
    {
    	// Efficiency, return ourselves if turnSteps = 0
    	if( turnSteps == 0 )
    	{
    		return this;
    	}

    	int newDirectionOrd = this.ordinal() + turnSteps;
        while (newDirectionOrd < 0 || newDirectionOrd > 3) 
        {
            if (newDirectionOrd > 3) 
            {
                newDirectionOrd -= 4;
            }
            if (newDirectionOrd < 0) 
            {
                newDirectionOrd += 4;
            }
        }
        return Direction.values()[newDirectionOrd];
    }

    /**
     * positive is clockwise, negative is counterclockwise

     * The amount of turns to be made when going from this direction
     * to the given newDirection. 
     * Inverse of {@link #getTurns(Direction)}
     * 
     * @param newDirection The new direction wanted
     * 
     * @return The amount of turns required
     */
    public int getTurns( Direction newDirection ) 
    {
        int turn = this.ordinal() - newDirection.ordinal();
        while (turn < 0 || turn > 3) 
        {
            if (turn < 0) 
            {
                turn += 4;
            }
            if (turn > 3) 
            {
                turn -= 4;
            }
        }
        return turn;
    }
    
    public static Collection<Direction> of( Direction d1 )
    {
    	return EnumSet.of( d1 );
    }

    public static Collection<Direction> of( Direction d1, Direction d2 )
    {
    	return EnumSet.of( d1, d2 );
    }

    public static Collection<Direction> of( Direction d1, Direction d2, Direction d3 )
    {
    	return EnumSet.of( d1, d2, d3 );
    }

    public static Collection<Direction> of( Direction d1, Direction d2, Direction d3, Direction d4 )
    {
    	return EnumSet.of( d1, d2, d3, d4 );
    }
    
    public static Collection<Direction> turnCollection( Collection<Direction> directions, int turnSteps )
    {
    	// Efficiency, return the given collection if turnSteps = 0
    	if( turnSteps == 0 )
    	{
    		return directions;
    	}
    	
    	EnumSet<Direction> result = EnumSet.noneOf( Direction.class );
    	for( Direction dir : directions )
    	{
    		result.add( dir.processRotate( turnSteps ) );
    	}
    	return result;
    }
}
