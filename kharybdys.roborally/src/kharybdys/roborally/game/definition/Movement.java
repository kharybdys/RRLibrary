package kharybdys.roborally.game.definition;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.util.Coordinates;

/**
 *
 * @author MHK
 */
public class Movement {

	private static final Logger logger = LoggerFactory.getLogger( Movement.class );

    /*
     * movementDirection needs to be not null when numberSquares isn't 0 and we want to process it.
     */
    private RoboRallyMovementPriority priority;
    private Direction movementDirection;
    /*
     * positive is clockwise, negative is counterclockwise.
     */
    private int facingTurnSteps;
    /*
     * Should never be greater than one, otherwise it needs to be represented by multiple movement objects.
     * Also assumed to be always positive.
     */
    private int numberSquares;
    private int numPriority;

    public enum RoboRallyMovementPriority {

        ROBOT_MOVEMENT,
        DUAL_SPEED_CONVEYOR,
        SINGLE_SPEED_CONVEYOR,
        ROTATOR,
        PUSHER,
        NONE
    }

    public Movement(Direction movementDirection, int facingSteps, RoboRallyMovementPriority priority, int numberSquares, int numPriority) {
        this.priority = priority;
        this.movementDirection = movementDirection;
        this.facingTurnSteps = facingSteps;
        this.numberSquares = numberSquares;
        this.numPriority = numPriority;
    }

    public Direction getNewFacingDirection(Direction oldFacingDirection)
    {
        return oldFacingDirection.processRotate(facingTurnSteps);
    }

    public Movement adjustMovement()
    { // outside has determined that we are bonking into a wall.
    	logger.debug("Adjusted the movement");
        this.numberSquares=0;
        return this;
    }
    public Direction getMovingDirection()
    {
        return movementDirection;
    }
    public RoboRallyMovementPriority getPriority()
    {
        return priority;
    }

    public void updateXAndYCoords(AbstractMovingElement ame)
    {
        Coordinates resultingCoords = getResultingCoordinates(ame.getCoords());
        ame.setCoords(resultingCoords);
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
}
