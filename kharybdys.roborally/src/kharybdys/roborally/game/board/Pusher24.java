package kharybdys.roborally.game.board;

import java.util.ArrayList;

/**
 * Represents a pusher that pushes in phases 2 and 4
 */
public class Pusher24 extends AbstractPusher {


    public Pusher24() 
    {
        pusherPhases = new ArrayList<Integer>();
        pusherPhases.add( 2 );
        pusherPhases.add( 4 );
        pusherText=" 2 4 ";
    }
}
