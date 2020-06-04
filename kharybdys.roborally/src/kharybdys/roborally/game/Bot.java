package kharybdys.roborally.game;

import java.awt.*;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.board.BoardElement;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.Movement;
import kharybdys.roborally.game.options.OptionCardDefinition;

/**
 *	Models a bot
 */
public class Bot extends AbstractMovingElement {

	public static final Integer INITIAL_HEALTH = 10 ;

    private Integer damage = 0;
    // number of the highest flag touched
	private Integer latestFlag = 0;
    private Direction facingDirection;


    public Bot( Integer id, Integer damage, Integer lives, Integer latestFlag, Integer orderNumber, Direction facingDirection ) 
    {
		super( id, orderNumber );
		this.lives = lives;
		this.latestFlag = latestFlag;
		this.facingDirection = facingDirection;
	}

    public Integer getDamage() {
        return damage;
    }

    public void takeDamage( Integer damage ) 
    {
        this.damage += damage;
        if ( this.damage >= INITIAL_HEALTH )
        { // you died
            processDeath();
        }
    }

    public Direction getFacingDirection() 
    {
        return facingDirection;
    }


	public int getLatestFlag() 
	{
		return latestFlag;
	}

	public boolean hasOptionCard( OptionCardDefinition optionCard ) 
	{
		// TODO Implement this method
		return false;
	}
	
    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int size, int factor) 
    {
        g.setColor(new Color(255, 0, 255, 255));
        switch (facingDirection) {
            case NORTH:
                g.fillPolygon(BoardElement.createTriangle(baseX, baseY + size / 2, baseX + size, baseY + size / 2, baseX + size / 2, baseY));
                g.fillRect(baseX, baseY + size / 2, size, size / 2);
                break;
            case SOUTH:
                g.fillPolygon(BoardElement.createTriangle(baseX, baseY + size / 2, baseX + size, baseY + size / 2, baseX + size / 2, baseY + size));
                g.fillRect(baseX, baseY, size, size / 2);
                break;
            case WEST:
                g.fillPolygon(BoardElement.createTriangle(baseX + size / 2, baseY, baseX + size / 2, baseY + size, baseX, baseY + size / 2));
                g.fillRect(baseX + size / 2, baseY, size / 2, size);
                break;
            case EAST:
                g.fillPolygon(BoardElement.createTriangle(baseX + size / 2, baseY, baseX + size / 2, baseY + size, baseX + size, baseY + size / 2));
                g.fillRect(baseX, baseY, size / 2, size);
                break;
        }
        g.setColor(new Color(0, 0, 0, 128));
        int fontHeight = (size);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontHeight));
        FontMetrics fm = g.getFontMetrics();
        int number = getOrderNumber();
        int xCorr = (size-2*factor - fm.stringWidth(number+"")) / 2;
        g.drawString(number+"", baseX+factor+xCorr, baseY+size -factor);
    }

    @Override
    public void processMovement( Movement movement )
    {
        updateLocation( movement );
        facingDirection = movement.getNewFacingDirection( facingDirection );
    }

    public void processDeath()
    {
        super.processDeath();

		currentLocation.setBot( null );
		currentLocation = null;

		this.lives-- ;
        this.damage = 2;
        facingDirection = Direction.NORTH;
    }
}
