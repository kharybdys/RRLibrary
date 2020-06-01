package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Defines a Basic boardElement. These have no special logic but do have different styles of drawing
 */
public class BasicElement extends AbstractBoardElement {

	private BasicElementType type = BasicElementType.BASIC;

    /**
     * Adds the basic element type to this board element
     * 
     * @param type The type to add
     * 
     * @return this object, for chaining
     */
    public BasicElement withBasicElementType( BasicElementType type )
    {
    	this.type = type;

    	return this;
    }

    /**
     * Returns the type of the boardElement (mostly for checking if it's a hole).
     * 
     * @return The type of this boardElement
     */
	@Override
    public BasicElementType getBoardElementType() {
        return type;
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int factor) 
    {
        Font f = new Font( Font.SANS_SERIF, Font.PLAIN, size - ( 8 * factor ) );
        String charToDraw = "";
        switch (type) 
        {
            case STARTING_1:
            case STARTING_2:
            case STARTING_3:
            case STARTING_4:
            case STARTING_5:
            case STARTING_6:
            case STARTING_7:
            case STARTING_8:
                charToDraw = type.getNumber() + "";
                g.setColor(Color.black);
                g.drawOval(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                break;
            case REPAIR:
                charToDraw = "R";
                g.setColor(Color.darkGray);
                g.fillRect(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                break;
            case OPTION:
                charToDraw = "O";
                g.setColor(Color.darkGray);
                g.fillRect(baseX + (3 * factor), baseY + (3 * factor), size - (6 * factor), size - (6 * factor));
                break;
            case HOLE:
                g.setColor(Color.black);
                g.fillRect(baseX + 1, baseY + 1, size - 2, size - 2);
/*                g.setColor(Color.black);
                g.fillRect(baseX + 1+factor, baseY + 1+factor, size - 2-2*factor, size - 2-2*factor);*/
                break;
		default:
			break;
        }
        if (charToDraw.length() > 0) {
            g.setColor(Color.white);
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            int xCorr = ( size - 8 * factor - fm.stringWidth( charToDraw ) ) / 2;
            int yCorr = ( size - 8 * factor - fm.getMaxAscent() + fm.getMaxDescent() ) / 2;
            g.drawString( charToDraw, baseX + 4 * factor + xCorr, baseY + size - 4 * factor - yCorr );
        }
    }
}
