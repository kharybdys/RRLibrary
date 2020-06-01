package kharybdys.roborally.game.board;

import java.awt.Graphics;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Flag;
import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.definition.Movement;

/**
 *  Models an element that can move over the board. Bots and Flags come to mind 
 */
public abstract class AbstractMovingElement {

	protected BoardElement currentLocation;
	protected BoardElement archiveMarker;
	
	protected int orderNumber;
	protected int lives;

	protected Boolean diedThisTurn = false;

	public AbstractMovingElement( Integer orderNumber ) 
	{
		this.orderNumber = orderNumber;
	}

	public void paint( Graphics g, int factor ) 
	{
		if( currentLocation != null )
		{
			int size = AbstractBoardElement.baseSize * factor;
			int baseX = currentLocation.getXCoordinate() * size;
			int baseY = ( currentLocation.getYCoordinate() + 1 ) * size;
			paintElement( g, baseX + 6 * factor, baseY + 6 * factor, size - 12 * factor, factor );
		}

	}

	public BoardElement getLocation() 
	{
		return currentLocation;
	}
	
	public void setLocation( BoardElement location )
	{
		this.currentLocation = location;
	}
	
	public void setArchiveMarker( BoardElement archiveMarker )
	{
		this.archiveMarker = archiveMarker;
	}

	public abstract void paintElement( Graphics g, int baseX, int baseY, int size, int factor );

	public abstract void processMovement( Movement movement );

	public void processDeath() 
	{
		setDiedThisTurn( Boolean.TRUE );
	}
	
	public void processReincarnation()
	{
		if( this instanceof Bot )
		{
			archiveMarker.setBot( (Bot) this );
		}
		if( this instanceof Flag )
		{
			archiveMarker.setFlag( (Flag) this );
		}
	}

	public abstract Game getGame();

	public abstract void setGame(Game game);

	public Boolean getDiedThisTurn() 
	{
		return diedThisTurn;
	}

	public void setDiedThisTurn( Boolean diedThisTurn ) 
	{
		this.diedThisTurn = diedThisTurn;
	}

    public Integer getLives() 
    {
        return lives;
    }

    public Integer getOrderNumber()
    {
    	return orderNumber;
    }
}
