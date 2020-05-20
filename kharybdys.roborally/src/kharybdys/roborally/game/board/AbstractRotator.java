package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementPriority;
import kharybdys.util.Utils;

/**
 *
 * @author MHK
 */
public abstract class AbstractRotator extends AbstractBoardElement {

    int turnSteps=0;
    Color color;
    
    public AbstractRotator(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
    }

    public AbstractRotator(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
    }

    public RoboRallyMovementPriority getMovementPriority()
    {
        return Movement.RoboRallyMovementPriority.ROTATOR;
    }

    @Override
    public Movement getBoardMovement(int phase) {
        return new Movement(null, turnSteps, RoboRallyMovementPriority.ROTATOR, 0, 0);
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int factor) {
        int sign = -1 * Utils.signum(turnSteps);
        g.setColor(Color.white);
        g.fillOval(baseX+2*factor, baseY+2*factor, size-4*factor, size-4*factor); 
        g.setColor(color);
        g.fillArc(baseX+3*factor, baseY+3*factor, size-6*factor, size-6*factor, 90+sign*10, 80*sign);
        g.setColor(Color.white);
        g.fillOval(baseX+5*factor, baseY+5*factor, size-10*factor, size-10*factor);
        g.setColor(Color.lightGray);
        g.fillOval(baseX+6*factor, baseY+6*factor, size-12*factor, size-12*factor);
        g.setColor(color);
        int corrSize = sign<0 ? size : 0;
        g.fillPolygon(Utils.createTriangle(baseX + corrSize + sign * 2 * factor, baseY + size / 2, baseX + corrSize + sign * 6 * factor, baseY + size / 2, baseX + corrSize + sign * 4 * factor, baseY + size / 2 + 2 * factor));
    }
}
