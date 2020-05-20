package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;

/**
 * Contains information about walls & lasers and the business methods to handle those
 * @author MHK
 */
public abstract class AbstractBoardElement implements BoardElement{

    private List<Direction> walls;
    /*
     * Only one of the following two should ever be not null / not empty.
     */
    private Map<Direction, Integer> laserMount;
    private Map<Direction, Integer> laserShot;
    protected int xCoord;
    protected int yCoord;
    public static final int baseSize = 20;
    protected int size;

    public AbstractBoardElement(int xCoord, int yCoord, List<Direction> walls, Map<Direction, Integer> laserMount, Map<Direction, Integer> laserShot)
    {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.walls = walls;
        if (this.walls == null) {
            this.walls = new ArrayList<Direction>();
        }
        this.laserShot=laserShot;
        if (this.laserShot == null)
        {
        this.laserShot = new HashMap<Direction, Integer>();
        }
        this.laserMount=laserMount;
        if (this.laserMount == null)
        {
        this.laserMount = new HashMap<Direction, Integer>();
        }

    }
    public AbstractBoardElement(int xCoord, int yCoord, List<Direction> walls, Direction laserMount, List<Direction> laserShot) {
        this(xCoord, yCoord, walls, (Map<Direction, Integer>) null, null);
        if (laserShot != null) {
            for (Direction dir : laserShot)
            {
               this.laserShot.put(dir, 1);
            }
        }
        if (laserMount != null)
        {
            this.laserMount.put(laserMount, 1);
        }
    }

    public int compareTo(BoardElement rrbe)
    {
        return this.getMovementPriority().compareTo(rrbe.getMovementPriority());
    }

    public Movement adjustMovementForWalls(Movement oldMovement) {
        if (walls.isEmpty()) {
            return oldMovement;
        } else {
            if (walls.contains(oldMovement.getMovingDirection())) {
                return oldMovement.adjustMovement();
            } else {
                return oldMovement;
            }
        }
    }

    public Movement correctingMovementAfter(Direction entry)
    { // default behaviour, should be overriden by those elements that do need to correct.
        return null;
    }

    @Override
    public BoardElementType getBoardElementType() {
        // everything is basic unless otherwise specified.
        return AbstractBoardElement.BoardElementType.BASIC;
    }

    public AbstractBoardElement turn(int turnSteps, int newX, int newY) {
        this.xCoord = newX;
        this.yCoord = newY;
        List<Direction> dirs = new ArrayList<Direction>();
        for (Direction dir : walls) {
            dirs.add(dir.processRotate(turnSteps));
        }
        walls = dirs;
        Map<Direction, Integer> lasers = new HashMap<Direction, Integer>();
        for (Direction dir : laserShot.keySet()) {
            lasers.put(dir.processRotate(turnSteps), laserShot.get(dir));
        }
        laserShot = lasers;
        lasers = new HashMap<Direction, Integer>();
        for (Direction dir : laserMount.keySet()) {
            lasers.put(dir.processRotate(turnSteps), laserMount.get(dir));
        }
        laserMount = lasers;
        return this;
    }

    public void paint(Graphics g, int boardXOffset, int boardYOffset, int ySizePanel, int factor) {
        size = baseSize * factor;
        int baseX = (boardXOffset + xCoord) * size;
        int baseY = ySizePanel - ((boardYOffset + yCoord + 1) * size);
        g.setColor(Color.black);
        g.drawRect(baseX, baseY, size - 1, size - 1);
        g.setColor(Color.lightGray);
        g.fillRect(baseX + 1, baseY + 1, size - 2, size - 2);
        paintElement(g, baseX, baseY, factor);
        g.setColor(Color.yellow);
        // corners are to be filled by north or south walls
        if (walls.contains(Direction.NORTH)) {
            g.fillRect(baseX, baseY, size, factor);
        }
        if (walls.contains(Direction.SOUTH)) {
            g.fillRect(baseX, baseY + size - factor, size, factor);
        }
        if (walls.contains(Direction.WEST)) {
            g.fillRect(baseX, baseY, factor, size);
        }
        if (walls.contains(Direction.EAST)) {
            g.fillRect(baseX + size - factor, baseY, factor, size);
        }
        g.setColor(Color.red);
        for (Direction dir : laserMount.keySet()) {
            for (int i = 1; i< laserMount.get(dir)+1; i++)
            {
              drawLaserMount(g, baseX, baseY, factor, dir, (i+0.0)/(laserMount.get(dir)+1));
            }
        }
        for (Direction dir : laserShot.keySet()) {
            for (int i = 1; i< laserShot.get(dir)+1; i++)
              {
                drawLaserShot(g, baseX, baseY, factor, dir, (i+0.0)/(laserShot.get(dir)+1));
              }
          }
    }

    public abstract void paintElement(Graphics g, int baseX, int baseY, int factor);

    public boolean existsLaserMount(Direction directionLaserFire) {
        return laserMount != null && laserMount.containsKey(directionLaserFire);
    }

    @Override
    public boolean hasWall(Direction side) {
        return walls != null && walls.contains(side);
    }

    public List<Direction> getDirectionLaserFire() {
        List<Direction> result = new ArrayList<Direction>();
        if (laserShot != null)
        {
            result.addAll(laserShot.keySet());
        }
        if (laserMount != null)
        {
            result.addAll(laserMount.keySet());
        }
        return result;
    }

    public Integer getLaserDamage(Direction directionLaserFire)
    {
        if (laserShot != null && laserShot.containsKey(directionLaserFire))
        {
            return laserShot.get(directionLaserFire);
        }
        if (laserMount != null && laserMount.containsKey(directionLaserFire))
        {
            return laserMount.get(directionLaserFire);
        }
        // default
        return 0;
    }

    public enum BoardElementType {

        BASIC, STARTING, REPAIR, OPTION, HOLE
    }

    private void drawLaserMount(Graphics g, int baseX, int baseY, int factor, Direction dir, double compensation)
    {
        int laserStartX = 0;
        int laserStartY = 0;
        int laserEndX = 0;
        int laserEndY = 0;
        int startAngle = 0;
        int mountBaseX = 0;
        int mountBaseY = 0;
        switch (dir) {
            case NORTH:
                startAngle = 180;
                mountBaseX = new Double(size * compensation).intValue() - (3 * factor);
                mountBaseY = -2 * factor;
                laserStartX = baseX + new Double(size * compensation).intValue();
                laserStartY = baseY + factor;
                laserEndX = laserStartX;
                laserEndY = baseY + size - 2;
                if (!walls.contains(Direction.SOUTH)) {
                    laserEndY+=factor;
                }
                break;
            case SOUTH:
                startAngle = 0;
                mountBaseX = new Double(size * compensation).intValue() - (3 * factor);
                mountBaseY = size - 4 * factor;
                laserStartX = baseX + new Double(size * compensation).intValue();
                laserStartY = baseY + factor;
                laserEndX = laserStartX;
                laserEndY = baseY + size - 1 - factor;
                if (!walls.contains(Direction.NORTH)) {
                    laserStartY-=factor;
                }
                break;
            case EAST:
                startAngle = 90;
                mountBaseX = size - (4 * factor) ;
                mountBaseY = new Double(size * compensation).intValue() - (3 * factor);
                laserStartX = baseX + 1;
                laserStartY = baseY + new Double(size * compensation).intValue();
                laserEndX = baseX + size - 1 - factor;
                laserEndY = laserStartY;
                if (!walls.contains(Direction.WEST)) {
                    laserStartX-=factor;
                }
                break;
            case WEST:
                startAngle = 270;
                mountBaseX = (-2 * factor);
                mountBaseY = new Double(size * compensation).intValue() - 3 * factor;
                laserStartX = baseX + factor;
                laserStartY = baseY + new Double(size * compensation).intValue();
                laserEndX = baseX + size - 1 - factor;
                laserEndY = laserStartY;
                if (!walls.contains(Direction.EAST)) {
                    laserEndX+=factor;
                }
                break;
        }
        g.fillArc(baseX + mountBaseX, baseY + mountBaseY, factor * 6, factor * 6, startAngle, 180);
        g.drawLine(laserStartX, laserStartY, laserEndX, laserEndY);
    }

    private void drawLaserShot(Graphics g, int baseX, int baseY, int factor, Direction dir, double compensation)
    {
        int laserStartX = 0;
        int laserStartY = 0;
        int laserEndX = 0;
        int laserEndY = 0;
        if (dir.equals((Direction.NORTH)) || dir.equals(Direction.SOUTH)) {
            laserStartX = baseX + new Double(size * compensation).intValue();
            laserStartY = baseY + 1;
            laserEndX = laserStartX;
            laserEndY = baseY + size - 2;
            if (!walls.contains(Direction.SOUTH)) {
                laserEndY+=factor;
            }
            if (!walls.contains(Direction.NORTH)) {
                laserStartY-=factor;
            }
            g.drawLine(laserStartX, laserStartY, laserEndX, laserEndY);
        }
        if (dir.equals(Direction.EAST) || dir.equals(Direction.WEST)) {
            laserStartX = baseX + 1;
            laserStartY = baseY + new Double(size * compensation).intValue();
            laserEndX = baseX + size - 2;
            laserEndY = laserStartY;
            if (!walls.contains(Direction.EAST)) {
                laserEndX+=factor;
            }
            if (!walls.contains(Direction.WEST)) {
                laserStartX-=factor;
            }
            g.drawLine(laserStartX, laserStartY, laserEndX, laserEndY);
        }
    }
}
