package kharybdys.roborally.game.board;

import java.awt.Color;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

/**
 * Represents a rotator rotating counter-clockwise
 */
public class RotatorCounterClockwise extends AbstractRotator {

    public RotatorCounterClockwise( int xCoord, int yCoord, Collection<Direction> walls )
    {
       super( xCoord, yCoord, walls );
       color = Color.red;
       turnSteps = -1;
    }
}
