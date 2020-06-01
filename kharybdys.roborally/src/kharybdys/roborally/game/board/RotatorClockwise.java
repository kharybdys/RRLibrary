package kharybdys.roborally.game.board;

import java.awt.Color;

/**
 * Represents a rotator rotating clockwise
 */
public class RotatorClockwise extends AbstractRotator {

    public RotatorClockwise()
    {
       color = new Color( 0, 127, 0 );
       turnSteps = 1;
    }
}
