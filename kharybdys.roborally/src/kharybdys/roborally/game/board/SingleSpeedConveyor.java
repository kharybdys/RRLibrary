package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

import kharybdys.roborally.game.definition.Movement;

/**
 * This class represents a single-speed conveyor belt
 */
public class SingleSpeedConveyor extends AbstractConveyor 
{

    /**
     * Basic constructor for a single-speed conveyor belt
     * Just defines color
     */
    
    public SingleSpeedConveyor()
    {
       color = new Color(127, 127, 0);
    }

    /**
     * Get the basic movement that this board element enacts on the bot in the given phase.
     * For the single-speed conveyor belt this is equal to the current boardelement's movement
     * 
     * @param phase The phase we're interested in
     * 
     * @return a (possibly empty) collection of Movement objects representing the boardMovement 
     *         as applied to all movingElements currently on this boardElement
     */
    @Override
    public Collection<Movement> getBoardMovements( int phase ) 
    {
    	return getBasicBoardMovements( false );
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
    public void drawInnerField( Graphics g, int baseX, int baseY, int width, int height, int factor )
    {
        g.setColor( BACKGROUND_COLOR );
        switch ( endDirection ) 
        {
            case NORTH:
                g.fillRect( baseX, baseY - 4 * factor, width, 2 * factor );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX, baseY - 2 * factor, 
                		                                    baseX + width, baseY - 2 * factor, 
                		                                    baseX + width / 2, baseY - 4 * factor
                		                                  ) );
                break;
            case SOUTH:
                g.fillRect( baseX, baseY + height + 2 * factor, width, 2 * factor );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX, baseY + height + 2 * factor, 
                		                                    baseX + width, baseY + height + 2 * factor, 
                		                                    baseX + width / 2, baseY + height + 4 * factor
                		                                   ) );
                break;
            case EAST:
                g.fillRect( baseX + width + 2 * factor, baseY, 2 * factor, height );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX + width + 2 * factor, baseY, 
                		                                    baseX + width + 2 * factor, baseY + height, 
                		                                    baseX + width + 4 * factor, baseY + height / 2
                		                                   ) );
                break;
            case WEST:
                g.fillRect( baseX - 4 * factor, baseY, 2 * factor, height );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX - 2 * factor, baseY, 
                		                                    baseX - 2 * factor, baseY + height, 
                		                                    baseX - 4 * factor, baseY + height / 2
                		                                   ) );
                break;
        }
    }
}
