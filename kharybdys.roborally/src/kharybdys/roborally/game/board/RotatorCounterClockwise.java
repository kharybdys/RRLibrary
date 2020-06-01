package kharybdys.roborally.game.board;

import java.awt.Color;

/**
 * Represents a rotator rotating counter-clockwise
 */
public class RotatorCounterClockwise extends AbstractRotator {

    public RotatorCounterClockwise()
    {
       color = Color.red;
       turnSteps = -1;
    }
}
