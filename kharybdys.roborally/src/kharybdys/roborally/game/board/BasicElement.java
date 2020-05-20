/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementPriority;

/**
 *
 * @author MHK
 */
public class BasicElement extends AbstractBoardElement {
    // number will be used by types FLAG and STARTING and otherwise ignored.

    private int number;
    private BoardElementType type;

    public BasicElement(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot, int number, BoardElementType type) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
        this.number = number;
        this.type = type;
    }

    public BasicElement(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot, int number, BoardElementType type) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
        this.number = number;
        this.type = type;
    }

    @Override
    public BoardElementType getBoardElementType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public RoboRallyMovementPriority getMovementPriority()
    {
        return Movement.RoboRallyMovementPriority.NONE;
    }

    @Override
    public Movement getBoardMovement(int phase) {
        return new Movement(null, 0, RoboRallyMovementPriority.ROBOT_MOVEMENT, 0, 0);
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int factor) {
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, size - (8 * factor));
        String charToDraw = "";
        int xAdjustment = 4 * factor;
        switch (type) {
            case STARTING:
                g.setColor(Color.black);
                charToDraw = number + "";
                g.drawOval(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                break;
            case REPAIR:
                charToDraw = "R";
                g.setColor(Color.darkGray);
                g.fillRect(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                g.setColor(Color.white);
                xAdjustment -= factor;
                break;
            case OPTION:
                charToDraw = "O";
                g.setColor(Color.darkGray);
                g.fillRect(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                g.setColor(Color.white);
                xAdjustment -= factor;
                break;
            case HOLE:
                g.setColor(Color.black);
                g.fillRect(baseX + 1, baseY + 1, size - 2, size - 2);
/*                g.setColor(Color.black);
                g.fillRect(baseX + 1+factor, baseY + 1+factor, size - 2-2*factor, size - 2-2*factor);*/
                break;
        }
        if (charToDraw.length() > 0) {
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            int xCorr = (size - 8*factor - fm.stringWidth(charToDraw))/2;
            int yCorr = (size - 8*factor - fm.getMaxAscent()+fm.getMaxDescent())/2;
            g.drawString(charToDraw, baseX + 4 * factor + xCorr, baseY + size - 4 * factor -yCorr);
        }
    }
}
