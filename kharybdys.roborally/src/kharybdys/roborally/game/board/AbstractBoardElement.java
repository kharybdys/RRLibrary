package kharybdys.roborally.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Flag;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;

/**
 * Contains information about walls & lasers and the business methods to handle those
 */
public abstract class AbstractBoardElement implements BoardElement
{

	/**
	 * baseSize: CANNOT CHANGE WITHOUT PAINTING LOGIC CHANGING TOO
	 */
    public static final int baseSize = 20;
	private static final Logger logger = LoggerFactory.getLogger( AbstractBoardElement.class );

    /**
     * The collection of walls
     */
    private Collection<Direction> walls = EnumSet.noneOf( Direction.class );

    /**
     * The bot currently at this boardelement
     */
    protected Bot bot = null;

    /**
     * The flag currently at this boardelement
     */
    protected Flag flag = null;
    
    /**
     * The lasers available on this boardelement (can be one vertical and one horizontal)
     */
    private Collection<Laser> lasers = new ArrayList<Laser>();
    
    /**
     * The boardelements that surround us.
     */
    private Map<Direction, BoardElement> neighbours = new EnumMap<Direction, BoardElement>( Direction.class );
    
    /**
     * The xCoordinate of this element
     */
    protected int xCoord = -1;
    
    /**
     * The yCoordinate of this element
     */
    protected int yCoord = -1;
    
    /**
     * How big to draw this boardElement (pixels by pixels)
     */
    protected int size;

    /**
     * Setup method, adds all directions in the given collection
     * 
     * @param walls The direction(s) of the wall(s)
     * 
     * @return this object, for chaining
     */
    public AbstractBoardElement withWalls( Collection<Direction> walls )
    {
    	this.walls.addAll( walls );
    	
    	return this;
    }
    
    /**
     * Setup method, adds/sets the coordinates
     * 
     * @param xCoord The xCoordinate
     * @param yCoord The yCoordinate
     * 
     * @return this object, for chaining
     */
    public AbstractBoardElement withCoordinates( int xCoord, int yCoord )
    {
    	this.xCoord = xCoord;
    	this.yCoord = yCoord;
    	
    	return this;
    }
    
    /**
     * Setup method, adds a boardElement in the given direction as neighbour
     * Also adds us to the neighbour's neighbour list in the opposite direction
     * 
     * @param direction The direction to add the given boardElement to
     * @param neighbour The boardElement to add
     */
    public void addNeighbour( Direction direction, BoardElement neighbour )
    {
    	if( ! neighbour.equals( neighbours.get( direction ) ) )
    	{
    		// cleanup the current neighbour?
    		removeNeighbour( direction );
    		
    		// Add the neighbour relations
	    	neighbours.put( direction, neighbour );
	    	neighbour.addNeighbour( direction.processRotate( 2 ), this );
    	}
    }
    
    /**
     * Setup method, removes the neighbour in the given direction.
     * Also removes us from the neighbour's neighbour list in the opposite direction
     * 
     * @param direction The direction to remove
     */
    public void removeNeighbour( Direction direction )
    {
    	BoardElement neighbour = neighbours.get( direction );
    	if( neighbour != null )
    	{
    		neighbours.remove( direction );
    		neighbour.removeNeighbour( direction.processRotate( 2 ) );
    		
    	}
    }
    
    /**
     * Adds a laser to this boardElement and its neighbours until this laser hits a wall
     * Used recursively
     * 
     * @param strength    The strength of the laser
     * @param originating The originating direction
     * @param preceding   The preceding boardElement (if any)
     */
    public void addLaser( int strength, Direction originating, BoardElement preceding )
    {
    	// Decide whether the laser continues by finding followupBoardElement
    	BoardElement followup = null;
    	Direction exiting = originating.processRotate( 2 );

    	if( ! walls.contains( exiting ) )
    	{
    		// This can return null which is what we want here
    		followup = neighbours.get( exiting );
    	}
    	
    	lasers.add( new Laser( strength, originating, preceding, followup ) );
    	
    	if( followup != null )
    	{
    		followup.addLaser( strength, originating, this );
    	}
    }

	/**
	 * Returns the X Coordinate of this boardElement
	 * 
	 * @return the X Coordinate
	 */
	public int getXCoordinate()
	{
		return xCoord;
	}

	/**
	 * Returns the Y Coordinate of this boardElement
	 * 
	 * @return the Y Coordinate
	 */
	public int getYCoordinate()
	{
		return yCoord;
	}

	/**
     * Gets the neighbour in the given direction. Always returns a boardElement TODO: Currently doesn't
     * 
     * @param direction The direction for which to return the neighbour
     * 
     * @return The neighbour at that direction
     */
    public BoardElement getNeighbour( Direction direction )
    {
//    	return neighbours.containsKey( direction ) ? neighbours.get( direction ) : outsideElement;
    	return neighbours.get( direction );
    }
    
    /**
     * Gets the neighbour in the given direction. Will return null if no element is specified
     * 
     * @param direction The direction for which to return a neighbour
     * 
     * @return The neighbour at that direction, or null if none present
     */
    protected BoardElement getInternalNeighbour( Direction direction )
    {
    	return neighbours.get( direction );
    }
    
    /**
     * Get the basic movement that this board element enacts on the bot in the given phase.
     * Default implementation: no movements
     * 
     * @param phase The phase we're interested in
     * 
     * @return a (possibly empty) collection of Movement objects representing the boardMovement 
     *         as applied to all movingElements currently on this boardElement
     */
    public Collection<Movement> getBoardMovements( int phase ) 
    {
        return Collections.emptyList();
    }

    /**
     * Adjusts the given movement to comply with the rules (bumping into walls)
     * 
     * @param movement The movement to adjust
     * 
     * @return The new movement item representing the changed movement
     */
    public Movement adjustMovementForWalls(Movement oldMovement) 
    {
        if ( walls.isEmpty() || ! oldMovement.changesPosition() ) 
        {
            return oldMovement;
        } 
        else 
        {
            if ( walls.contains( oldMovement.getMovingDirection() ) ) 
            {
                return oldMovement.stopMovement();
            } 
            else 
            {
                return oldMovement;
            }
        }
    }

    /**
     * Returns the type of the boardElement (mostly for checking if it's a hole).
     * 
     * @return The type of this boardElement
     */
    public BasicElementType getBoardElementType() {
        // everything is basic unless otherwise specified.
        return BasicElementType.BASIC;
    }
    
	/**
     * Helper method to check whether a wall exists in that direction.
     * 
     * @param side The direction where to check for a wall
     * 
     * @return Whether a wall exists there
     */
    public boolean hasWall( Direction side ) 
    {
        return walls.contains( side );
    }

    /**
     * Sets the bot at this element
     * 
     * @param bot The bot to set at this element
     */
    public void setBot( Bot bot )
    {
    	if( this.bot != null && bot != null && ! this.bot.equals( bot ) )
    	{
    		logger.warn( "Replacing bot {} with bot {} at boardElement {}", this.bot, bot, this );
    		throw new UnsupportedOperationException( "Cannot replace one bot with another at a boardElement" ); 
    	}
    	
    	this.bot = bot;
    	bot.setLocation( this );
    }
    
    /**
     * Sets the flag at this element
     * 
     * @param flag The flag to set at this element
     */
    public void setFlag( Flag flag )
    {
    	if( this.flag != null && flag != null && ! this.flag.equals( flag ) )
    	{
    		logger.warn( "Replacing flag {} with flag {} at boardElement {}", this.flag, flag, this );
    		throw new UnsupportedOperationException( "Cannot replace one flag with another at a boardElement" ); 
    	}
    	
    	this.flag = flag;
    	flag.setLocation( this );
    	
    }
    
    /**
     * Mimics firing the laser(s) on this boardelement, and finds the bot(s) getting hit by the laser(s).
     * Note, returns null if no laser on this boardelement
     * 
     * @return The bot(s) getting hit by laser(s) starting on this element, with the strength hit with
     */
    public Map<Bot, Integer> findLaserTargets()
    {
    	if( lasers.isEmpty() )
    	{
    		return null;
    	}
    	
    	if( bot == null || bot.getDiedThisTurn() )
    	{
    		Map<Bot, Integer> results = new HashMap<Bot, Integer>();
    		for( Laser laser : lasers )
    		{
    			Bot target = laser.findLaserTarget();
    			if( target != null )
    			{
    				results.put( target, laser.getStrength() );
    			}
    		}
    		return results;
    	}
    	else
    	{
    		return Collections.singletonMap( bot, lasers.stream().mapToInt( Laser::getStrength ).sum() );
    	}
    }    

    /**
     * Follows the laser coming from the given direction, and finds the bot getting hit by this laser.
     * 
     * @return The bot getting hit by the given laser (through its originating direction)
     */
    public Bot findLaserTarget( Direction originating )
    {
    	Optional<Laser> result = lasers.stream().filter( laser -> laser.getShotDir().equals( originating ) ).findAny();
    	return result.isPresent() ? result.get().findLaserTarget() : null;
    }
    
    /**
     * Logic that draws this boardElement 
     * 
     * @param g              The graphics object to use
     * @param factor         The magnification factor to use
     */
    public void paint(Graphics g, int factor) 
    {
        size = baseSize * factor;
        int baseX = xCoord * size;
        int baseY = ( yCoord + 1 ) * size;
        g.setColor( Color.black );
        g.drawRect( baseX, baseY, size - 1, size - 1 );
        g.setColor( Color.lightGray );
        g.fillRect( baseX + 1, baseY + 1, size - 2, size - 2 );
        try
        {
        	paintElement( g, baseX, baseY, factor );
        }
        catch( Throwable t )
        {
        	logger.error( "Something went wrong drawing boardelement at coordinates {}, {}", xCoord, yCoord, t);
        }
        g.setColor( Color.yellow );
        // corners are to be filled by north or south walls
        if ( walls.contains( Direction.NORTH ) ) 
        {
            g.fillRect( baseX, baseY, size, factor );
        }
        if ( walls.contains( Direction.SOUTH ) ) 
        {
            g.fillRect( baseX, baseY + size - factor, size, factor );
        }
        if ( walls.contains( Direction.WEST ) ) 
        {
            g.fillRect( baseX, baseY, factor, size );
        }
        if ( walls.contains( Direction.EAST ) ) 
        {
            g.fillRect( baseX + size - factor, baseY, factor, size );
        }
        g.setColor( Color.red );
        
        drawLaserMount( g, baseX, baseY, factor );

        drawLaserShots( g, baseX, baseY, factor );
    }

    /**
     * This method is responsible for painting the special features of a boardElement
     * 
     * @param g
     * @param baseX
     * @param baseY
     * @param factor
     */
    protected abstract void paintElement(Graphics g, int baseX, int baseY, int factor);

    /**
     * Logic that draws the red triangle that represents the laserMount for all available lasers
     * Can be called without a laserMount existing on any of the available lasers, does nothing then.
     * 
     * @param g              The graphics object to use
     * @param baseX          The starting x coordinate to draw from (in pixels)
     * @param baseY          The starting y coordinate to draw from (in pixels)
     * @param factor         The magnification factor to use
     */
    private void drawLaserMount( Graphics g, int baseX, int baseY, int factor )
    {
    	for( Laser laser :  lasers.stream().filter( l -> l.getMountDir() != null ).collect( Collectors.toList() ) )
    	{
	        for ( int i = 1; i < laser.getStrength() + 1; i++ )
	        {
	            int startAngle = 0;
	            int mountBaseX = 0;
	            int mountBaseY = 0;
	            int laserStartX = 0;
	            int laserStartY = 0;
	            int laserEndX = 0;
	            int laserEndY = 0;
	
	            // compensation for having to draw multiple laser lines
	            double compensation = ( i + 0.0 ) / ( laser.getStrength() + 1 );
	
	            // calculate the numbers based on the direction
		        switch (laser.getMountDir()) {
		            case NORTH:
		                startAngle = 180;
		                mountBaseX = Double.valueOf(size * compensation).intValue() - (3 * factor);
		                mountBaseY = -2 * factor;
		                laserStartX = baseX + Double.valueOf(size * compensation).intValue();
		                laserStartY = baseY + factor;
		                laserEndX = laserStartX;
		                laserEndY = baseY + size - 2;
		                if (!walls.contains(Direction.SOUTH)) {
		                    laserEndY+=factor;
		                }
		                break;
		            case SOUTH:
		                startAngle = 0;
		                mountBaseX = Double.valueOf(size * compensation).intValue() - (3 * factor);
		                mountBaseY = size - 4 * factor;
		                laserStartX = baseX + Double.valueOf(size * compensation).intValue();
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
		                mountBaseY = Double.valueOf(size * compensation).intValue() - (3 * factor);
		                laserStartX = baseX + 1;
		                laserStartY = baseY + Double.valueOf(size * compensation).intValue();
		                laserEndX = baseX + size - 1 - factor;
		                laserEndY = laserStartY;
		                if (!walls.contains(Direction.WEST)) {
		                    laserStartX-=factor;
		                }
		                break;
		            case WEST:
		                startAngle = 270;
		                mountBaseX = (-2 * factor);
		                mountBaseY = Double.valueOf(size * compensation).intValue() - 3 * factor;
		                laserStartX = baseX + factor;
		                laserStartY = baseY + Double.valueOf(size * compensation).intValue();
		                laserEndX = baseX + size - 1 - factor;
		                laserEndY = laserStartY;
		                if (!walls.contains(Direction.EAST)) {
		                    laserEndX+=factor;
		                }
		                break;
		            default:
		                return;
		        }
	
		        // Draw it
		        g.fillArc(baseX + mountBaseX, baseY + mountBaseY, factor * 6, factor * 6, startAngle, 180);
	            g.drawLine(laserStartX, laserStartY, laserEndX, laserEndY);
	        }
    	}
    }

    /**
     * Logic that draws the red line(s) that represents a laser, for all lasers available
     * 
     * @param g              The graphics object to use
     * @param baseX          The starting x coordinate to draw from (in pixels)
     * @param baseY          The starting y coordinate to draw from (in pixels)
     * @param factor         The magnification factor to use
     */
    private void drawLaserShots( Graphics g, int baseX, int baseY, int factor )
    {
    	for( Laser laser :  lasers.stream().filter( l -> l.getShotDir() != null ).collect( Collectors.toList() ) )
    	{
	        for ( int i = 1; i < laser.getStrength() + 1; i++ )
	        {
	            // drawLaserShots(g, baseX, baseY, factor, dir, (i+0.0)/(laserShot.get(dir)+1));
	        	
		        int laserStartX = 0;
		        int laserStartY = 0;
		        int laserEndX = 0;
		        int laserEndY = 0;
		        
		        // compensation for having to draw multiple laser lines
		        double compensation = ( i + 0.0 ) / ( laser.getStrength() + 1 );
		        
		        // calculate the numbers based on the direction
		        if ( laser.getShotDir().equals( Direction.NORTH ) || laser.getShotDir().equals( Direction.SOUTH ) ) 
		        {
		            laserStartX = baseX + Double.valueOf(size * compensation).intValue();
		            laserStartY = baseY + 1;
		            laserEndX = laserStartX;
		            laserEndY = baseY + size - 2;
		            if (!walls.contains(Direction.SOUTH)) {
		                laserEndY+=factor;
		            }
		            if (!walls.contains(Direction.NORTH)) {
		                laserStartY-=factor;
		            }
		        }
		        if ( laser.getShotDir().equals( Direction.EAST ) || laser.getShotDir().equals( Direction.WEST ) ) 
		        {
		            laserStartX = baseX + 1;
		            laserStartY = baseY + Double.valueOf(size * compensation).intValue();
		            laserEndX = baseX + size - 2;
		            laserEndY = laserStartY;
		            if (!walls.contains(Direction.EAST)) {
		                laserEndX+=factor;
		            }
		            if (!walls.contains(Direction.WEST)) {
		                laserStartX-=factor;
		            }
		        }
		        // draw it
	            g.drawLine(laserStartX, laserStartY, laserEndX, laserEndY);
	        }
    	}
    }
    
    /**
     * Model the laser that can exist on a boardelement
     * 
     * @author Kharybdys
     *
     */
    private class Laser
    {
    	// Direction the laser comes from. If preceding is null, this means the laser mount is in this direction.
    	private Direction originating;
    	private BoardElement precedingBoardElement = null;
    	private BoardElement followupBoardElement = null;
    	private int strength = 0;
    	
    	/**
    	 * Initialize a new laser
    	 * 
    	 * @param strength    The number of damage this laser does
    	 * @param originating The direction from which the laser beam originates (if preceding is null, this is the location of the laser mount)
    	 * @param preceding   The boardelement this laser beam originates from (can be null)
    	 * @param followup    The boardelement this laser beam continues into (can be null)
    	 */
    	private Laser( int strength, Direction originating, BoardElement preceding, BoardElement followup )
    	{
    		this.strength = strength;
    		this.originating = originating;
    		this.precedingBoardElement = preceding;
    		this.followupBoardElement = followup;
    	}

		/**
		 * Gets the direction where the laser mount is (for drawing), null if there is no laser mount
		 * 
		 * @return The direction where the laser mount is
		 */
    	private Direction getMountDir()
		{
			return precedingBoardElement == null ? originating : null;
		}
		
		/**
		 * Gets the direction where the laser is shot from (for drawing), null if there is a laser mount
		 * 
		 * @return The direction where the laser is shot from
		 */
    	public Object getShotDir() {
			return precedingBoardElement == null ? null : originating;
		}

    	/**
    	 * Gets the strength of the laser
    	 * 
    	 * @return The strength of the laser
    	 */
		private int getStrength()
		{
			return strength;
		}

		/**
		 * Find the bot being hit by this laser. If this is being called, it is not on this boardelement so ask our followup boardelement
		 * 
		 * @return The bot being hit by this laser
		 */
		private Bot findLaserTarget() {
			return followupBoardElement == null ? null : followupBoardElement.findLaserTarget( originating );
		}

    }
}