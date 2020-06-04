package kharybdys.roborally.game.movement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.util.Coordinates;

/**
 *  Class encapsulating movement of anything. This is either one step or a turn or a combination of both
 */
public class Movement implements Comparable<Movement>
{

	private static final Logger logger = LoggerFactory.getLogger( Movement.class );

    /*
     * movementDirection needs to be not null when numberSquares isn't 0 and we want to process it.
     */
    private MovementType type;
    private Direction movementDirection;
    /*
     * positive is clockwise, negative is counterclockwise.
     */
    private int facingTurnSteps;
    /*
     * Should never be greater than one, otherwise it needs to be represented by multiple movement objects.
     * Also assumed to be always positive.
     * TODO: Refactor to a boolean, we either move or not
     */
    private int numberSquares;
    
    private int priority;
    
    private AbstractMovingElement movingElement = null;

    public enum MovementType 
    {

        ROBOT_MOVEMENT,
        DUAL_SPEED_CONVEYOR,
        SINGLE_SPEED_CONVEYOR,
        ROTATOR,
        PUSHER,
        NONE
    }

    public Movement( Direction movementDirection, int turnSteps, MovementType type, int numberSquares, int priority ) 
    {
        this.type = type;
        this.movementDirection = movementDirection;
        this.facingTurnSteps = turnSteps;
        this.numberSquares = numberSquares;
        this.priority = priority;
    }

    public Movement( Direction movementDirection, int turnSteps, int numberSquares, int priority ) 
    {
        this( movementDirection, turnSteps, MovementType.ROBOT_MOVEMENT, numberSquares, priority );
    }

    public Direction getNewFacingDirection(Direction oldFacingDirection)
    {
        return oldFacingDirection.processRotate(facingTurnSteps);
    }

    /**
     * Changes this movement to no longer change position.
     * Eg when we are bonking into a wall, or in a bot that cannot move itself.
     * 
     * @return the changed movement object
     */
    public Movement stopMovement()
    {
    	logger.debug("Stopped the movement");
        this.numberSquares = 0;
        return this;
    }
    
    public Direction getMovingDirection()
    {
        return movementDirection;
    }
    
    public MovementType getType()
    {
        return type;
    }

    private int getPriority()
    {
        return priority;
    }

    /**
     * Whether this movement represents an actual change in position
     * 
     * @return Whether this movement represents an actual change in position
     */
    public boolean changesPosition()
    {
    	return numberSquares > 0;
    }
    
    public void updateXAndYCoords(AbstractMovingElement ame)
    {
    	// TODO
    }

    public Coordinates getResultingCoordinates(Coordinates coords)
    {
        int xChange = 0;
        int yChange = 0;
        if (movementDirection != null)
        {
            switch(movementDirection)
            {
                case WEST: xChange = -1 * numberSquares; break;
                case EAST: xChange = numberSquares; break;
                case NORTH: yChange = numberSquares; break;
                case SOUTH: yChange = -1 * numberSquares; break;
            }
        }
        return new Coordinates(xChange + coords.getxCoord(), yChange + coords.getyCoord());
    }

    /**
     * The movingElement to which this movement applies
     * TODO: Probably should be a required field so no separate setter, but in the constructor
     * 
     * @return The movingElement
     */
	public AbstractMovingElement getMovingElement() {
		return movingElement;
	}

	/**
	 * Sets the movingElement to which this movement applies
	 * 
	 * @param movingElement The movingElement to which this movement applies
	 */
	public void setMovingElement(AbstractMovingElement movingElement) {
		this.movingElement = movingElement;
	}

	/**
	 * Compares two movements based on type of movement first, then priority second
	 */
	@Override
	public int compareTo( Movement m ) 
	{
		int typeComparison = type.compareTo( m.getType() ); 

		if(  typeComparison == 0 )
		{
			return Integer.valueOf( priority ).compareTo( Integer.valueOf( m.getPriority() ) );
		}
		else
		{
			return typeComparison;
		}
	}
}
