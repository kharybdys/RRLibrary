package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

/**
 * Defines a Basic boardElement. These have no special logic but do have different styles of drawing
 * TODO: Remove type Flag as that has its own class
 */
public class BasicElement extends AbstractBoardElement {
    // number will be used by type STARTING and otherwise ignored.

    private int number;
    private BasicElementType type;

    /**
     * Constructor for a BasicElement
     * Adds a number and a type
     * 
     * @param xCoord The xCoordinate of this boardElement
     * @param yCoord The yCoordinate of this boardElement
     * @param walls  The collection of directions that have walls on this boardElement
     * @param number The number of this element (0 equals not used)
     * @param type   The type of this BasicElement
     */
    public BasicElement(int xCoord, int yCoord, Collection<Direction> walls, int number, BasicElementType type) 
    {
        super( xCoord, yCoord, walls );
        this.number = number;
        this.type = type;
    }

    /**
     * Constructor for a BasicElement
     * Number not used, type is BASIC
     * 
     * @param xCoord The xCoordinate of this boardElement
     * @param yCoord The yCoordinate of this boardElement
     * @param walls  The collection of directions that have walls on this boardElement
     */
    public BasicElement(int xCoord, int yCoord, Collection<Direction> walls) 
    {
        super( xCoord, yCoord, walls );
        this.number = 0;
        this.type = BasicElementType.BASIC;
    }

	@Override
    public BasicElementType getBoardElementType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int factor) 
    {
        Font f = new Font( Font.SANS_SERIF, Font.PLAIN, size - ( 8 * factor ) );
        String charToDraw = "";
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
                break;
            case OPTION:
                charToDraw = "O";
                g.setColor(Color.darkGray);
                g.fillRect(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                g.setColor(Color.white);
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
            int xCorr = ( size - 8 * factor - fm.stringWidth( charToDraw ) ) / 2;
            int yCorr = ( size - 8 * factor - fm.getMaxAscent() + fm.getMaxDescent() ) / 2;
            g.drawString( charToDraw, baseX + 4 * factor + xCorr, baseY + size - 4 * factor - yCorr );
        }
    }
}
