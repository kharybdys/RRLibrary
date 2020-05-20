/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kharybdys.roborally.game.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;

/**
 *
 * @author MHK
 */
public class Pusher135 extends AbstractPusher {

    public Pusher135(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot, Direction pusherDirection)
    {
       super(xCoord, yCoord, walls, laserMount, laserShot, pusherDirection);
        pusherPhases = new ArrayList<Integer>();
        pusherPhases.add(1);
        pusherPhases.add(3);
        pusherPhases.add(5);
        pusherText="1 3 5";
    }

    public Pusher135(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot, Direction pusherDirection)
    {
       super(xCoord, yCoord, walls, laserMount, laserShot, pusherDirection);
        pusherPhases = new ArrayList<Integer>();
        pusherPhases.add(1);
        pusherPhases.add(3);
        pusherPhases.add(5);
        pusherText="1 3 5";
    }
}
