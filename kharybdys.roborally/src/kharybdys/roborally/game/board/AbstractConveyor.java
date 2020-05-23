package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementType;

/**
 * Abstract implementation of a conveyor belt. Only thing that's missing is its speed (single or dual)
 * @author MHK
 */
public abstract class AbstractConveyor extends AbstractBoardElement 
{

    protected Collection<Direction> startingDirections;
    protected Direction endDirection;
    protected Color color;
    protected final static Color BACKGROUND_COLOR = Color.black;

    /**
     * Basic constructor for a conveyor belt
     * Adds startingDirections and endDirection to the AbstractBoardElement implementation.
     * 
     * @param xCoord             The xCoordinate of this boardElement
     * @param yCoord             The yCoordinate of this boardElement
     * @param walls              The collection of directions that have walls on this boardElement
     * @param startingDirections The collection of directions from which this conveyor belt comes TODO: decide whether to derive instead of specify
     * @param endDirection       The direction this conveyor belt exits to
     */
    public AbstractConveyor( int xCoord, int yCoord, Collection<Direction> walls, Collection<Direction> startingDirections, Direction endDirection ) 
    {
        super( xCoord, yCoord, walls );
        this.startingDirections = startingDirections;
        this.endDirection = endDirection;
    }

    /**
     * Note to the subclasses that this one needs implementation. Supplied helper method is {@link #getBasicBoardMovements(boolean)}.
     */
    @Override
    public abstract Collection<Movement> getBoardMovements( int phase ); 
    
    /**
     * Implements the movement this exact boardElement causes, without taking into account 
     * that dualspeed conveyor belts may have us end up on another conveyor belt that will still move
     * 
     *  Implements:
     *  1) Basic movement of the conveyor
     *  2) Extra turn action because of the turn in the conveyor we just took
     *  
     * @param firstAction Whether this is the firstAction of a dual-speed conveyor belt (and thus has to have higher priority)
     * 
     * @return The movements belonging to this boardElement on its own
     */
    protected Collection<Movement> getBasicBoardMovements( boolean firstAction )
    {
    	int priorityFactor = firstAction ? 3 : 1;
    	Collection<Movement> boardMovements = new ArrayList<Movement>();
    	// 1) Basic movement of the conveyor
    	boardMovements.add( new Movement( endDirection, 0, RoboRallyMovementType.SINGLE_SPEED_CONVEYOR, 1, 200 * priorityFactor ) );
    	// 2) Extra turn action ( optional ), only when nextElement is the same type as we are
    	BoardElement nextElement = getNeighbour( endDirection );
    	if( this.getClass().equals( nextElement.getClass() ) && nextElement instanceof AbstractConveyor )
    	{
    		// If that conveyor belt does not exit in the same direction, we are turning
    		Direction nextEndDirection = ((AbstractConveyor) nextElement).endDirection; 
    	    if( ! nextEndDirection.equals( endDirection ) )
    	    {	// TODO: Verify the correct direction of turns is gotten. Might also be wrong on the enum implementation
    	    	boardMovements.add( new Movement( null, endDirection.getTurns( nextEndDirection ), RoboRallyMovementType.SINGLE_SPEED_CONVEYOR, 0, 100 * priorityFactor ) );
    	    }
    	}
    	return boardMovements;
    }

    /**
     * Helper method to execute extra actions for turning this boardElement 
     * the given number of steps in the clockwise direction
     * 
     * @param turnSteps The number of steps to turn
     */
    protected void performTurn(int turnSteps) 
    {
        this.endDirection = this.endDirection == null ? null : this.endDirection.processRotate( turnSteps );
        Collection<Direction> dirs = new ArrayList<Direction>();
        for ( Direction dir : startingDirections )
        {
            dirs.add( dir.processRotate( turnSteps ) );
        }
        startingDirections = dirs;
	}

    /**
     * Draws the specific features of a conveyor belt, for as much as they're shared between the two versions
     * 
     * @param g
     * @param baseX
     * @param baseY
     * @param factor
     */
    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int factor) {

        int width = size - 8 * factor;
        int height = size - 8 * factor;
        g.setColor(color);
        g.fillRect(baseX, baseY, size, size);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(baseX + 4 * factor, baseY + 4 * factor, width, height);
        if (startingDirections.contains(Direction.SOUTH) || endDirection.equals(Direction.SOUTH)) {
            g.fillRect(baseX + 4 * factor, baseY + size - 4 * factor, width, 4 * factor);
        }
        if (startingDirections.contains(Direction.NORTH) || endDirection.equals(Direction.NORTH)) {
            g.fillRect(baseX + 4 * factor, baseY, width, 4 * factor);
        }
        if (startingDirections.contains(Direction.EAST) || endDirection.equals(Direction.EAST)) {
            g.fillRect(baseX + size - 4 * factor, baseY + 4 * factor, 4 * factor, height);
        }
        if (startingDirections.contains(Direction.WEST) || endDirection.equals(Direction.WEST)) {
            g.fillRect(baseX, baseY + 4 * factor, 4 * factor, height);
        }
        int innerBaseX = baseX + 4 * factor + 2 * factor;
        int innerBaseY = baseY + 4 * factor + 2 * factor;
        int innerWidth = width - 4 * factor;
        int innerHeight = height - 4 * factor;
        for (Direction startingDirection : startingDirections) {
            g.setColor(color);
            List<Direction> directions = new ArrayList<Direction>();
            directions.add(startingDirection);
            directions.add(endDirection);
            if (directions.contains(Direction.NORTH)) {
                g.fillRect(innerBaseX + 2 * factor, innerBaseY - 2 * factor, innerWidth - 4 * factor, 2 * factor);
            }
            if (directions.contains(Direction.SOUTH)) {
                g.fillRect(innerBaseX + 2 * factor, innerBaseY + innerHeight, innerWidth - 4 * factor, 2 * factor);
            }
            if (directions.contains(Direction.EAST)) {
                g.fillRect(innerBaseX + innerWidth, innerBaseY + 2 * factor, 2 * factor, innerHeight - 4 * factor);
            }
            if (directions.contains(Direction.WEST)) {
                g.fillRect(innerBaseX - 2 * factor, innerBaseY + 2 * factor, 2 * factor, innerHeight - 4 * factor);
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.NORTH)) 
            { // draw a rectangle. Do not use the outermost 4xfactor pixels
                g.fillRect(innerBaseX + 2 * factor, innerBaseY, innerWidth - 4 * factor, innerHeight);
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.EAST)) 
            {  // fill an arc that covers the entire rectangle we were given
                g.fillArc(innerBaseX + 2 * factor, innerBaseY + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 90, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX + innerWidth - 2 * factor, innerBaseY + innerHeight - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 90, 90);
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.WEST)) 
            {
                g.fillArc(innerBaseX - innerWidth + 2 * factor, innerBaseY + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 0, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX - 2 * factor, innerBaseY + innerHeight - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 0, 90);
            }
            if (directions.contains(Direction.WEST) && directions.contains(Direction.NORTH)) 
            {
                g.fillArc(innerBaseX - innerWidth + 2 * factor, innerBaseY - innerHeight + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 270, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX - 2 * factor, innerBaseY - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 270, 90);
            }
            if (directions.contains(Direction.EAST) && directions.contains(Direction.NORTH)) 
            {
                g.fillArc(innerBaseX + 2 * factor, innerBaseY - innerHeight + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 180, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX + innerWidth - 2 * factor, innerBaseY - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 180, 90);
            }
            if (directions.contains(Direction.EAST) && directions.contains(Direction.WEST)) 
            { // draw a rectangle. Do not use the outermost 4xfactor pixels.
                g.fillRect(innerBaseX, innerBaseY + 2 * factor, innerWidth, innerHeight - 4 * factor);
            }
        }
        drawInnerField(g, innerBaseX, innerBaseY, innerWidth, innerHeight, factor);
    }

    /**
     * Responsible for drawing the arrows of the conveyor belt
     * 
     * @param g      The graphics object to use
     * @param baseX  The baseX of the remaining area available to draw on
     * @param baseY  The baseY of the remaining area available to draw on
     * @param width  The width of the remaining area available to draw on
     * @param height The height of the remaining area available to draw on
     * @param factor The multiplication factor with which to draw
     */
    public abstract void drawInnerField(Graphics g, int baseX, int baseY, int width, int height, int factor);

}
