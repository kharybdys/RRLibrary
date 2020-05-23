package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementType;

/**
 * Class representing an AbstractPusher. Only part missing is the phases this pusher applies to
 */
public abstract class AbstractPusher extends AbstractBoardElement {

    protected Direction pusherDirection;
    protected String pusherText;
    protected Collection<Integer> pusherPhases;

    /**
     * Basic constructor for any pusher
     * Adds pusherDirection onto the basic boardelement
     * 
     * @param xCoord          The xCoordinate of this boardElement
     * @param yCoord          The yCoordinate of this boardElement
     * @param walls           The collection of directions that have walls on this boardElement
     * @param pusherDirection The direction this pusher pushes towards
     */
    public AbstractPusher( int xCoord, int yCoord, Collection<Direction> walls, Direction pusherDirection ) 
    {
        super( xCoord, yCoord, walls );
        this.pusherDirection = pusherDirection;
    }

    /**
     * Get the basic movement that this board element enacts on the bot in the given phase.
     * 
     * @param phase The phase we're interested in
     * 
     * @return a (possibly empty) collection of Movement objects representing the boardMovement 
     *         as applied to all movingElements currently on this boardElement
     */
    public Collection<Movement> getBoardMovements( int phase ) 
    {
        if ( pusherPhases.contains( phase ) ) 
        {
            return Collections.singletonList( new Movement( pusherDirection, 0, RoboRallyMovementType.PUSHER, 1, 0 ) );
        } 
        else 
        {
            return Collections.emptyList();
        }
    }

    /**
     * Helper method to execute extra actions for turning this boardElement 
     * the given number of steps in the clockwise direction
     * 
     * @param turnSteps The number of steps to turn
     */
    @Override
    protected void performTurn( int turnSteps ) 
    {
        pusherDirection = pusherDirection == null ? null : pusherDirection.processRotate( turnSteps );
    }

    /**
     * Paints the special features of a pusher
     * 
     * @param g
     * @param baseX
     * @param baseY
     * @param factor
     */
    public void paintElement( Graphics g, int baseX, int baseY, int factor ) 
    {
        Graphics2D g2d = (Graphics2D) g;
        int fontHeight = (size - (8 * factor)) / 2;
        int width = size - 2;
        int height = fontHeight + factor;
        int startX = baseX + 1;
        int startY = baseY + 1;
        int fontX = startX + factor;
        int fontY = startY + height - factor;
        switch (pusherDirection) {
        // note that the pusher needs to be drawn at the opposite side of the direction
            case NORTH:
                g2d.rotate(Math.PI, baseX + size / 2, baseY + size / 2);
                break;
            case SOUTH:
                // no rotation needed
                break;
            case EAST:
                g2d.rotate(Math.PI * 1.5, baseX + size / 2, baseY + size / 2);
                break;
            case WEST:
                g2d.rotate(Math.PI * 0.5, baseX + size / 2, baseY + size / 2);
                break;
        }
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontHeight));
        FontMetrics fm = g.getFontMetrics();
        int xCorr = (width - fm.stringWidth(pusherText)) / 2;
        g.setColor(Color.darkGray);
        g.fillRect(startX, startY, width, height);
        g.setColor(Color.yellow);
        g.drawString(pusherText, fontX + xCorr, fontY);
        switch (pusherDirection) {
        // note that the pusher needs to be drawn at the opposite side of the direction
            case NORTH:
                g2d.rotate(-Math.PI, baseX + size / 2, baseY + size / 2);
                break;
            case SOUTH:
                // no rotation needed
                break;
            case EAST:
                g2d.rotate(-Math.PI * 1.5, baseX + size / 2, baseY + size / 2);
                break;
            case WEST:
                g2d.rotate(-Math.PI * 0.5, baseX + size / 2, baseY + size / 2);
                break;
        }
    }
}
