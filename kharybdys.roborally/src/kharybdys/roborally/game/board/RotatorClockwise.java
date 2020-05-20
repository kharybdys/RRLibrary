package kharybdys.roborally.game.board;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;

/**
 *
 * @author MHK
 */
public class RotatorClockwise extends AbstractRotator {

    public RotatorClockwise(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot)
    {
       super(xCoord, yCoord, walls, laserMount, laserShot);
       color=new Color(0, 127, 0);
       turnSteps = 1;
    }

    public RotatorClockwise(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot)
    {
       super(xCoord, yCoord, walls, laserMount, laserShot);
       color=new Color(0, 127, 0);
       turnSteps = 1;
    }
}
