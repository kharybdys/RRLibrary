package kharybdys.roborally.game.board;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.Bot;
import kharybdys.roborally.game.Flag;
import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.definition.Direction;

/**
 *	Encapsulates all helper methods to create a Game object
 */
public class GameBuilder {

	private static final Logger logger = LoggerFactory.getLogger( GameBuilder.class );

	private static final int STANDARD_BOARD_SIZE = 12;

    // worker variables
    private int xOffset;
    private int yOffset;
    private int turnSteps;
    
    // the board being built
    private BoardElement[][] board;
    
    private Map<Integer, AffineTransform> affineTransformations = new HashMap<Integer, AffineTransform>();
    
    // These represent the items to transfer to the Game object
    private int xSize;
    private int ySize;
    private Collection<Bot> bots = new ArrayList<Bot>();
    private Collection<Flag> flags = new ArrayList<Flag>();
    private Map<BoardElement, Map<Direction, Integer>> laserMounts = new HashMap<BoardElement, Map<Direction, Integer>>();


    /**
     * Transforms the data contained in this GameBuilder into a Game object
     * 
     * @return a game object
     */
	public Game asGame() 
	{
		runConsistencyChecks();
		return new Game( xSize, ySize, board[0][0], bots, flags, getLaserMounts() );
	}

	/**
	 * Runs consistency checks on the just generated board
	 */
	private void runConsistencyChecks() 
	{
		boolean consistent = true;
		// check that all neighbour connections are transitive, and obey the relative coordinates
        for ( int i = 0; i < xSize; i++ ) 
        {
            for ( int j = 0; j < ySize; j++ ) 
            {
            	BoardElement currentElement = board[j][i];
            	if( currentElement.getNeighbour( Direction.SOUTH ) != null )
            	{
            		BoardElement neighbour = currentElement.getNeighbour( Direction.SOUTH );
            		if( ! currentElement.equals( neighbour.getNeighbour( Direction.NORTH ) ) )
            		{
            			consistent = false;
            			logger.error( "Problem with transitivity of the SOUTH neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            		if( neighbour.getXCoordinate() != i || neighbour.getYCoordinate() != j + 1 )
            		{
            			consistent = false;
            			logger.error( "Problem with mis-matching coordinates of the SOUTH neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            	}

            	if( currentElement.getNeighbour( Direction.EAST ) != null )
            	{
            		BoardElement neighbour = currentElement.getNeighbour( Direction.EAST );
            		if( ! currentElement.equals( neighbour.getNeighbour( Direction.WEST ) ) )
            		{
            			consistent = false;
            			logger.error( "Problem with transitivity of the EAST neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            		if( neighbour.getXCoordinate() != i + 1 || neighbour.getYCoordinate() != j )
            		{
            			consistent = false;
            			logger.error( "Problem with mis-matching coordinates of the EAST neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            	}

            	if( currentElement.getNeighbour( Direction.NORTH ) != null )
            	{
            		BoardElement neighbour = currentElement.getNeighbour( Direction.NORTH );
            		if( ! currentElement.equals( neighbour.getNeighbour( Direction.SOUTH ) ) )
            		{
            			consistent = false;
            			logger.error( "Problem with transitivity of the NORTH neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            		if( neighbour.getXCoordinate() != i || neighbour.getYCoordinate() != j - 1 )
            		{
            			consistent = false;
            			logger.error( "Problem with mis-matching coordinates of the NORTH neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            	}

            	if( currentElement.getNeighbour( Direction.WEST ) != null )
            	{
            		BoardElement neighbour = currentElement.getNeighbour( Direction.WEST );
            		if( ! currentElement.equals( neighbour.getNeighbour( Direction.EAST ) ) )
            		{
            			consistent = false;
            			logger.error( "Problem with transitivity of the WEST neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            		if( neighbour.getXCoordinate() != i - 1 || neighbour.getYCoordinate() != j )
            		{
            			consistent = false;
            			logger.error( "Problem with mis-matching coordinates of the WEST neighbour link of coordinates ( " + j + ", " + i + " )" );
            		}
            	}
            }
        }
		
	}

	/**
     * Helper method to calculate the real coordinates based on the board's coordinates, offset and a number of turns to make.
     * TODO: Cannot handle starterboard turning as it assumes the board to be 12x12
     * 
     * @param point
     * @param xOffset
     * @param yOffset
     * @param turnSteps
     * @return
     */
    private Point2D getRealCoordinates( Point2D point, int xOffset, int yOffset, int turnSteps )
    {
    	AffineTransform transformation = getAffineTransformation( turnSteps );
        Point2D result = transformation.transform( point, new Point( 0, 0 ) );
        result.setLocation( result.getX() + xOffset, result.getY() + yOffset );
    	return result;
    }
    
    
    private AffineTransform getAffineTransformation( int turnSteps )
    {
    	if( ! affineTransformations.containsKey( turnSteps ) )
    	{
    		affineTransformations.put( turnSteps, 
    				                   AffineTransform.getQuadrantRotateInstance( turnSteps, 
    		                                                                      ( STANDARD_BOARD_SIZE - 1 ) / 2.0, 
    		                                                                      ( STANDARD_BOARD_SIZE - 1 ) / 2.0
    		                                                                    )
    				                 ); 
    	}
    	return affineTransformations.get( turnSteps );
    }
    
    /**
     * Initializes the board array given the sizes. Also links every element to each of its neighbours
     * 
     * @param xSize The number of elements on the x-Scale
     * @param ySize The number of elements on the y-Scale
     */
    public void initializeBoard( int xSize, int ySize )
    {
    	this.xSize = xSize;
    	this.ySize = ySize;
    	
        this.board = new BoardElement[ySize][xSize];
        for ( int i = 0; i < xSize; i++ ) 
        {
            for ( int j = 0; j < ySize; j++ ) 
            {
                board[j][i] = new BasicElement().withCoordinates( i, j );
                if( j > 0 )
                {
                	board[j][i].addNeighbour( Direction.NORTH, board[j-1][i] );
                }
                if( i > 0 )
                {
                	board[j][i].addNeighbour( Direction.WEST, board[j][i-1] );
                }
            }
        }
    }
    
    public void setBoardVariables( int xOffset, int yOffset, int turnSteps )
    {
    	this.xOffset = xOffset;
    	this.yOffset = yOffset;
    	this.turnSteps = turnSteps;
    }
    
    public void addFlag( int xCoordinate, int yCoordinate, int number )
    {
    	Flag flag = new Flag( number );
    	this.board[yCoordinate][xCoordinate].setFlag( flag );
    	flag.setArchiveMarker( board[yCoordinate][xCoordinate] );
    	
    	flags.add( flag );
    	
    }
    
    /**
     * Adds a marker for a laser mount
     * 
     * @param element        The board element holding the laser mount
     * @param mountDirection The direction in which the laser mounts
     * @param strength       The strength of this laser
     */
    public void addLaserMount( BoardElement element, Direction mountDirection, int strength )
    {
    	Direction effectiveMountDirection = mountDirection.processRotate( turnSteps );
    	
    	if( ! laserMounts.containsKey( element ) )
    	{
    		laserMounts.put( element, new EnumMap<Direction, Integer>( Direction.class ) );
    	}
    	Map<Direction, Integer> mount = laserMounts.get( element );
    	if( ! mount.containsKey( effectiveMountDirection ) )
    	{
    		mount.put( effectiveMountDirection, strength );
    	}
    	else
    	{	// Shouldn't happen
    		logger.warn( "Adding laser mount to already existing laser mount!" );
    		mount.put( effectiveMountDirection, mount.get( effectiveMountDirection ) + strength );
    	}
    }
    
    /**
     * Processes the collection of laserMounts, adding the lasers to the boardElements.
     * 
     * @return the collection of boardElements containing laser mounts
     */
    public Collection<BoardElement> getLaserMounts()
    {
    	for( BoardElement element : laserMounts.keySet() )
    	{
    		for( Entry<Direction, Integer> laser :  laserMounts.get( element ).entrySet() )
    		{
        		element.addLaser( laser.getValue(), laser.getKey(), null );
    		}
    	}
    	
    	return laserMounts.keySet();
    }
    
    /**
     * Add the constructed element to the board in the given position.
     * Also links it to its neighbours
     * 
     * @param element
     * @param coordinates
     * @param xOffset
     * @param yOffset
     * @param turnSteps
     */
    public void addElement( AbstractBoardElement element, Point2D coordinates )
    {
    	Point2D realCoordinates = getRealCoordinates( coordinates, xOffset, yOffset, turnSteps );
    	int xCoordinate = (int) realCoordinates.getX();
    	int yCoordinate = (int) realCoordinates.getY();
    	element.withCoordinates( xCoordinate, yCoordinate );
    	this.board[yCoordinate][xCoordinate] = element;
    	if( xCoordinate > 0 )
    	{
    		element.addNeighbour( Direction.WEST, this.board[yCoordinate][xCoordinate - 1] );
    	}
    	if( yCoordinate > 0 )
    	{
    		element.addNeighbour( Direction.NORTH, this.board[yCoordinate - 1][xCoordinate] );
    	}
    	if( xCoordinate < xSize - 1 )
    	{
    		element.addNeighbour( Direction.EAST, this.board[yCoordinate][xCoordinate + 1] );
    	}
    	if( yCoordinate < ySize - 1 )
    	{
    		element.addNeighbour( Direction.SOUTH, this.board[yCoordinate + 1][xCoordinate] );
    	}
    }
    
    public AbstractBoardElement createBasicElement( Collection<Direction> walls, BasicElementType type )
    {
    	return new BasicElement().withBasicElementType( type ).withWalls( Direction.turnCollection( walls, turnSteps ) );
    }
    
    public AbstractBoardElement createBasicElement( BasicElementType type )
    {
    	return new BasicElement().withBasicElementType( type );
    }
    
    public AbstractBoardElement createBasicElement( Collection<Direction> walls )
    {
    	return new BasicElement().withWalls( Direction.turnCollection( walls, turnSteps ) );
    }
    
    public AbstractBoardElement createBasicElement()
    {
    	return new BasicElement();
    }
    
    public AbstractBoardElement createDualSpeedConveyor( Collection<Direction> walls, Direction endDirection, Collection<Direction> startingDirections )
    {
    	return new DualSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
    			                      .withEndDirection( endDirection.processRotate( turnSteps ) )
    			                      .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    public AbstractBoardElement createDualSpeedConveyor( Direction endDirection, Collection<Direction> startingDirections )
    {
    	return new DualSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
    			                      .withEndDirection( endDirection.processRotate( turnSteps ) );
    }

    public AbstractBoardElement createSingleSpeedConveyor( Collection<Direction> walls, Direction endDirection, Collection<Direction> startingDirections )
    {
    	return new SingleSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
    			                        .withEndDirection( endDirection.processRotate( turnSteps ) )
    			                        .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    public AbstractBoardElement createSingleSpeedConveyor( Direction endDirection, Collection<Direction> startingDirections )
    {
    	return new SingleSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
    			                        .withEndDirection( endDirection.processRotate( turnSteps ) );
    }

    public AbstractBoardElement createClockwiseRotator( Collection<Direction> walls )
    {
    	return new RotatorClockwise().withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    public AbstractBoardElement createClockwiseRotator()
    {
    	return new RotatorClockwise();
    }

    public AbstractBoardElement createCounterClockwiseRotator( Collection<Direction> walls )
    {
    	return new RotatorCounterClockwise().withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    public AbstractBoardElement createCounterClockwiseRotator()
    {
    	return new RotatorCounterClockwise();
    }
    
    public AbstractBoardElement create135Pusher( Collection<Direction> walls, Direction pusherDirection )
    {
    	return new Pusher135().withPusherDirection( pusherDirection.processRotate( turnSteps ) )
    			              .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    public AbstractBoardElement create24Pusher( Collection<Direction> walls, Direction pusherDirection )
    {
    	return new Pusher24().withPusherDirection( pusherDirection.processRotate( turnSteps ) )
    			             .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }
}
