package kharybdys.roborally.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.AbstractBoardElement;
import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.board.BoardElement;
import kharybdys.roborally.game.definition.Direction;

/**
 *	Models everything a game has to know
 */
public class Game {

	private int xSize;
	private int ySize;
    private Collection<Flag> flags;
    private Collection<Bot> bots;
    private Collection<BoardElement> laserMounts;
    // rootElement is the element at coordinates 0, 0
    private BoardElement rootElement;
    private Integer currentRound = 0;

	private static final Logger logger = LoggerFactory.getLogger( Game.class );

    public Game( int xSize, int ySize, 
    		BoardElement rootElement, 
    		Collection<Bot> bots, 
    		Collection<Flag> flags,
			Collection<BoardElement> laserMounts ) 
    {
    	this.xSize = xSize;
    	this.ySize = ySize;
    	this.rootElement = rootElement;
    	this.bots = bots;
    	this.flags = flags;
    	this.laserMounts = laserMounts;
    	
    	for( Bot b : bots )
    	{
    		b.setGame( this );
    	}
    	
    	for( Flag f : flags)
    	{
    		f.setGame( this );
    	}
	}

	public Dimension getDimension(int factor)
    {
        return new Dimension( xSize * factor * AbstractBoardElement.baseSize, ySize * factor * AbstractBoardElement.baseSize );
    }

    public void setFlags( Collection<Flag> flags )
    {
        this.flags = flags;
    }

    public void setBots( Collection<Bot> bots )
    {
    	this.bots = bots;
        for (Bot b : bots)
        {
            b.setGame( this );
        }
    }

    public boolean outOfYBounds( int y )
    {
        return y < 0 || y >= ySize;
    }

    public boolean outOfXBounds( int x )
    {
        return x < 0 || x >= xSize;
    }

    public BufferedImage getImage(int factor) 
    {
        int w = ( xSize + 2 ) * factor * AbstractBoardElement.baseSize;
        int h = ( ySize + 2 ) * factor * AbstractBoardElement.baseSize;
        BufferedImage bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
        Graphics g = bi.getGraphics();
        g.setColor( new Color( 31, 31, 31 ) );
        g.fillRect( 0, 0, w, h );
        this.paint( g, factor );
        g.dispose();
        return bi;
    }

    public void paint( Graphics g, int factor ) 
    {
        int w = xSize * factor * AbstractBoardElement.baseSize;
        int h = ySize * factor * AbstractBoardElement.baseSize;
        BoardElement currentRowStart = rootElement;
        while( currentRowStart != null )
        {
            BoardElement currentElement = currentRowStart;
            while( currentElement != null )
            {
            	currentElement.paint(g, factor);
            	currentElement = currentElement.getNeighbour( Direction.EAST );
            }
            currentRowStart = currentRowStart.getNeighbour( Direction.SOUTH );
        }
        // paint flags first then bots so the bots actually show
        for ( Flag flag : flags ) 
        {
            flag.paint( g, factor );
        }
        // TODO, if a bot is on a flag, should the flag number be visible somewhere? But how then.
        for ( Bot bot : bots ) 
        {
            bot.paint( g, factor );
        }
    }


	public Collection<AbstractMovingElement> getBotsAndFlags() 
	{
        List<AbstractMovingElement> botsAndFlags = new ArrayList<AbstractMovingElement>();
        botsAndFlags.addAll( bots );
        botsAndFlags.addAll( flags );
        return botsAndFlags;
	}

	public Collection<Bot> getBots() 
	{
		return bots ;
	}

	public Integer getCurrentRound() 
	{
		return currentRound;
	}
}
