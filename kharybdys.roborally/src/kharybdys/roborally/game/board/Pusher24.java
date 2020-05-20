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
public class Pusher24 extends AbstractPusher {


    public Pusher24(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot, Direction pusherDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot, pusherDirection);
        pusherPhases = new ArrayList<Integer>();
        pusherPhases.add(2);
        pusherPhases.add(4);
        pusherText=" 2 4 ";
    }

    public Pusher24(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot, Direction pusherDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot, pusherDirection);
        pusherPhases = new ArrayList<Integer>();
        pusherPhases.add(2);
        pusherPhases.add(4);
        pusherText=" 2 4 ";
    }
}
