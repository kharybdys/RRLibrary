package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementPriority;
import kharybdys.util.Utils;

/**
 *
 * @author MHK
 */
public class DualSpeedConveyor extends AbstractConveyor {

    public DualSpeedConveyor(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot, List<Direction> startingDirections, Direction endDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot, startingDirections, endDirection);
        priority = RoboRallyMovementPriority.DUAL_SPEED_CONVEYOR;
        color = Color.blue;
    }

    public DualSpeedConveyor(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot, List<Direction> startingDirections, Direction endDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot, startingDirections, endDirection);
        priority = RoboRallyMovementPriority.DUAL_SPEED_CONVEYOR;
        color = Color.blue;
    }

    public void drawInnerField(Graphics g, int baseX, int baseY, int width, int height, int factor) {
        g.setColor(Color.red);
        switch (endDirection) {
            case NORTH:
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(baseX, baseY-6*factor, width, 4*factor);
                g.setColor(color);
                g.fillPolygon(Utils.createTriangle(baseX, baseY - 4 * factor, baseX + width, baseY - 4 * factor, baseX + width / 2, baseY - 6 * factor));
                g.fillPolygon(Utils.createTriangle(baseX, baseY-2*factor, baseX+width, baseY-2*factor, baseX+width/2, baseY-4*factor));
                break;
            case SOUTH:
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(baseX, baseY+height+2*factor, width, 4*factor);
                g.setColor(color);
                g.fillPolygon(Utils.createTriangle(baseX, baseY+height+2*factor, baseX+width, baseY+height+2*factor, baseX+width/2, baseY+height+4*factor));
                g.fillPolygon(Utils.createTriangle(baseX, baseY+height+4*factor, baseX+width, baseY+height+4*factor, baseX+width/2, baseY+height+6*factor));
                break;
            case EAST:
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(baseX+width+2*factor, baseY, 2*factor, height);
                g.setColor(color);
                g.fillPolygon(Utils.createTriangle(baseX+width+2*factor, baseY, baseX+width+2*factor, baseY+height, baseX+width+4*factor, baseY+height/2));
                g.fillPolygon(Utils.createTriangle(baseX+width+4*factor, baseY, baseX+width+4*factor, baseY+height, baseX+width+6*factor, baseY+height/2));
                break;
            case WEST:
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(baseX-4*factor, baseY, 2*factor, height);
                g.setColor(color);
                g.fillPolygon(Utils.createTriangle(baseX-4*factor, baseY, baseX-4*factor, baseY+height, baseX-6*factor, baseY+height/2));
                g.fillPolygon(Utils.createTriangle(baseX-2*factor, baseY, baseX-2*factor, baseY+height, baseX-4*factor, baseY+height/2));
                break;
        }
    }
}
