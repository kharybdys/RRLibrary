package kharybdys.roborally.game.board;

import java.util.ArrayList;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

/**
 * Represents a pusher that pushes in the phases 1, 3 and 5
 */
public class Pusher135 extends AbstractPusher {

    public Pusher135(int xCoord, int yCoord, Collection<Direction> walls, Direction pusherDirection)
    {
        super( xCoord, yCoord, walls, pusherDirection );
        pusherPhases = new ArrayList<Integer>();
        pusherPhases.add( 1 );
        pusherPhases.add( 3 );
        pusherPhases.add( 5 );
        pusherText="1 3 5";
    }
}
