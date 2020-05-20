/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kharybdys.roborally.game.board;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;

/**
 *
 * @author MHK
 */
public class RotatorCounterClockwise extends AbstractRotator {

    public RotatorCounterClockwise(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot)
    {
       super(xCoord, yCoord, walls, laserMount, laserShot);
       color = Color.red;
       turnSteps=-1;
    }

    public RotatorCounterClockwise(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot)
    {
       super(xCoord, yCoord, walls, laserMount, laserShot);
       color = Color.red;
       turnSteps=-1;
    }
}
