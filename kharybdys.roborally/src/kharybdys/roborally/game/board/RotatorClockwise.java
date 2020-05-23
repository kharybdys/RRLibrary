package kharybdys.roborally.game.board;

import java.awt.Color;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

/**
 * Represents a rotator rotating clockwise
 */
public class RotatorClockwise extends AbstractRotator {

    public RotatorClockwise( int xCoord, int yCoord, Collection<Direction> walls )
    {
       super( xCoord, yCoord, walls );
       color = new Color( 0, 127, 0 );
       turnSteps = 1;
    }
}
