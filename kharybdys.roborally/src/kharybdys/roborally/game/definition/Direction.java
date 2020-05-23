package kharybdys.roborally.game.definition;

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
     * @param steps The number of steps to turn
     * 
     * @return The new direction
     */
    public Direction processRotate( int steps ) 
    {
        int newDirectionOrd = this.ordinal() + steps;
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
}
