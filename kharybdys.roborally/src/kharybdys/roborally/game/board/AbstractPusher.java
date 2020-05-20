package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementPriority;

/**
 *
 * @author MHK
 */
public abstract class AbstractPusher extends AbstractBoardElement {

    protected Direction pusherDirection;
    protected String pusherText;
    protected List<Integer> pusherPhases;

    public AbstractPusher(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot, Direction pusherDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
        this.pusherDirection=pusherDirection;
    }

    public AbstractPusher(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot, Direction pusherDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
        this.pusherDirection=pusherDirection;
    }

    public RoboRallyMovementPriority getMovementPriority()
    {
        return Movement.RoboRallyMovementPriority.PUSHER;
    }

    public Movement getBoardMovement(int phase) {
        if (pusherPhases.contains(phase)) {
            return new Movement(pusherDirection, 0, RoboRallyMovementPriority.PUSHER, 1, 0);
        } else {
            return null;
        }
    }

    @Override
    public AbstractPusher turn(int turnSteps, int newX, int newY)
    {
        super.turn(turnSteps, newX, newY);
        pusherDirection = pusherDirection==null ? null : pusherDirection.processRotate(turnSteps);
        return this;
    }

    public void paintElement(Graphics g, int baseX, int baseY, int factor) {
        Graphics2D g2d = (Graphics2D) g;
        int fontHeight = (size - (8 * factor)) / 2;
        int width = size - 2;
        int height = fontHeight + factor;
        int startX = baseX + 1;
        int startY = baseY + 1;
        int fontX = startX + factor;
        int fontY = startY + height - factor;
        switch (pusherDirection) {
        // note that the pusher needs to be drawn at the opposite side of the direction
            case NORTH:
                g2d.rotate(Math.PI, baseX + size / 2, baseY + size / 2);
                break;
            case SOUTH:
                // no rotation needed
                break;
            case EAST:
                g2d.rotate(Math.PI * 1.5, baseX + size / 2, baseY + size / 2);
                break;
            case WEST:
                g2d.rotate(Math.PI * 0.5, baseX + size / 2, baseY + size / 2);
                break;
        }
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontHeight));
        FontMetrics fm = g.getFontMetrics();
        int xCorr = (width - fm.stringWidth(pusherText)) / 2;
        g.setColor(Color.darkGray);
        g.fillRect(startX, startY, width, height);
        g.setColor(Color.yellow);
        g.drawString(pusherText, fontX + xCorr, fontY);
        switch (pusherDirection) {
        // note that the pusher needs to be drawn at the opposite side of the direction
            case NORTH:
                g2d.rotate(-Math.PI, baseX + size / 2, baseY + size / 2);
                break;
            case SOUTH:
                // no rotation needed
                break;
            case EAST:
                g2d.rotate(-Math.PI * 1.5, baseX + size / 2, baseY + size / 2);
                break;
            case WEST:
                g2d.rotate(-Math.PI * 0.5, baseX + size / 2, baseY + size / 2);
                break;
        }
    }
}
