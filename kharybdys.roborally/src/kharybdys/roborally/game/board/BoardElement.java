package kharybdys.roborally.game.board;

import java.awt.Graphics;
import java.util.List;

import kharybdys.roborally.game.board.AbstractBoardElement.BoardElementType;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;

/**
 * Contains available business methods that are shared amongst the elements
 * @author MHK
 */
public interface BoardElement extends Comparable<BoardElement> {

    public BoardElement outsideElement = new BasicElement(-1, -1, null, null, (List<Direction>) null, 0, AbstractBoardElement.BoardElementType.HOLE);

	/*
     * Provides sorting information for the implementation of Comparable
     */
    public Movement.RoboRallyMovementPriority getMovementPriority();

    /*
     * Return this board element turned the number of steps in the clockwise direction.
     */
    public BoardElement turn(int turnSteps, int newX, int newY);

    /*
     * Paints this element.
     */
    public void paint(Graphics g, int boardXOffset, int boardYOffset, int ySizePanel, int factor);

    /*
     * Get whether there is a laser mount on this element (if so, you are shot).
     */
    public boolean existsLaserMount(Direction directionLaserFire);

    /*
     * Get the direction(s) from where the laser fire comes (for checking whether another bot actually got this shot earlier).
     */
    public List<Direction> getDirectionLaserFire();

    /*
     * Gets the amount of damage taken from laser(s) firing from the given direction
     */
    public Integer getLaserDamage(Direction directionLaserFire);

    /*
     * Get the basic movement that this board element enacts on the bot.
     */
    public Movement getBoardMovement(int phase);

    /*
     * Adjusts the given movement to comply with the rules (bumping into walls)
     */
    public Movement adjustMovementForWalls(Movement movement);

    /*
     * Provides a correcting movement (being turned by the conveyor you arrived on).
     */
    public Movement correctingMovementAfter(Direction entry);

    /*
     * Returns the type of the boardElement (mostly for checking if it's a hole).
     */
    public AbstractBoardElement.BoardElementType getBoardElementType();

    /*
     * Returns whether a wall exists in that direction. Mostly necessary for laserfire checks.
     */
    public boolean hasWall(Direction side);
}
