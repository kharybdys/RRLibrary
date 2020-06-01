package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Collection;

import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementType;

/**
 * Represents a generic implementation of a rotator board element. Only thing missing is the direction in which to turn (clockwise or counter-clockwise)
 */
public abstract class AbstractRotator extends AbstractBoardElement 
{

    int turnSteps = 0;
    Color color;
    
    /**
     * Get the basic movement that this board element enacts on the bot in the given phase.
     * 
     * @param phase The phase we're interested in
     * 
     * @return a (possibly empty) collection of Movement objects representing the boardMovement 
     *         as applied to all movingElements currently on this boardElement
     */
    @Override
    public Collection<Movement> getBoardMovements( int phase ) 
    {
        return Collections.singletonList( new Movement(null, turnSteps, RoboRallyMovementType.ROTATOR, 0, 0) );
    }

    /**
     * Paints the special features of a rotator
     * 
     * @param g
     * @param baseX
     * @param baseY
     * @param factor
     */
    @Override
    public void paintElement( Graphics g, int baseX, int baseY, int factor ) 
    {
        int sign = -1 * Integer.signum( turnSteps );
        g.setColor( Color.white );
        g.fillOval( baseX + 2 * factor, baseY + 2 * factor, size - 4 * factor, size - 4 * factor ); 
        g.setColor( color );
        g.fillArc( baseX + 3 * factor, baseY + 3 * factor, size - 6 * factor, size - 6 * factor, 90 + sign * 10, 80 * sign );
        g.setColor( Color.white );
        g.fillOval( baseX + 5 * factor, baseY + 5 * factor, size - 10 * factor, size - 10 * factor );
        g.setColor( Color.lightGray );
        g.fillOval( baseX + 6 * factor, baseY + 6 * factor, size - 12 * factor, size - 12 * factor );
        g.setColor( color);
        int corrSize = sign < 0 ? size : 0;
        g.fillPolygon( BoardElement.createTriangle( baseX + corrSize + sign * 2 * factor, 
                                                    baseY + size / 2, 
                                                    baseX + corrSize + sign * 6 * factor, 
                                                    baseY + size / 2, 
                                                    baseX + corrSize + sign * 4 * factor, 
                                                    baseY + size / 2 + 2 * factor 
                                                  ) );
    }
}
