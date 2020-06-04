package kharybdys.roborally.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

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

	public static final int DEFAULT_FACTOR = 4;
	public static final int MAX_PHASE = 5 ;
	
	private int xSize;
	private int ySize;
    private Collection<Flag> flags;
    private Collection<Bot> bots;
    private Collection<BoardElement> laserMounts;
    // rootElement is the element at coordinates 0, 0
    private BoardElement rootElement;
    private Integer currentRound = 0;
    private Integer currentPhase = 1;
    private Integer id = 0;


	private static final Logger logger = LoggerFactory.getLogger( Game.class );

    public Game( Integer id, Integer currentRound, 
    		int xSize, int ySize, 
    		BoardElement rootElement, 
    		Collection<Bot> bots, 
    		Collection<Flag> flags,
			Collection<BoardElement> laserMounts ) 
    {
    	this.id = id;
    	this.currentRound = currentRound;
    	this.xSize = xSize;
    	this.ySize = ySize;
    	this.rootElement = rootElement;
    	this.bots = bots;
    	this.flags = flags;
    	this.laserMounts = laserMounts;
    	
    	for( AbstractMovingElement b : bots )
    	{
    		b.setGame( this );
    	}
    	
    	for( AbstractMovingElement f : flags)
    	{
    		f.setGame( this );
    	}
	}

    public byte[] getImage(int factor) 
    {
        int w = ( xSize + 2 ) * factor * AbstractBoardElement.baseSize;
        int h = ( ySize + 2 ) * factor * AbstractBoardElement.baseSize;
        BufferedImage bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
        Graphics g = bi.getGraphics();
        g.setColor( new Color( 31, 31, 31 ) );
        g.fillRect( 0, 0, w, h );
        this.paint( g, factor );
        g.dispose();
		try 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( bi, "png", baos );
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			logger.error( "Something went wrong writing the generated image to a byte array.", e );
			return null;
		}
    }

    public void paint( Graphics g, int factor ) 
    {
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
        for ( AbstractMovingElement flag : flags ) 
        {
            flag.paint( g, factor );
        }
        // TODO, if a bot is on a flag, should the flag number be visible somewhere? But how then.
        for ( AbstractMovingElement bot : bots ) 
        {
            bot.paint( g, factor );
        }
    }

	public void nextPhase() 
	{
		if( currentPhase == MAX_PHASE )
		{
			throw new IllegalStateException( "Cannot increase phase beyond " + MAX_PHASE + " on " + this );
		}
		this.currentPhase++ ;
	}

	public void nextRound() 
	{
		this.currentPhase = 1;
		this.currentRound++ ;
	}
	
	public Collection<AbstractMovingElement> getBotsAndFlags() 
	{
        List<AbstractMovingElement> botsAndFlags = new ArrayList<AbstractMovingElement>();
        botsAndFlags.addAll( bots );
        botsAndFlags.addAll( flags );
        return botsAndFlags;
	}

	public Bot getBot( Integer botId ) 
	{
		Optional<Bot> bot = bots.stream().filter( b -> b.getId().equals( botId ) ).findFirst();
		return bot.isPresent() ? bot.get() : null;
	}
	
	public Collection<Bot> getBots() 
	{
		return bots;
	}

	public Collection<Flag> getFlags() 
	{
		return flags;
	}

	public Integer getCurrentPhase() 
	{
		return currentPhase;
	}
	public Integer getId() 
	{
		return id;
	}
	
	public Integer getCurrentRound() 
	{
		return currentRound;
	}

	@Override
	public String toString()
	{
		return "Game(" + id + ")";
	}
}
