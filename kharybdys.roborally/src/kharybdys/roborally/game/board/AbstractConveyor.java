package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.Movement.RoboRallyMovementPriority;

/**
 *
 * @author MHK
 */
public abstract class AbstractConveyor extends AbstractBoardElement {

    protected List<Direction> startingDirections;
    protected Direction endDirection;
    protected RoboRallyMovementPriority priority;
    protected Color color;
    protected final static Color BACKGROUND_COLOR = Color.black;

    public AbstractConveyor(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot, List<Direction> startingDirections, Direction endDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
        this.startingDirections = startingDirections;
        this.endDirection = endDirection;
    }

    public AbstractConveyor(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot, List<Direction> startingDirections, Direction endDirection) {
        super(xCoord, yCoord, walls, laserMount, laserShot);
        this.startingDirections = startingDirections;
        this.endDirection = endDirection;
    }

    public RoboRallyMovementPriority getMovementPriority()
    {
        return priority;
    }

    @Override
    public Movement getBoardMovement(int phase) {
        return new Movement(endDirection, 0, priority, 1, 0);
    }

    @Override
    public Movement correctingMovementAfter(Direction entry)
    {
        if(startingDirections.contains(entry) && !entry.processRotate(2).equals(endDirection))
        {
            return new Movement(null, entry.getTurns(endDirection), RoboRallyMovementPriority.ROTATOR, 0, 0);
        }
        else
        { // no need to turn.
            return null;
        }
    }

    @Override
    public AbstractConveyor turn(int turnSteps, int newX, int newY)
    {
        super.turn(turnSteps, newX, newY);
        this.endDirection = this.endDirection==null ? null : this.endDirection.processRotate(turnSteps);
        List<Direction> dirs = new ArrayList<Direction>();
        for (Direction dir : startingDirections)
        {
            dirs.add(dir.processRotate(turnSteps));
        }
        startingDirections = dirs;
        return this;
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int factor) {

        int width = size - 8 * factor;
        int height = size - 8 * factor;
        g.setColor(color);
        g.fillRect(baseX, baseY, size, size);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(baseX + 4 * factor, baseY + 4 * factor, width, height);
        if (startingDirections.contains(Direction.SOUTH) || endDirection.equals(Direction.SOUTH)) {
            g.fillRect(baseX + 4 * factor, baseY + size - 4 * factor, width, 4 * factor);
        }
        if (startingDirections.contains(Direction.NORTH) || endDirection.equals(Direction.NORTH)) {
            g.fillRect(baseX + 4 * factor, baseY, width, 4 * factor);
        }
        if (startingDirections.contains(Direction.EAST) || endDirection.equals(Direction.EAST)) {
            g.fillRect(baseX + size - 4 * factor, baseY + 4 * factor, 4 * factor, height);
        }
        if (startingDirections.contains(Direction.WEST) || endDirection.equals(Direction.WEST)) {
            g.fillRect(baseX, baseY + 4 * factor, 4 * factor, height);
        }
        int innerBaseX = baseX + 4 * factor + 2 * factor;
        int innerBaseY = baseY + 4 * factor + 2 * factor;
        int innerWidth = width - 4 * factor;
        int innerHeight = height - 4 * factor;
        for (Direction startingDirection : startingDirections) {
            g.setColor(color);
            List<Direction> directions = new ArrayList<Direction>();
            directions.add(startingDirection);
            directions.add(endDirection);
            if (directions.contains(Direction.NORTH)) {
                g.fillRect(innerBaseX + 2 * factor, innerBaseY - 2 * factor, innerWidth - 4 * factor, 2 * factor);
            }
            if (directions.contains(Direction.SOUTH)) {
                g.fillRect(innerBaseX + 2 * factor, innerBaseY + innerHeight, innerWidth - 4 * factor, 2 * factor);
            }
            if (directions.contains(Direction.EAST)) {
                g.fillRect(innerBaseX + innerWidth, innerBaseY + 2 * factor, 2 * factor, innerHeight - 4 * factor);
            }
            if (directions.contains(Direction.WEST)) {
                g.fillRect(innerBaseX - 2 * factor, innerBaseY + 2 * factor, 2 * factor, innerHeight - 4 * factor);
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.NORTH)) { // draw a rectangle. Do not use the outermost 4xfactor pixels
                g.fillRect(innerBaseX + 2 * factor, innerBaseY, innerWidth - 4 * factor, innerHeight);
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.EAST)) {  // fill an arc that covers the entire rectangle we were given
                g.fillArc(innerBaseX + 2 * factor, innerBaseY + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 90, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX + innerWidth - 2 * factor, innerBaseY + innerHeight - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 90, 90);
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.WEST)) {
                g.fillArc(innerBaseX - innerWidth + 2 * factor, innerBaseY + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 0, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX - 2 * factor, innerBaseY + innerHeight - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 0, 90);
            }
            if (directions.contains(Direction.WEST) && directions.contains(Direction.NORTH)) {
                g.fillArc(innerBaseX - innerWidth + 2 * factor, innerBaseY - innerHeight + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 270, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX - 2 * factor, innerBaseY - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 270, 90);
            }
            if (directions.contains(Direction.EAST) && directions.contains(Direction.NORTH)) {
                g.fillArc(innerBaseX + 2 * factor, innerBaseY - innerHeight + 2 * factor, 2 * (innerWidth - 2* factor), 2 * (innerHeight - 2 * factor), 180, 90);
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(innerBaseX + innerWidth - 2 * factor, innerBaseY - 2 * factor, innerWidth - 4 * factor, innerHeight - 4 * factor, 180, 90);
            }
            if (directions.contains(Direction.EAST) && directions.contains(Direction.WEST)) { // draw a rectangle. Do not use the outermost 4xfactor pixels.
                g.fillRect(innerBaseX, innerBaseY + 2 * factor, innerWidth, innerHeight - 4 * factor);
            }
        }
        drawInnerField(g, innerBaseX, innerBaseY, innerWidth, innerHeight, factor);
    }

    // draws the arrows part of the conveyor belt
    public abstract void drawInnerField(Graphics g, int baseX, int baseY, int width, int height, int factor);

}
