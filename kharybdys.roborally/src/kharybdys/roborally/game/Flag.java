package kharybdys.roborally.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.movement.Movement;

/**
 * Models a flag
 */
public class Flag extends AbstractMovingElement {
	
    public Flag( Integer id, Integer orderNumber ) 
    {
		super( id, orderNumber );
	}

    public void processDeath()
    {
        super.processDeath();

		currentLocation.setFlag( null );
		currentLocation = null;
		
		// TODO: Flags resurrect at the end of a phase instead of at the end of a turn
		// Cannot do it directly as bots on the archive spot should NOT touch this flag 
    }
    
    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int size, int factor)
    {
        g.setColor(Color.green);
        g.fillRect(baseX, baseY, size, size);
        g.setColor(new Color(0, 0, 0, 128));
        int fontHeight = (size);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontHeight));
        FontMetrics fm = g.getFontMetrics();
        int flagNumber = getOrderNumber();
        int xCorr = (size-2*factor - fm.stringWidth(flagNumber+"")) / 2;
        g.drawString(flagNumber+"", baseX+factor+xCorr, baseY+size -factor);
    }

    @Override
    public void processMovement( Movement movement )
    {
        // flags can only be moved by board movement:
        if (movement.getType() != Movement.MovementType.ROBOT_MOVEMENT)
        {
            updateLocation( movement );
        }
        // flags ignore turns
    }
}
