package kharybdys.roborally.game.board;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Collection;
import java.util.Map;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;

/**
 * Contains available business methods that are shared amongst the elements
 */
public interface BoardElement 
{

	/**
	 * Unique element representing anything outside of the board.
	 */
    public BoardElement outsideElement = new BasicElement(-1, -1, null, 0, BasicElementType.HOLE);

    /**
     * Turn a copy of this board element turned the number of steps in the clockwise direction
     * Assumes turning is done before laser logic is applied
     * 
     * @param turnSteps The number of steps to turn
     * @param newX      The new x coordinate
     * @param newY      The new y coordinate
     * @return          The turned BoardElement
     */
    public BoardElement turn(int turnSteps, int newX, int newY);

    /**
     * Adds a laser to this boardElement and its neighbours until this laser hits a wall
     * Used recursively
     * 
     * @param strength    The strength of the laser
     * @param originating The originating direction
     * @param preceding   The preceding boardElement (if any)
     */
    public void addLaser( int strength, Direction originating, BoardElement preceding );

    /**
     * Setup method, adds a boardElement in the given direction as neighbour
     * 
     * @param direction The direction to add the given boardElement to
     * @param neighbour The boardElement to add
     */
    public void addNeighbour( Direction direction, BoardElement neighbour );

    /**
     * Gets the neighbour in the given direction. Always returns a boardElement
     * 
     * @param direction The direction for which to return the neighbour
     * 
     * @return The neighbour at that direction
     */
    public BoardElement getNeighbour( Direction direction );

    /**
     * Logic that draws this boardElement 
     * 
     * @param g              The graphics object to use
     * @param boardXOffset   The x offset of the board we are a part of
     * @param boardYOffset   The y offset of the board we are a part of
     * @param ySizePanel     The height of the panel we are drawing on
     * @param factor         The magnification factor to use
     */
    public void paint(Graphics g, int boardXOffset, int boardYOffset, int ySizePanel, int factor);

    /**
     * Mimics firing the laser(s) on this boardelement, and finds the bot(s) getting hit by the laser(s).
     * Note, returns null if no laser on this boardelement
     * 
     * @return The bot(s) getting hit by laser(s) starting on this element, with the strength hit with
     */
    public Map<Bot, Integer> findLaserTargets();
    
    /**
     * Follows the laser coming from the given direction, and finds the bot getting hit by this laser.
     * 
     * @return The bot getting hit by the given laser (through its originating direction)
     */
    public Bot findLaserTarget( Direction originating );

    /**
     * Get the basic movement that this board element enacts on the bot in the given phase.
     * 
     * @param phase The phase we're interested in
     * 
     * @return a (possibly empty) collection of Movement objects representing the boardMovement 
     *         as applied to all movingElements currently on this boardElement
     */
    public Collection<Movement> getBoardMovements(int phase);

    /**
     * Adjusts the given movement to comply with the rules (bumping into walls)
     * 
     * @param movement The movement to adjust
     * 
     * @return The new movement item representing the changed movement
     */
    public Movement adjustMovementForWalls(Movement movement);

    /**
     * Returns the type of the boardElement (mostly for checking if it's a hole).
     * TODO: Check if this is only for checking whether it's a hole, in that case change it to "killsBot" or such
     * 
     * @return The type of this boardElement
     */
    public BasicElementType getBoardElementType();

    /**
     * Helper method to check whether a wall exists in that direction.
     * 
     * @param side The direction where to check for a wall
     * 
     * @return Whether a wall exists there
     */
    public boolean hasWall( Direction side );

    /**
     * Helper method to create a Polygon representing a triangle with three given cornerpoints
     * 
     * @param x1 X-coordinate of the first corner
     * @param y1 Y-coordinate of the first corner
     * @param x2 X-coordinate of the second corner
     * @param y2 Y-coordinate of the second corner
     * @param x3 X-coordinate of the third corner
     * @param y3 Y-coordinate of the third corner
     * 
     * @return A Polygon object representing a triangle
     */
	static Polygon createTriangle( int x1, int y1, int x2, int y2, int x3, int y3 ) 
	{
	    int[] xpoints = new int[3];
	    xpoints[0] = x1;
	    xpoints[1] = x2;
	    xpoints[2] = x3;
	    int[] ypoints = new int[3];
	    ypoints[0] = y1;
	    ypoints[1] = y2;
	    ypoints[2] = y3;
	    return new Polygon( xpoints, ypoints, 3 );
	}
}
