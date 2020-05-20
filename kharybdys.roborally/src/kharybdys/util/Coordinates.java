package kharybdys.util;

import kharybdys.roborally.game.definition.Direction;

public final class Coordinates {
    private int xCoord;
    private int yCoord;

    public Coordinates(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public boolean equals(Coordinates coords) {
        return xCoord == coords.getxCoord() &&  yCoord == coords.getyCoord();
    }

    public String toString()
    {
        return xCoord + " , " +  yCoord;
    }

    public Coordinates getNeighbouringCoordinates(Direction direction)
    {
        if (direction != null)
        {
            int xChange = 0;
            int yChange = 0;
            switch(direction)
            {
                case WEST: xChange = -1; break;
                case EAST: xChange = 1; break;
                case NORTH: yChange = 1; break;
                case SOUTH: yChange = -1; break;
            }
            return new Coordinates(xChange + getxCoord(), yChange + getyCoord());
        }
        else
        {
            return null;
        }
    }
}
