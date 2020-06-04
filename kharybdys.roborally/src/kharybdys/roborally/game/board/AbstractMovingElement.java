package kharybdys.roborally.game.board;

import java.awt.Graphics;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Flag;
import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.movement.Movement;

/**
 *  Models an element that can move over the board. Bots and Flags come to mind
 *  TODO: Maybe model archiveMarkers also as AbstractMovingElements? That way an archive marker at a moving flag can be modelled. 
 */
public abstract class AbstractMovingElement {

	protected BoardElement currentLocation;
	protected BoardElement archiveMarker;
	
	protected int orderNumber;
	protected int lives;

	protected Boolean diedThisTurn = false;
	protected Integer id;
	private Game game;

	public AbstractMovingElement( Integer id, Integer orderNumber ) 
	{
		this.id = id;
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

	public BoardElement getArchiveMarker()
	{
		return archiveMarker;
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

	public Integer getId() 
	{
	    return id;
	}

	public Game getGame() 
	{
	    return game;
	}

	public void setGame( Game game ) 
	{
	    this.game = game;
	}

	@Override
	public int hashCode() 
	{
	    int hash = 0;
	    hash += ( id != null ? id.hashCode() : 0 );
	    return hash;
	}

	@Override
	public boolean equals( Object object ) 
	{
	    // Warning - this method won't work in the case the id fields are not set
	    if ( !getClass().isInstance( object ) ) 
	    {
	        return false;
	    }
	    AbstractMovingElement other = (AbstractMovingElement) object;
	    if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) ) 
	    {
	        return false;
	    }
	    return true;
	}

	protected void updateLocation(Movement movement) {
		// TODO Auto-generated method stub
		
	}
}
