package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.Movement;

/**
 * Represents a dualspeed conveyor belt
 */
public class DualSpeedConveyor extends AbstractConveyor 
{
	
    /**
     * Basic constructor for a dual-speed conveyor belt
     * Just defines the color
     */
    public DualSpeedConveyor() 
    {
        color = Color.blue;
    }

    /**
     * Get the basic movement that this board element enacts on the bot in the given phase.
     * For the dual-speed conveyor belt, if we move onto another conveyor belt we need that one's movement too
     * 
     * @param phase The phase we're interested in
     * 
     * @return a (possibly empty) collection of Movement objects representing the boardMovement 
     *         as applied to all movingElements currently on this boardElement
     */
    @Override
    public Collection<Movement> getBoardMovements( int phase ) 
    {
    	Collection<Movement> boardMovements = new ArrayList<Movement>();

    	boardMovements.addAll( getBasicBoardMovements( true ) );
    	
    	BoardElement nextElement = getNeighbour( endDirection );
    	if( nextElement instanceof AbstractConveyor )
    	{
    		boardMovements.addAll( ((AbstractConveyor) nextElement).getBasicBoardMovements( false ) );
    	}
    	return boardMovements;
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
        switch ( endDirection ) {
            case NORTH:
                g.fillRect( baseX, baseY-6*factor, width, 4*factor );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX, baseY - 4 * factor, 
                		                                    baseX + width, baseY - 4 * factor, 
                		                                    baseX + width / 2, baseY - 6 * factor 
                		                                  ) );
                g.fillPolygon( BoardElement.createTriangle( baseX, baseY - 2 * factor, 
                		                                    baseX + width, baseY - 2 * factor, 
                		                                    baseX + width / 2, baseY - 4 * factor 
                		                                  ) );
                break;
            case SOUTH:
                g.fillRect( baseX, baseY + height + 2 * factor, width, 4 * factor );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX, baseY + height + 2 * factor, 
                		                                    baseX + width, baseY + height + 2 * factor, 
                		                                    baseX + width / 2, baseY + height + 4 * factor
                		                                  ) );
                g.fillPolygon( BoardElement.createTriangle( baseX, baseY + height + 4 * factor, 
                		                                    baseX + width, 
                		                                    baseY + height + 4 * factor, 
                		                                    baseX + width / 2, 
                		                                    baseY + height + 6 * factor
                		                                  ) );
                break;
            case EAST:
                g.fillRect( baseX + width + 2 * factor, baseY, 2 * factor, height );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX + width + 2 * factor, baseY, 
                		                                    baseX + width + 2 * factor, baseY + height, 
                		                                    baseX + width + 4 * factor, baseY + height / 2
                		                                  ) );
                g.fillPolygon( BoardElement.createTriangle( baseX + width + 4 * factor, baseY, 
                		                                    baseX + width + 4 * factor, baseY + height, 
                		                                    baseX + width + 6 * factor, baseY + height / 2
                		                                   ) );
                break;
            case WEST:
                g.fillRect( baseX - 4 * factor, baseY, 2 * factor, height );
                g.setColor( color );
                g.fillPolygon( BoardElement.createTriangle( baseX - 4 * factor, baseY, 
                		                                    baseX - 4 * factor, baseY + height, 
                		                                    baseX - 6 * factor, baseY + height / 2
                		                                  ) );
                g.fillPolygon( BoardElement.createTriangle( baseX - 2 * factor, baseY, 
                		                                    baseX - 2 * factor, baseY + height, 
                		                                    baseX - 4 * factor, baseY + height / 2
                		                                  ) );
                break;
        }
    }
}
