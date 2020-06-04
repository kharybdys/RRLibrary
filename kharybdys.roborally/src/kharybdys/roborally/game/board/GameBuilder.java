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
 *    Encapsulates all helper methods to create a Game object
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
    private Map<Integer, Flag> flags = new HashMap<Integer, Flag>();
    private Map<BoardElement, Map<Direction, Integer>> laserMounts = new HashMap<BoardElement, Map<Direction, Integer>>();

	private Map<Integer, BoardElement> startingPositions = new HashMap<Integer, BoardElement>();


    /**
     * Transforms the data contained in this GameBuilder into a Game object
     * Id and currentRound need to be provided (comes from the DB)
     * 
     * @return a game object
     */
    public Game asGame( Integer id, Integer currentRound ) 
    {
        runConsistencyChecks();
        return new Game( id, currentRound, xSize, ySize, board[0][0], bots, flags.values(), getLaserMounts() );
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
        if( !consistent )
        {
            throw new IllegalStateException( "GameBuilder built an inconsistent board, check logger messages" );
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
        Point2D result = transformation.transform( point,     new Point( 0, 0 ) );
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
                board[j][i] = createBasicElement().withCoordinates( i, j );
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
        Flag flag = new Flag( -1, number );
        this.board[yCoordinate][xCoordinate].setFlag( flag );
        flag.setArchiveMarker( board[yCoordinate][xCoordinate] );
        
        flags.put( number, flag );
        
    }
    
    /**
     * Adds a flag to this game. 
     * Might replace an already added flag (done on orderNumber), keeping that flag's coordinates then
     * 
     * @param flagId        the id of the Flag in the DB
     * @param xCoord        the x-Coordinate of the location of the flag
     * @param yCoord        the y-Coordinate of the location of the flag
     * @param archiveXCoord the x-Coordinate of the archive marker of the flag
     * @param archiveYCoord the y-Coordinate of the archive marker of the flag
     * @param orderNumber   the orderNumber of the flag
     */
	public void addFlag( Integer flagId, 
			             Integer xCoord, Integer yCoord, 
			             Integer archiveXCoord, Integer archiveYCoord,
			             Integer orderNumber) 
	{
		Flag flag = new Flag( flagId, orderNumber );
		BoardElement location = null;
		BoardElement archiveMarker = null;
		if( flags.containsKey( orderNumber ) )
		{
			// ignore the given coordinates and take the one from the map
			location = flags.get( orderNumber ).getLocation();
			archiveMarker = flags.get( orderNumber ).getArchiveMarker();
			// remove the flag from the given location and from the map
			location.setFlag( null );
			flags.remove( orderNumber );
		}
		else
		{
			location = board[yCoord][xCoord];
			archiveMarker = board[archiveYCoord][archiveXCoord];
		}
		flag.setLocation( location );
		flag.setArchiveMarker( archiveMarker );
		flags.put( orderNumber, flag );
	}

	/**
     * Adds bots not linked to the DB
     * 
     * @param nrOfBots The amount of bots to add
     */
    public void addDummyBots( int nrOfBots ) 
    {
        for( int i = 1; i <= nrOfBots; i++ )
        {
        	addBot( -1, 0, 3, -1, -1, -1, -1, 0, i, Direction.NORTH);
        }
        
    }


	public void addBot( Integer botId, Integer damage, Integer lives, 
			            Integer xCoord, Integer yCoord,
			            Integer archiveXCoord, Integer archiveYCoord, 
			            Integer latestFlag, Integer orderNumber,
			            Direction facingDirection) 
	{
		BoardElement location = null;
		BoardElement archiveMarker = null;
		if( xCoord == -1 || yCoord == -1 || archiveXCoord == -1 || archiveYCoord == -1 )
		{
			// Bot hasn't been initialized yet, let's do so by finding the starting position equal to the bot's orderNumber.
			location = startingPositions.get(orderNumber);
			archiveMarker = location;
		}
		else
		{
			location = board[yCoord][xCoord];
			archiveMarker = board[archiveYCoord][archiveXCoord];
		}
		Bot bot = new Bot( botId, damage, lives, latestFlag, orderNumber, facingDirection );
		location.setBot( bot );
		bot.setArchiveMarker( archiveMarker );
		bots.add( bot );
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
        {    // Shouldn't happen
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
        
        // if this element is a starting position, add it to the map of starting positions
        if( element.getBoardElementType().isStartingPosition() )
        {
        	startingPositions.put( element.getBoardElementType().getNumber(), element );
        }
    }
    
    private AbstractBoardElement createBasicElement( Collection<Direction> walls, BasicElementType type )
    {
        return new BasicElement().withBasicElementType( type ).withWalls( Direction.turnCollection( walls, turnSteps ) );
    }
    
    private AbstractBoardElement createBasicElement( BasicElementType type )
    {
        return new BasicElement().withBasicElementType( type );
    }
    
    private AbstractBoardElement createBasicElement( Collection<Direction> walls )
    {
        return new BasicElement().withWalls( Direction.turnCollection( walls, turnSteps ) );
    }
    
    private AbstractBoardElement createBasicElement()
    {
        return new BasicElement();
    }
    
    private AbstractBoardElement createDualSpeedConveyor( Collection<Direction> walls, Direction endDirection, Collection<Direction> startingDirections )
    {
        return new DualSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
                                      .withEndDirection( endDirection.processRotate( turnSteps ) )
                                      .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    private AbstractBoardElement createDualSpeedConveyor( Direction endDirection, Collection<Direction> startingDirections )
    {
        return new DualSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
                                      .withEndDirection( endDirection.processRotate( turnSteps ) );
    }

    private AbstractBoardElement createSingleSpeedConveyor( Collection<Direction> walls, Direction endDirection, Collection<Direction> startingDirections )
    {
        return new SingleSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
                                        .withEndDirection( endDirection.processRotate( turnSteps ) )
                                        .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    private AbstractBoardElement createSingleSpeedConveyor( Direction endDirection, Collection<Direction> startingDirections )
    {
        return new SingleSpeedConveyor().withStartingDirections( Direction.turnCollection( startingDirections, turnSteps ) )
                                        .withEndDirection( endDirection.processRotate( turnSteps ) );
    }

    private AbstractBoardElement createClockwiseRotator( Collection<Direction> walls )
    {
        return new RotatorClockwise().withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    private AbstractBoardElement createClockwiseRotator()
    {
        return new RotatorClockwise();
    }

    private AbstractBoardElement createCounterClockwiseRotator( Collection<Direction> walls )
    {
        return new RotatorCounterClockwise().withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    private AbstractBoardElement createCounterClockwiseRotator()
    {
        return new RotatorCounterClockwise();
    }
    
    private AbstractBoardElement create135Pusher( Collection<Direction> walls, Direction pusherDirection )
    {
        return new Pusher135().withPusherDirection( pusherDirection.processRotate( turnSteps ) )
                              .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    private AbstractBoardElement create24Pusher( Collection<Direction> walls, Direction pusherDirection )
    {
        return new Pusher24().withPusherDirection( pusherDirection.processRotate( turnSteps ) )
                             .withWalls( Direction.turnCollection( walls, turnSteps ) );
    }

    /**
     * Adds the elements for the first starting board to the builder, given the x and yOffsets
     * TODO: Support turning?
     * 
     * @param xOffset The x-Offset
     * @param yOffset The y-Offset
     *  
     */
    public void addFirstStartingBoard( int xOffset, int yOffset ) 
    {
        setBoardVariables( xOffset, yOffset, 0 );

        // row 0
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 2, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 4, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 7, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 0 ) );
        //row 2
        addElement( createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_7 ), 
                    new Point( 0, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_5 ), 
                    new Point( 1, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 2, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_3 ), 
                    new Point( 3, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 4, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_1 ), 
                    new Point( 5, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_2 ), 
                    new Point( 6, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 7, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_4 ), 
                    new Point( 8, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 9, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_6 ), 
                    new Point( 10, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_8 ), 
                    new Point( 11, 2 ) );

       // row 3
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 3 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 4, 3 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 7, 3 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 9, 3 ) );
    }

    /**
     * Adds the elements for the second starting board to the builder, given the x and yOffsets
     * TODO: Support turning?
     * 
     * @param xOffset The x-Offset
     * @param yOffset The y-Offset
     */
    public void addSecondStartingBoard( int xOffset, int yOffset ) 
    {
        setBoardVariables( xOffset, yOffset, 0 );
    
        // row 0
        addElement( createBasicElement( BasicElementType.STARTING_7 ), 
                    new Point( 0, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 2, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 3, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH, Direction.WEST ) ), 
                    new Point( 4, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH, Direction.EAST ) ), 
                    new Point( 7, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 8, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 0 ) );
        addElement( createBasicElement( BasicElementType.STARTING_8 ), 
                    new Point( 11, 0 ) );
    
        // row 1
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 0, 1 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_5 ), 
                    new Point( 1, 1 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 2, 1 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 9, 1 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_6 ), 
                    new Point( 10, 1 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 11, 1 ) );
    
        // row 2
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 0, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 1, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                    new Point( 2, 2 ) );
        addElement( createBasicElement( BasicElementType.STARTING_3 ), 
                    new Point( 3, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 5, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 6, 2 ) );
        addElement( createBasicElement( BasicElementType.STARTING_4 ), 
                    new Point( 8, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.EAST ) ), 
                    new Point( 9, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 10, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 11, 2 ) );
    
        // row 3
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.NORTH ) ), 
                    new Point( 2, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 3, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 4, 3 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_1 ), 
                    new Point( 5, 3 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_2 ), 
                    new Point( 6, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 7, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 8, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 3 ) );
    }

    /**
     * Adds the elements for the Maelstrom board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addMaelstrom( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
        addElement( create24Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                    new Point( 2, 0 ) );
        addElement( createBasicElement( BasicElementType.OPTION ), 
                    new Point( 3, 0 ) );
        addElement( create135Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                    new Point( 4, 0 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 0 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 0 ) );
        addElement( create135Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                    new Point( 7, 0 ) );
        addElement( create24Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                    new Point( 9, 0 ) );
        addElement( createBasicElement( BasicElementType.REPAIR ), 
                    new Point( 11, 0 ) );
        
        // row 1
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.NORTH, Direction.SOUTH ) ), 
                    new Point( 1, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 2, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 3, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 4, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST, Direction.NORTH ) ), 
                    new Point( 5, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 6, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 7, 1 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 8, 1 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                    new Point( 9, 1 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.EAST ) ), 
                    new Point( 10, 1 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 11, 1 ) );
        
        // row 2
        addElement( create24Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                    new Point( 0, 2 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 3, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 4, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 5, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 6, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 7, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                    new Point( 8, 2 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 2 ) );
        addElement( create24Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                    new Point( 11, 2 ) );
    
        // row 3
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 3 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 3 ) );
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 4, 3 ) );
    
        temp = createDualSpeedConveyor( Direction.of( Direction.NORTH ), Direction.EAST, Direction.of( Direction.WEST ) );
        addElement( temp, 
                    new Point( 5, 3 ) );
        addLaserMount( temp, Direction.NORTH, 1 );
    
        addElement( createDualSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 6, 3 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                    new Point( 7, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 3 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 3 ) );
    
        // row 4
        addElement( create135Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                    new Point( 0, 4 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 4 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 4 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 4 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.SOUTH ) ), 
                    new Point( 4, 4 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 5, 4 ) );
    
        temp = createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.SOUTH, Direction.of( Direction.WEST ) );
        addElement( temp, 
                    new Point( 6, 4 ) );
        addLaserMount( temp, Direction.NORTH, 1 );
    
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 7, 4 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 4 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 4 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 4 ) );
        addElement( create135Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                    new Point( 11, 4 ) );
    
        // row 5
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 5 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 5 ) );
    
        temp = createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.NORTH, Direction.of( Direction.SOUTH ) );
        addElement( temp, 
                    new Point( 4, 5 ) );
        addLaserMount( temp, Direction.WEST, 1 );
    
        addElement( createBasicElement( BasicElementType.HOLE ), 
                    new Point( 5, 5 ) );
        addElement( createBasicElement( BasicElementType.HOLE ), 
                    new Point( 6, 5 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 7, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.EAST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 5 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH, Direction.EAST ) ), 
                    new Point( 10, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 11, 5 ) );
    
        // row 6
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 0, 6 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.WEST, Direction.SOUTH ) ), 
                    new Point( 1, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 6 ) );
        
        temp = createDualSpeedConveyor( Direction.of( Direction.WEST ), Direction.NORTH, Direction.of( Direction.SOUTH ) );
        addElement( temp, 
                    new Point( 3, 6 ) );
        addLaserMount( temp, Direction.WEST, 1 );
        
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 4, 6 ) );
        addElement( createBasicElement( BasicElementType.HOLE ), 
                    new Point( 5, 6 ) );
        addElement( createBasicElement( BasicElementType.HOLE ), 
                    new Point( 6, 6 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.EAST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 7, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 6 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 11, 6 ) );
    
        // row 7
        addElement( create135Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                    new Point( 0, 7 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 7 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 7 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 7 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 4, 7 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.NORTH, Direction.of( Direction.EAST ) ), 
                    new Point( 5, 7 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 6, 7 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                    new Point( 7, 7 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 7 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 7 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 7 ) );
        addElement( create135Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                    new Point( 11, 7 ) );
    
        // row 8
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 8 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.EAST ) ), 
                    new Point( 4, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 5, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 6, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 7, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 8 ) );
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 8 ) );
    
        // row 9
        addElement( create24Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                    new Point( 0, 9 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 9 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 9 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.EAST ) ), 
                    new Point( 3, 9 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 4, 9 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 5, 9 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 6, 9 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 7, 9 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 8, 9 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 9 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 9 ) );
        addElement( create24Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                    new Point( 11, 9 ) );
        
        // row 10
        addElement( createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 0, 10 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.WEST ) ), 
                    new Point( 1, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.EAST ) ), 
                    new Point( 2, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 3, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 4, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 5, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST, Direction.SOUTH ) ), 
                    new Point( 6, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 7, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 8, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 9, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH, Direction.SOUTH ) ), 
                    new Point( 10, 10 ) );
    
        // row 11
        addElement( createBasicElement( BasicElementType.REPAIR ), 
                    new Point( 0, 11 ) );
        addElement( create24Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                    new Point( 2, 11 ) );
        addElement( create135Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                    new Point( 4, 11 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 11 ) );
        addElement( create135Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                    new Point( 7, 11 ) );
        addElement( createBasicElement( BasicElementType.OPTION ), 
                    new Point( 8, 11 ) );
        addElement( create24Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                    new Point( 9, 11 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 10, 11 ) );
    }

    /**
     * Adds the elements for the SpinZone board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addSpinZone( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        /*
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
    
        board[2][0]   = new BasicElement(            2,  0,  getSouthDirs()       );
        board[4][0]   = new BasicElement(            4,  0,  getSouthDirs()       );
        board[7][0]   = new BasicElement(            7,  0,  getSouthDirs()       );
        board[9][0]   = new BasicElement(            9,  0,  getSouthDirs()       );
        board[1][1]   = new DualSpeedConveyor(       1,  1,  null,                getEastDirs(), Direction.NORTH);
        board[2][1]   = new DualSpeedConveyor(       2,  1,  null,                getEastDirs(), Direction.WEST);
        board[3][1]   = new DualSpeedConveyor(       3,  1,  null,                getEastDirs(), Direction.WEST);
        board[4][1]   = new DualSpeedConveyor(       4,  1,  null,                getNorthDirs(), Direction.WEST);
        board[7][1]   = new DualSpeedConveyor(       7,  1,  null,                getEastDirs(), Direction.NORTH);
        board[8][1]   = new DualSpeedConveyor(       8,  1,  null,                getEastDirs(), Direction.WEST);
        board[9][1]   = new DualSpeedConveyor(       9,  1,  null,                getEastDirs(), Direction.WEST);
        board[10][1]  = new DualSpeedConveyor(       10, 1,  null,                getNorthDirs(), Direction.WEST);
        board[0][2]   = new BasicElement(            0,  2,  getWestDirs()        );
        board[1][2]   = new DualSpeedConveyor(       1,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[3][2]   = new RotatorClockwise(        3,  2,  null                 );
        board[4][2]   = new DualSpeedConveyor(       4,  2,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][2]   = new RotatorCounterClockwise( 6,  2,  null                 );
        board[7][2]   = new DualSpeedConveyor(       7,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[8][2]   = new BasicElement(            8,  2,  getNorthDirs()       );
        board[9][2]   = new RotatorClockwise(        9,  2,  null                 );
        board[10][2]  = new DualSpeedConveyor(       10, 2,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][2]  = new BasicElement(            11, 2,  getEastDirs()        );
        board[1][3]   = new DualSpeedConveyor(       1,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[2][3]   = new RotatorClockwise(        2,  3,  null                 );
        board[3][3]   = new BasicElement(            3,  3,  null,                0, BasicElementType.OPTION);
        board[4][3]   = new DualSpeedConveyor(       4,  3,  getEastDirs(),       getNorthDirs(), Direction.SOUTH);
        board[5][3]   = new BasicElement(            5,  3,  getWestDirs()        ); // Laser mount west, strength 1
        board[6][3]   = new BasicElement(            6,  3,  getEastDirs()        );
        board[7][3]   = new DualSpeedConveyor(       7,  3,  getWestDirs(),       getSouthDirs(), Direction.NORTH);
        board[8][3]   = new RotatorClockwise(        8,  3,  getSouthDirs()       );
        board[9][3]   = new BasicElement(            9,  3,  null,                0, BasicElementType.REPAIR);
        board[10][3]  = new DualSpeedConveyor(       10, 3,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][4]   = new BasicElement(            0,  4,  getWestDirs()        );
        board[1][4]   = new DualSpeedConveyor(       1,  4,  null,                getSouthDirs(), Direction.EAST);
        board[2][4]   = new DualSpeedConveyor(       2,  4,  null,                getWestDirs(), Direction.EAST);
        board[3][4]   = new DualSpeedConveyor(       3,  4,  getNorthDirs(),      getWestDirs(), Direction.EAST);
        board[4][4]   = new DualSpeedConveyor(       4,  4,  null,                getWestDirs(), Direction.SOUTH);
        board[5][4]   = new RotatorCounterClockwise( 5,  4,  null                 );
        board[7][4]   = new DualSpeedConveyor(       7,  4,  null,                getSouthDirs(), Direction.EAST);
        board[8][4]   = new DualSpeedConveyor(       8,  4,  null,                getWestDirs(), Direction.EAST);
        board[9][4]   = new DualSpeedConveyor(       9,  4,  null,                getWestDirs(), Direction.EAST);
        board[10][4]  = new DualSpeedConveyor(       10, 4,  null,                getWestDirs(), Direction.SOUTH);
        board[11][4]  = new BasicElement(            11, 4,  getEastDirs()        );
        board[2][5]   = new RotatorCounterClockwise( 2,  5,  null                 );
        board[3][5]   = new BasicElement(            3,  5,  getSouthDirs()       ); // Laser mount south, strength 1
        board[7][5]   = new RotatorCounterClockwise( 7,  5,  null                 );
        board[4][6]   = new RotatorCounterClockwise( 4,  6,  null                 );
        board[8][6]   = new BasicElement(            8,  6,  getNorthDirs()       ); // Laser mount north, strength 1
        board[9][6]   = new RotatorCounterClockwise( 9,  6,  null                 );
        board[0][7]   = new BasicElement(            0,  7,  getWestDirs()        );
        board[1][7]   = new DualSpeedConveyor(       1,  7,  null,                getEastDirs(), Direction.NORTH);
        board[2][7]   = new DualSpeedConveyor(       2,  7,  null,                getEastDirs(), Direction.WEST);
        board[3][7]   = new DualSpeedConveyor(       3,  7,  null,                getEastDirs(), Direction.WEST);
        board[4][7]   = new DualSpeedConveyor(       4,  7,  null,                getNorthDirs(), Direction.WEST);
        board[6][7]   = new RotatorCounterClockwise( 6,  7,  null                 );
        board[7][7]   = new DualSpeedConveyor(       7,  7,  null,                getEastDirs(), Direction.NORTH);
        board[8][7]   = new DualSpeedConveyor(       8,  7,  getSouthDirs(),      getEastDirs(), Direction.WEST);
        board[9][7]   = new DualSpeedConveyor(       9,  7,  null,                getEastDirs(), Direction.WEST);
        board[10][7]  = new DualSpeedConveyor(       10, 7,  null,                getNorthDirs(), Direction.WEST);
        board[11][7]  = new BasicElement(            11, 7,  getEastDirs()        );
        board[1][8]   = new DualSpeedConveyor(       1,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[2][8]   = new BasicElement(            2,  8,  null,                0, BasicElementType.REPAIR);
        board[3][8]   = new RotatorClockwise(        3,  8,  getNorthDirs()       );
        board[4][8]   = new DualSpeedConveyor(       4,  8,  getEastDirs(),       getNorthDirs(), Direction.SOUTH);
        board[5][8]   = new BasicElement(            5,  8,  getWestDirs()        );
        board[6][8]   = new BasicElement(            6,  8,  getEastDirs()        ); // Laser mount east, strength 1
        board[7][8]   = new DualSpeedConveyor(       7,  8,  getWestDirs(),       getSouthDirs(), Direction.NORTH);
        board[8][8]   = new BasicElement(            8,  8,  null,                0, BasicElementType.OPTION);
        board[9][8]   = new RotatorClockwise(        9,  8,  null                 );
        board[10][8]  = new DualSpeedConveyor(       10, 8,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][9]   = new BasicElement(            0,  9,  getWestDirs()        );
        board[1][9]   = new DualSpeedConveyor(       1,  9,  null,                getSouthDirs(), Direction.NORTH);
        board[2][9]   = new RotatorClockwise(        2,  9,  null                 );
        board[3][9]   = new BasicElement(            3,  9,  getSouthDirs()       );
        board[4][9]   = new DualSpeedConveyor(       4,  9,  null,                getNorthDirs(), Direction.SOUTH);
        board[5][9]   = new RotatorCounterClockwise( 5,  9,  null                 );
        board[7][9]   = new DualSpeedConveyor(       7,  9,  null,                getSouthDirs(), Direction.NORTH);
        board[8][9]   = new RotatorClockwise(        8,  9,  null                 );
        board[10][9]  = new DualSpeedConveyor(       10, 9,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][9]  = new BasicElement(            11, 9,  getEastDirs()        );
        board[1][10]  = new DualSpeedConveyor(       1,  10, null,                getSouthDirs(), Direction.EAST);
        board[2][10]  = new DualSpeedConveyor(       2,  10, null,                getWestDirs(), Direction.EAST);
        board[3][10]  = new DualSpeedConveyor(       3,  10, null,                getWestDirs(), Direction.EAST);
        board[4][10]  = new DualSpeedConveyor(       4,  10, null,                getWestDirs(), Direction.SOUTH);
        board[7][10]  = new DualSpeedConveyor(       7,  10, null,                getSouthDirs(), Direction.EAST);
        board[8][10]  = new DualSpeedConveyor(       8,  10, null,                getWestDirs(), Direction.EAST);
        board[9][10]  = new DualSpeedConveyor(       9,  10, null,                getWestDirs(), Direction.EAST);
        board[10][10] = new DualSpeedConveyor(       10, 10, null,                getWestDirs(), Direction.SOUTH);
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        */
    }

    /**
     * Adds the elements for the Vault board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addVault( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        /*
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
    
        board[1][0]   = new SingleSpeedConveyor(     1,  0,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][0]   = new BasicElement(            2,  0,  getSouthDirs()       ); // Laser mount south, strength 1
        board[4][0]   = new BasicElement(            4,  0,  getSouthDirs()       );
        board[6][0]   = new SingleSpeedConveyor(     6,  0,  null,                getSouthDirs(), Direction.NORTH);
        board[7][0]   = new BasicElement(            7,  0,  getSouthDirs()       );
        board[9][0]   = new BasicElement(            9,  0,  getSouthDirs()       );
        board[11][0]  = new BasicElement(            11, 0,  null,                0, BasicElementType.REPAIR);
        board[0][1]   = new SingleSpeedConveyor(     0,  1,  null,                getWestDirs(), Direction.EAST);
        board[1][1]   = new SingleSpeedConveyor(     1,  1,  null,                getWestDirs(), Direction.SOUTH);
        board[6][1]   = new SingleSpeedConveyor(     6,  1,  null,                getSouthDirs(), Direction.EAST);
        board[7][1]   = new SingleSpeedConveyor(     7,  1,  null,                getWestDirs(), Direction.EAST);
        board[8][1]   = new SingleSpeedConveyor(     8,  1,  null,                getWestDirs(), Direction.EAST);
        board[9][1]   = new SingleSpeedConveyor(     9,  1,  null,                getWestDirs(), Direction.EAST);
        board[10][1]  = new SingleSpeedConveyor(     10, 1,  null,                getWestDirs(), Direction.NORTH);
        board[0][2]   = new BasicElement(            0,  2,  getWestDirs()        );
        board[3][2]   = new BasicElement(            3,  2,  null,                0, BasicElementType.HOLE);
        board[5][2]   = new BasicElement(            5,  2,  getNorthDirs()       );
        board[6][2]   = new Pusher135(               6,  2,  getNorthDirs(),      Direction.SOUTH);
        board[8][2]   = new BasicElement(            8,  2,  null,                0, BasicElementType.HOLE);
        board[10][2]  = new SingleSpeedConveyor(     10, 2,  null,                getSouthDirs(), Direction.NORTH);
        board[11][2]  = new BasicElement(            11, 2,  getEastDirs()        );
        board[0][3]   = new SingleSpeedConveyor(     0,  3,  null,                getEastDirs(), Direction.WEST);
        board[1][3]   = new RotatorClockwise(        1,  3,  null                 );
        board[2][3]   = new BasicElement(            2,  3,  getNorthDirs()       );
        board[4][3]   = new BasicElement(            4,  3,  getNorthDirs()       );
        board[5][3]   = new BasicElement(            5,  3,  getSouthDirs()       );
        board[6][3]   = new BasicElement(            6,  3,  getSouthDirs()       );
        board[7][3]   = new BasicElement(            7,  3,  getNorthDirs()       );
        board[10][3]  = new SingleSpeedConveyor(     10, 3,  null,                getSouthDirs(), Direction.NORTH);
        board[0][4]   = new BasicElement(            0,  4,  getWestDirs()        ); // Laser mount west, strength 1
        board[1][4]   = new SingleSpeedConveyor(     1,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][4]   = new BasicElement(            2,  4,  getSouthDirs()       );
        board[3][4]   = new BasicElement(            3,  4,  getEastDirs()        );
        board[4][4]   = new BasicElement(            4,  4,  getSouthWestDirs()   );
        board[7][4]   = new BasicElement(            7,  4,  getSouthEastDirs()   );
        board[8][4]   = new BasicElement(            8,  4,  getWestDirs()        );
        board[10][4]  = new SingleSpeedConveyor(     10, 4,  null,                getSouthDirs(), Direction.NORTH);
        board[11][4]  = new BasicElement(            11, 4,  getEastDirs()        ); // Laser mount east, strength 1
        board[1][5]   = new SingleSpeedConveyor(     1,  5,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][5]   = new Pusher135(               2,  5,  getEastDirs(),       Direction.WEST);
        board[3][5]   = new BasicElement(            3,  5,  getWestDirs()        );
        board[5][5]   = new BasicElement(            5,  5,  null,                0, BasicElementType.OPTION);
        board[6][5]   = new BasicElement(            6,  5,  null,                0, BasicElementType.OPTION);
        board[8][5]   = new BasicElement(            8,  5,  getEastDirs()        );
        board[9][5]   = new Pusher24(                9,  5,  getWestDirs(),       Direction.EAST);
        board[10][5]  = new SingleSpeedConveyor(     10, 5,  null,                getSouthDirs(), Direction.EAST);
        board[11][5]  = new SingleSpeedConveyor(     11, 5,  null,                getWestDirs(), Direction.EAST);
        board[1][6]   = new SingleSpeedConveyor(     1,  6,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][6]   = new BasicElement(            2,  6,  getEastDirs()        );
        board[3][6]   = new BasicElement(            3,  6,  getWestDirs()        );
        board[5][6]   = new BasicElement(            5,  6,  null,                0, BasicElementType.OPTION);
        board[6][6]   = new BasicElement(            6,  6,  null,                0, BasicElementType.OPTION);
        board[8][6]   = new BasicElement(            8,  6,  getEastDirs()        );
        board[9][6]   = new Pusher135(               9,  6,  getWestDirs(),       Direction.EAST);
        board[0][7]   = new BasicElement(            0,  7,  getWestDirs()        ); // Laser mount west, strength 1
        board[1][7]   = new SingleSpeedConveyor(     1,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[3][7]   = new BasicElement(            3,  7,  getEastDirs()        );
        board[4][7]   = new BasicElement(            4,  7,  getNorthWestDirs()   );
        board[7][7]   = new BasicElement(            7,  7,  getNorthEastDirs()   );
        board[8][7]   = new BasicElement(            8,  7,  getWestDirs()        );
        board[11][7]  = new BasicElement(            11, 7,  getEastDirs()        ); // Laser mount east, strength 1
        board[0][8]   = new SingleSpeedConveyor(     0,  8,  null,                getNorthWestDirs(), Direction.EAST);
        board[1][8]   = new SingleSpeedConveyor(     1,  8,  null,                getWestDirs(), Direction.SOUTH);
        board[4][8]   = new BasicElement(            4,  8,  getSouthDirs()       );
        board[5][8]   = new BasicElement(            5,  8,  getNorthDirs()       );
        board[6][8]   = new BasicElement(            6,  8,  getNorthDirs()       );
        board[7][8]   = new BasicElement(            7,  8,  getSouthDirs()       );
        board[10][8]  = new SingleSpeedConveyor(     10, 8,  null,                getSouthDirs(), Direction.NORTH);
        board[0][9]   = new SingleSpeedConveyor(     0,  9,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[3][9]   = new BasicElement(            3,  9,  null,                0, BasicElementType.HOLE);
        board[5][9]   = new BasicElement(            5,  9,  getSouthDirs()       );
        board[6][9]   = new Pusher135(               6,  9,  getSouthDirs(),      Direction.NORTH);
        board[8][9]   = new BasicElement(            8,  9,  null,                0, BasicElementType.HOLE);
        board[10][9]  = new SingleSpeedConveyor(     10, 9,  null,                getSouthDirs(), Direction.NORTH);
        board[11][9]  = new BasicElement(            11, 9,  getEastDirs()        );
        board[0][10]  = new RotatorClockwise(        0,  10, null                 );
        board[1][10]  = new DualSpeedConveyor(       1,  10, null,                getWestDirs(), Direction.EAST);
        board[2][10]  = new DualSpeedConveyor(       2,  10, null,                getWestDirs(), Direction.EAST);
        board[3][10]  = new DualSpeedConveyor(       3,  10, null,                getWestDirs(), Direction.NORTH);
        board[4][10]  = new BasicElement(            4,  10, getEastDirs()        );
        board[5][10]  = new Pusher24(                5,  10, getWestDirs(),       Direction.EAST);
        board[6][10]  = new DualSpeedConveyor(       6,  10, null,                getSouthDirs(), Direction.NORTH);
        board[10][10] = new SingleSpeedConveyor(     10, 10, null,                getSouthDirs(), Direction.NORTH);
        board[0][11]  = new BasicElement(            0,  11, null,                0, BasicElementType.REPAIR);
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[3][11]  = new DualSpeedConveyor(       3,  11, null,                getSouthDirs(), Direction.NORTH);
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[6][11]  = new DualSpeedConveyor(       6,  11, null,                getSouthDirs(), Direction.NORTH);
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        board[10][11] = new SingleSpeedConveyor(     10, 11, null,                getSouthDirs(), Direction.NORTH);
        */
    }

    /**
     * Adds the elements for the Cross board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addCross( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        /*
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
    
        board[2][0]   = new BasicElement(            2,  0,  getSouthDirs()       );
        board[4][0]   = new BasicElement(            4,  0,  getSouthDirs()       );
        board[5][0]   = new SingleSpeedConveyor(     5,  0,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][0]   = new SingleSpeedConveyor(     6,  0,  null,                getSouthDirs(), Direction.NORTH);
        board[7][0]   = new BasicElement(            7,  0,  getSouthEastDirs()   );
        board[8][0]   = new BasicElement(            8,  0,  getWestDirs()        );
        board[9][0]   = new BasicElement(            9,  0,  getSouthDirs(),      0, BasicElementType.REPAIR);
        board[10][0]  = new SingleSpeedConveyor(     10, 0,  null,                getSouthDirs(), Direction.NORTH);
        board[11][0]  = new BasicElement(            11, 0,  null,                0, BasicElementType.HOLE);
        board[0][1]   = new SingleSpeedConveyor(     0,  1,  null,                getWestDirs(), Direction.EAST);
        board[1][1]   = new SingleSpeedConveyor(     1,  1,  null,                getWestDirs(), Direction.NORTH);
        board[3][1]   = new BasicElement(            3,  1,  getNorthEastDirs()   );
        board[4][1]   = new BasicElement(            4,  1,  getWestDirs(),       0, BasicElementType.HOLE);
        board[5][1]   = new SingleSpeedConveyor(     5,  1,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][1]   = new SingleSpeedConveyor(     6,  1,  null,                getSouthDirs(), Direction.NORTH);
        board[8][1]   = new BasicElement(            8,  1,  getNorthDirs()       );
        board[10][1]  = new SingleSpeedConveyor(     10, 1,  null,                getSouthDirs(), Direction.EAST);
        board[11][1]  = new SingleSpeedConveyor(     11, 1,  null,                getWestDirs(), Direction.EAST);
        board[0][2]   = new BasicElement(            0,  2,  getWestDirs()        );
        board[1][2]   = new SingleSpeedConveyor(     1,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[3][2]   = new BasicElement(            3,  2,  getSouthDirs(),      0, BasicElementType.OPTION);
        board[4][2]   = new BasicElement(            4,  2,  null,                0, BasicElementType.HOLE);
        board[5][2]   = new SingleSpeedConveyor(     5,  2,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][2]   = new SingleSpeedConveyor(     6,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[8][2]   = new BasicElement(            8,  2,  getSouthDirs()       ); // Laser mount south, strength 1
        board[9][2]   = new BasicElement(            9,  2,  getNorthDirs()       );
        board[10][2]  = new BasicElement(            10, 2,  null,                0, BasicElementType.HOLE);
        board[11][2]  = new BasicElement(            11, 2,  getEastDirs()        );
        board[1][3]   = new SingleSpeedConveyor(     1,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[3][3]   = new BasicElement(            3,  3,  getNorthDirs()       );
        board[4][3]   = new SingleSpeedConveyor(     4,  3,  null,                getNorthDirs(), Direction.EAST);
        board[5][3]   = new SingleSpeedConveyor(     5,  3,  null,                getWestDirs(), Direction.SOUTH);
        board[6][3]   = new SingleSpeedConveyor(     6,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[8][3]   = new BasicElement(            8,  3,  getNorthDirs()       );
        board[9][3]   = new BasicElement(            9,  3,  getSouthDirs()       );
        board[0][4]   = new BasicElement(            0,  4,  getWestDirs()        );
        board[1][4]   = new SingleSpeedConveyor(     1,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[2][4]   = new BasicElement(            2,  4,  getEastDirs()        ); // Laser mount east, strength 1
        board[3][4]   = new SingleSpeedConveyor(     3,  4,  getSouthWestDirs(),  getNorthDirs(), Direction.EAST);
        board[4][4]   = new SingleSpeedConveyor(     4,  4,  null,                getWestDirs(), Direction.SOUTH);
        board[5][4]   = new BasicElement(            5,  4,  null,                0, BasicElementType.HOLE);
        board[6][4]   = new SingleSpeedConveyor(     6,  4,  null,                getSouthDirs(), Direction.EAST);
        board[7][4]   = new SingleSpeedConveyor(     7,  4,  getEastDirs(),       getWestDirs(), Direction.NORTH);
        board[8][4]   = new BasicElement(            8,  4,  getSouthWestDirs()   );
        board[11][4]  = new BasicElement(            11, 4,  getEastDirs()        );
        board[0][5]   = new SingleSpeedConveyor(     0,  5,  null,                getWestDirs(), Direction.EAST);
        board[1][5]   = new SingleSpeedConveyor(     1,  5,  null,                getSouthWestDirs(), Direction.EAST);
        board[2][5]   = new SingleSpeedConveyor(     2,  5,  null,                getWestDirs(), Direction.EAST);
        board[3][5]   = new SingleSpeedConveyor(     3,  5,  null,                getWestDirs(), Direction.SOUTH);
        board[4][5]   = new BasicElement(            4,  5,  null,                0, BasicElementType.HOLE);
        board[5][5]   = new BasicElement(            5,  5,  null,                0, BasicElementType.HOLE);
        board[6][5]   = new BasicElement(            6,  5,  null,                0, BasicElementType.HOLE);
        board[7][5]   = new SingleSpeedConveyor(     7,  5,  null,                getSouthDirs(), Direction.EAST);
        board[8][5]   = new SingleSpeedConveyor(     8,  5,  null,                getWestDirs(), Direction.EAST);
        board[9][5]   = new SingleSpeedConveyor(     9,  5,  null,                getWestDirs(), Direction.EAST);
        board[10][5]  = new SingleSpeedConveyor(     10, 5,  null,                getWestDirs(), Direction.EAST);
        board[11][5]  = new SingleSpeedConveyor(     11, 5,  null,                getWestDirs(), Direction.EAST);
        board[0][6]   = new SingleSpeedConveyor(     0,  6,  null,                getEastDirs(), Direction.WEST);
        board[1][6]   = new SingleSpeedConveyor(     1,  6,  null,                getEastDirs(), Direction.WEST);
        board[2][6]   = new SingleSpeedConveyor(     2,  6,  null,                getEastDirs(), Direction.WEST);
        board[3][6]   = new SingleSpeedConveyor(     3,  6,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[4][6]   = new SingleSpeedConveyor(     4,  6,  null,                getNorthDirs(), Direction.WEST);
        board[5][6]   = new BasicElement(            5,  6,  null,                0, BasicElementType.HOLE);
        board[6][6]   = new SingleSpeedConveyor(     6,  6,  null,                getEastDirs(), Direction.NORTH);
        board[7][6]   = new SingleSpeedConveyor(     7,  6,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[8][6]   = new SingleSpeedConveyor(     8,  6,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[9][6]   = new SingleSpeedConveyor(     9,  6,  null,                getEastDirs(), Direction.WEST);
        board[10][6]  = new SingleSpeedConveyor(     10, 6,  null,                getNorthEastDirs(), Direction.WEST);
        board[11][6]  = new SingleSpeedConveyor(     11, 6,  null,                getEastDirs(), Direction.WEST);
        board[0][7]   = new BasicElement(            0,  7,  getWestDirs()        );
        board[3][7]   = new BasicElement(            3,  7,  getSouthEastDirs()   );
        board[4][7]   = new SingleSpeedConveyor(     4,  7,  getWestDirs(),       getEastDirs(), Direction.SOUTH);
        board[5][7]   = new SingleSpeedConveyor(     5,  7,  null,                getNorthDirs(), Direction.WEST);
        board[6][7]   = new SingleSpeedConveyor(     6,  7,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[7][7]   = new BasicElement(            7,  7,  getSouthWestDirs()   );
        board[8][7]   = new BasicElement(            8,  7,  getSouthDirs()       ); // Laser mount south, strength 1
        board[10][7]  = new SingleSpeedConveyor(     10, 7,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][7]  = new BasicElement(            11, 7,  getNorthEastDirs()   );
        board[0][8]   = new BasicElement(            0,  8,  getEastDirs()        );
        board[1][8]   = new BasicElement(            1,  8,  getWestDirs()        );
        board[2][8]   = new BasicElement(            2,  8,  null                 );
        board[3][8]   = new BasicElement(            3,  8,  getEastDirs()        ); // Laser mount east, strength 2
        board[4][8]   = new BasicElement(            4,  8,  getWestDirs()        );
        board[5][8]   = new SingleSpeedConveyor(     5,  8,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][8]   = new SingleSpeedConveyor(     6,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[8][8]   = new BasicElement(            8,  8,  getNorthDirs()       );
        board[10][8]  = new SingleSpeedConveyor(     10, 8,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][8]  = new BasicElement(            11, 8,  getSouthDirs()       );
        board[0][9]   = new BasicElement(            0,  9,  getWestDirs()        );
        board[2][9]   = new BasicElement(            2,  9,  null,                0, BasicElementType.HOLE);
        board[4][9]   = new BasicElement(            4,  9,  getEastDirs()        );
        board[5][9]   = new SingleSpeedConveyor(     5,  9,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[6][9]   = new SingleSpeedConveyor(     6,  9,  null,                getSouthDirs(), Direction.NORTH);
        board[7][9]   = new BasicElement(            7,  9,  null,                0, BasicElementType.OPTION);
        board[8][9]   = new BasicElement(            8,  9,  getSouthDirs(),      0, BasicElementType.HOLE);
        board[10][9]  = new SingleSpeedConveyor(     10, 9,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][9]  = new BasicElement(            11, 9,  getEastDirs()        );
        board[0][10]  = new SingleSpeedConveyor(     0,  10, null,                getEastDirs(), Direction.WEST);
        board[1][10]  = new SingleSpeedConveyor(     1,  10, null,                getNorthDirs(), Direction.WEST);
        board[5][10]  = new SingleSpeedConveyor(     5,  10, null,                getNorthDirs(), Direction.SOUTH);
        board[6][10]  = new SingleSpeedConveyor(     6,  10, getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[7][10]  = new BasicElement(            7,  10, getWestDirs()        );
        board[10][10] = new SingleSpeedConveyor(     10, 10, null,                getEastDirs(), Direction.SOUTH);
        board[11][10] = new SingleSpeedConveyor(     11, 10, null,                getEastDirs(), Direction.WEST);
        board[0][11]  = new BasicElement(            0,  11, null,                0, BasicElementType.REPAIR);
        board[1][11]  = new SingleSpeedConveyor(     1,  11, null,                getNorthDirs(), Direction.SOUTH);
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[5][11]  = new SingleSpeedConveyor(     5,  11, null,                getNorthDirs(), Direction.SOUTH);
        board[6][11]  = new SingleSpeedConveyor(     6,  11, null,                getSouthDirs(), Direction.NORTH);
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        */
    }

    /**
     * Adds the elements for the Chess board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addChess( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        /*
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
    
        board[2][0]   = new BasicElement(            2,  0,  getSouthDirs()       );
        board[4][0]   = new BasicElement(            4,  0,  getSouthDirs()       );
        board[7][0]   = new BasicElement(            7,  0,  getSouthDirs()       );
        board[9][0]   = new BasicElement(            9,  0,  getSouthDirs()       );
        board[11][0]  = new BasicElement(            11, 0,  null,                0, BasicElementType.REPAIR);
        board[1][1]   = new DualSpeedConveyor(       1,  1,  null,                getEastDirs(), Direction.NORTH);
        board[2][1]   = new DualSpeedConveyor(       2,  1,  null,                getEastDirs(), Direction.WEST);
        board[3][1]   = new DualSpeedConveyor(       3,  1,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[4][1]   = new DualSpeedConveyor(       4,  1,  null,                getEastDirs(), Direction.WEST);
        board[5][1]   = new DualSpeedConveyor(       5,  1,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[6][1]   = new DualSpeedConveyor(       6,  1,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[7][1]   = new DualSpeedConveyor(       7,  1,  null,                getEastDirs(), Direction.WEST);
        board[8][1]   = new DualSpeedConveyor(       8,  1,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[9][1]   = new DualSpeedConveyor(       9,  1,  null,                getEastDirs(), Direction.WEST);
        board[10][1]  = new DualSpeedConveyor(       10, 1,  null,                getNorthDirs(), Direction.WEST);
        board[0][2]   = new BasicElement(            0,  2,  getWestDirs()        );
        board[1][2]   = new DualSpeedConveyor(       1,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[2][2]   = new SingleSpeedConveyor(     2,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[3][2]   = new BasicElement(            3,  2,  getSouthDirs()       );
        board[4][2]   = new SingleSpeedConveyor(     4,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[5][2]   = new BasicElement(            5,  2,  getSouthDirs()       );
        board[6][2]   = new SingleSpeedConveyor(     6,  2,  getSouthDirs(),      getSouthDirs(), Direction.NORTH);
        board[8][2]   = new SingleSpeedConveyor(     8,  2,  getSouthDirs(),      getSouthDirs(), Direction.NORTH);
        board[10][2]  = new DualSpeedConveyor(       10, 2,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][2]  = new BasicElement(            11, 2,  getEastDirs()        );
        board[1][3]   = new DualSpeedConveyor(       1,  3,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[2][3]   = new BasicElement(            2,  3,  getWestDirs()        );
        board[3][3]   = new SingleSpeedConveyor(     3,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[5][3]   = new BasicElement(            5,  3,  null,                0, BasicElementType.HOLE);
        board[7][3]   = new SingleSpeedConveyor(     7,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[9][3]   = new SingleSpeedConveyor(     9,  3,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[10][3]  = new DualSpeedConveyor(       10, 3,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[0][4]   = new BasicElement(            0,  4,  getWestDirs()        );
        board[1][4]   = new DualSpeedConveyor(       1,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[2][4]   = new SingleSpeedConveyor(     2,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[4][4]   = new SingleSpeedConveyor(     4,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[6][4]   = new SingleSpeedConveyor(     6,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[8][4]   = new SingleSpeedConveyor(     8,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[10][4]  = new DualSpeedConveyor(       10, 4,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][4]  = new BasicElement(            11, 4,  getEastDirs()        );
        board[1][5]   = new DualSpeedConveyor(       1,  5,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[2][5]   = new BasicElement(            2,  5,  getWestDirs()        );
        board[3][5]   = new SingleSpeedConveyor(     3,  5,  null,                getSouthDirs(), Direction.NORTH);
        board[5][5]   = new BasicElement(            5,  5,  null,                0, BasicElementType.OPTION);
        board[7][5]   = new SingleSpeedConveyor(     7,  5,  null,                getSouthDirs(), Direction.NORTH);
        board[9][5]   = new SingleSpeedConveyor(     9,  5,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[10][5]  = new DualSpeedConveyor(       10, 5,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[1][6]   = new DualSpeedConveyor(       1,  6,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[2][6]   = new SingleSpeedConveyor(     2,  6,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[4][6]   = new BasicElement(            4,  6,  null,                0, BasicElementType.HOLE);
        board[6][6]   = new BasicElement(            6,  6,  null,                0, BasicElementType.OPTION);
        board[8][6]   = new SingleSpeedConveyor(     8,  6,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][6]   = new BasicElement(            9,  6,  getEastDirs()        );
        board[10][6]  = new DualSpeedConveyor(       10, 6,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[0][7]   = new BasicElement(            0,  7,  getWestDirs()        );
        board[1][7]   = new DualSpeedConveyor(       1,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[3][7]   = new SingleSpeedConveyor(     3,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[5][7]   = new SingleSpeedConveyor(     5,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[7][7]   = new SingleSpeedConveyor(     7,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][7]   = new SingleSpeedConveyor(     9,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][7]  = new DualSpeedConveyor(       10, 7,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][7]  = new BasicElement(            11, 7,  getEastDirs()        );
        board[1][8]   = new DualSpeedConveyor(       1,  8,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[2][8]   = new SingleSpeedConveyor(     2,  8,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[4][8]   = new SingleSpeedConveyor(     4,  8,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][8]   = new SingleSpeedConveyor(     6,  8,  null,                getNorthDirs(), Direction.SOUTH);
        board[8][8]   = new BasicElement(            8,  8,  null,                0, BasicElementType.HOLE);
        board[9][8]   = new BasicElement(            9,  8,  getEastDirs()        );
        board[10][8]  = new DualSpeedConveyor(       10, 8,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[0][9]   = new BasicElement(            0,  9,  getWestDirs()        );
        board[1][9]   = new DualSpeedConveyor(       1,  9,  null,                getSouthDirs(), Direction.NORTH);
        board[3][9]   = new SingleSpeedConveyor(     3,  9,  getNorthDirs(),      getNorthDirs(), Direction.SOUTH);
        board[5][9]   = new SingleSpeedConveyor(     5,  9,  getNorthDirs(),      getNorthDirs(), Direction.SOUTH);
        board[6][9]   = new BasicElement(            6,  9,  getNorthDirs()       );
        board[7][9]   = new SingleSpeedConveyor(     7,  9,  null,                getNorthDirs(), Direction.SOUTH);
        board[8][9]   = new BasicElement(            8,  9,  getNorthDirs()       );
        board[9][9]   = new SingleSpeedConveyor(     9,  9,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][9]  = new DualSpeedConveyor(       10, 9,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][9]  = new BasicElement(            11, 9,  getEastDirs()        );
        board[1][10]  = new DualSpeedConveyor(       1,  10, null,                getSouthDirs(), Direction.EAST);
        board[2][10]  = new DualSpeedConveyor(       2,  10, null,                getWestDirs(), Direction.EAST);
        board[3][10]  = new DualSpeedConveyor(       3,  10, getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[4][10]  = new DualSpeedConveyor(       4,  10, null,                getWestDirs(), Direction.EAST);
        board[5][10]  = new DualSpeedConveyor(       5,  10, getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[6][10]  = new DualSpeedConveyor(       6,  10, getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[7][10]  = new DualSpeedConveyor(       7,  10, null,                getWestDirs(), Direction.EAST);
        board[8][10]  = new DualSpeedConveyor(       8,  10, getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[9][10]  = new DualSpeedConveyor(       9,  10, null,                getWestDirs(), Direction.EAST);
        board[10][10] = new DualSpeedConveyor(       10, 10, null,                getWestDirs(), Direction.SOUTH);
        board[0][11]  = new BasicElement(            0,  11, null,                0, BasicElementType.REPAIR);
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        */
    }

    /**
     * Adds the elements for the ChopShop board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addChopShop( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        /*
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
    
        board[1][0]   = new SingleSpeedConveyor(     1,  0,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][0]   = new BasicElement(            2,  0,  getSouthDirs()       );
        board[3][0]   = new SingleSpeedConveyor(     3,  0,  null,                getSouthDirs(), Direction.NORTH);
        board[4][0]   = new BasicElement(            4,  0,  getSouthDirs()       );
        board[7][0]   = new BasicElement(            7,  0,  getSouthDirs()       );
        board[8][0]   = new SingleSpeedConveyor(     8,  0,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][0]   = new BasicElement(            9,  0,  getSouthDirs()       );
        board[11][0]  = new BasicElement(            11, 0,  null,                0, BasicElementType.REPAIR);
        board[0][1]   = new BasicElement(            0,  1,  getEastDirs()        );
        board[1][1]   = new BasicElement(            1,  1,  getEastWestDirs()    ); // Laser mount east, strength 3
        board[2][1]   = new BasicElement(            2,  1,  getWestDirs()        );
        board[3][1]   = new SingleSpeedConveyor(     3,  1,  null,                getSouthDirs(), Direction.NORTH);
        board[6][1]   = new BasicElement(            6,  1,  getEastDirs()        );
        board[7][1]   = new BasicElement(            7,  1,  getWestDirs()        ); // Laser mount west, strength 1
        board[8][1]   = new SingleSpeedConveyor(     8,  1,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][1]   = new BasicElement(            9,  1,  getEastDirs()        );
        board[10][1]  = new BasicElement(            10, 1,  getWestDirs()        );
        board[0][2]   = new BasicElement(            0,  2,  getWestDirs()        );
        board[1][2]   = new SingleSpeedConveyor(     1,  2,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][2]   = new BasicElement(            2,  2,  null,                0, BasicElementType.OPTION);
        board[3][2]   = new SingleSpeedConveyor(     3,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[5][2]   = new BasicElement(            5,  2,  null,                0, BasicElementType.HOLE);
        board[8][2]   = new SingleSpeedConveyor(     8,  2,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][2]   = new BasicElement(            9,  2,  null,                0, BasicElementType.HOLE);
        board[11][2]  = new BasicElement(            11, 2,  getEastDirs()        );
        board[1][3]   = new SingleSpeedConveyor(     1,  3,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][3]   = new BasicElement(            2,  3,  getNorthEastDirs()   );
        board[3][3]   = new BasicElement(            3,  3,  getWestDirs()        );
        board[4][3]   = new RotatorClockwise(        4,  3,  null                 );
        board[5][3]   = new RotatorCounterClockwise( 5,  3,  null                 );
        board[6][3]   = new BasicElement(            6,  3,  getEastDirs()        ); // Laser mount east, strength 2
        board[7][3]   = new BasicElement(            7,  3,  getWestDirs()        );
        board[8][3]   = new RotatorCounterClockwise( 8,  3,  null                 );
        board[9][3]   = new SingleSpeedConveyor(     9,  3,  null,                getEastDirs(), Direction.WEST);
        board[10][3]  = new SingleSpeedConveyor(     10, 3,  null,                getEastDirs(), Direction.WEST);
        board[11][3]  = new SingleSpeedConveyor(     11, 3,  null,                getEastDirs(), Direction.WEST);
        board[0][4]   = new BasicElement(            0,  4,  getWestDirs()        );
        board[1][4]   = new SingleSpeedConveyor(     1,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[2][4]   = new BasicElement(            2,  4,  getSouthDirs()       ); // Laser mount south, strength 1
        board[3][4]   = new DualSpeedConveyor(       3,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[6][4]   = new SingleSpeedConveyor(     6,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[8][4]   = new BasicElement(            8,  4,  getNorthDirs()       );
        board[10][4]  = new BasicElement(            10, 4,  getNorthDirs()       );
        board[11][4]  = new BasicElement(            11, 4,  getEastDirs()        );
        board[0][5]   = new SingleSpeedConveyor(     0,  5,  null,                getWestDirs(), Direction.EAST);
        board[1][5]   = new SingleSpeedConveyor(     1,  5,  null,                getWestDirs(), Direction.SOUTH);
        board[2][5]   = new RotatorCounterClockwise( 2,  5,  null                 );
        board[3][5]   = new DualSpeedConveyor(       3,  5,  null,                getSouthDirs(), Direction.NORTH);
        board[6][5]   = new SingleSpeedConveyor(     6,  5,  null,                getNorthDirs(), Direction.SOUTH);
        board[7][5]   = new BasicElement(            7,  5,  null,                0, BasicElementType.HOLE);
        board[8][5]   = new BasicElement(            8,  5,  getSouthDirs()       ); // Laser mount south, strength 1
        board[10][5]  = new BasicElement(            10, 5,  getSouthDirs()       );
        board[0][6]   = new SingleSpeedConveyor(     0,  6,  null,                getEastDirs(), Direction.WEST);
        board[1][6]   = new SingleSpeedConveyor(     1,  6,  null,                getEastDirs(), Direction.WEST);
        board[2][6]   = new RotatorClockwise(        2,  6,  null                 );
        board[3][6]   = new DualSpeedConveyor(       3,  6,  null,                getSouthDirs(), Direction.NORTH);
        board[5][6]   = new BasicElement(            5,  6,  getEastDirs(),       0, BasicElementType.OPTION);
        board[6][6]   = new SingleSpeedConveyor(     6,  6,  getWestDirs(),       getEastDirs(), Direction.SOUTH);
        board[7][6]   = new SingleSpeedConveyor(     7,  6,  null,                getEastDirs(), Direction.WEST);
        board[8][6]   = new RotatorClockwise(        8,  6,  null                 );
        board[9][6]   = new SingleSpeedConveyor(     9,  6,  null,                getEastDirs(), Direction.WEST);
        board[10][6]  = new SingleSpeedConveyor(     10, 6,  null,                getEastDirs(), Direction.WEST);
        board[11][6]  = new SingleSpeedConveyor(     11, 6,  null,                getEastDirs(), Direction.WEST);
        board[0][7]   = new BasicElement(            0,  7,  getWestDirs()        );
        board[3][7]   = new DualSpeedConveyor(       3,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[5][7]   = new SingleSpeedConveyor(     5,  7,  getNorthDirs(),      getWestDirs(), Direction.EAST);
        board[6][7]   = new RotatorCounterClockwise( 6,  7,  null                 );
        board[8][7]   = new BasicElement(            8,  7,  getNorthDirs()       );
        board[9][7]   = new BasicElement(            9,  7,  null,                0, BasicElementType.OPTION);
        board[11][7]  = new BasicElement(            11, 7,  getEastDirs()        );
        board[3][8]   = new DualSpeedConveyor(       3,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[4][8]   = new BasicElement(            4,  8,  getEastDirs()        );
        board[5][8]   = new BasicElement(            5,  8,  getSouthWestDirs()   );
        board[6][8]   = new SingleSpeedConveyor(     6,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[8][8]   = new BasicElement(            8,  8,  getSouthDirs()       );
        board[9][8]   = new BasicElement(            9,  8,  null,                0, BasicElementType.HOLE);
        board[0][9]   = new BasicElement(            0,  9,  getWestDirs()        );
        board[2][9]   = new BasicElement(            2,  9,  getNorthDirs()       );
        board[3][9]   = new DualSpeedConveyor(       3,  9,  null,                getSouthEastDirs(), Direction.NORTH);
        board[4][9]   = new DualSpeedConveyor(       4,  9,  null,                getEastDirs(), Direction.WEST);
        board[5][9]   = new DualSpeedConveyor(       5,  9,  null,                getNorthDirs(), Direction.WEST);
        board[6][9]   = new SingleSpeedConveyor(     6,  9,  null,                getSouthDirs(), Direction.NORTH);
        board[8][9]   = new SingleSpeedConveyor(     8,  9,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][9]   = new SingleSpeedConveyor(     9,  9,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][9]  = new BasicElement(            11, 9,  getEastDirs()        );
        board[1][10]  = new BasicElement(            1,  10, null,                0, BasicElementType.HOLE);
        board[2][10]  = new BasicElement(            2,  10, getSouthEastDirs()   );
        board[3][10]  = new DualSpeedConveyor(       3,  10, getWestDirs(),       getSouthDirs(), Direction.NORTH);
        board[4][10]  = new BasicElement(            4,  10, null                 );
        board[5][10]  = new DualSpeedConveyor(       5,  10, getEastDirs(),       getNorthDirs(), Direction.SOUTH); // Laser mount east, strength 1
        board[6][10]  = new SingleSpeedConveyor(     6,  10, getWestDirs(),       getSouthDirs(), Direction.NORTH);
        board[8][10]  = new SingleSpeedConveyor(     8,  10, null,                getNorthDirs(), Direction.SOUTH);
        board[9][10]  = new SingleSpeedConveyor(     9,  10, null,                getEastDirs(), Direction.SOUTH);
        board[10][10] = new SingleSpeedConveyor(     10, 10, null,                getEastDirs(), Direction.WEST);
        board[11][10] = new SingleSpeedConveyor(     11, 10, null,                getEastDirs(), Direction.WEST);
        board[0][11]  = new BasicElement(            0,  11, null,                0, BasicElementType.REPAIR);
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[3][11]  = new DualSpeedConveyor(       3,  11, null,                getSouthDirs(), Direction.NORTH);
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[5][11]  = new DualSpeedConveyor(       5,  11, null,                getNorthDirs(), Direction.SOUTH);
        board[6][11]  = new SingleSpeedConveyor(     6,  11, null,                getSouthDirs(), Direction.NORTH);
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[8][11]  = new SingleSpeedConveyor(     8,  11, null,                getNorthDirs(), Direction.SOUTH);
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        */
    }

    /**
     * Adds the elements for the Island board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addIsland( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        /*
        addElement( createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
    
        board[2][0]   = new BasicElement(            2,  0,  getSouthDirs()       );
        board[4][0]   = new BasicElement(            4,  0,  getSouthDirs()       );
        board[7][0]   = new BasicElement(            7,  0,  getSouthDirs()       );
        board[9][0]   = new BasicElement(            9,  0,  getSouthDirs(),      0, BasicElementType.REPAIR);
        board[1][1]   = new BasicElement(            1,  1,  null,                0, BasicElementType.HOLE);
        board[2][1]   = new BasicElement(            2,  1,  null,                0, BasicElementType.HOLE);
        board[9][1]   = new BasicElement(            9,  1,  null,                0, BasicElementType.HOLE);
        board[10][1]  = new BasicElement(            10, 1,  null,                0, BasicElementType.HOLE);
        board[0][2]   = new BasicElement(            0,  2,  getWestDirs()        );
        board[1][2]   = new BasicElement(            1,  2,  null,                0, BasicElementType.HOLE);
        board[2][2]   = new RotatorClockwise(        2,  2,  null                 );
        board[3][2]   = new SingleSpeedConveyor(     3,  2,  null,                getEastDirs(), Direction.WEST);
        board[4][2]   = new SingleSpeedConveyor(     4,  2,  null,                getEastDirs(), Direction.WEST);
        board[5][2]   = new SingleSpeedConveyor(     5,  2,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[6][2]   = new SingleSpeedConveyor(     6,  2,  null,                getEastDirs(), Direction.WEST);
        board[7][2]   = new SingleSpeedConveyor(     7,  2,  null,                getEastDirs(), Direction.WEST);
        board[8][2]   = new SingleSpeedConveyor(     8,  2,  null,                getEastDirs(), Direction.WEST);
        board[9][2]   = new SingleSpeedConveyor(     9,  2,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][2]  = new BasicElement(            10, 2,  null,                0, BasicElementType.HOLE);
        board[11][2]  = new BasicElement(            11, 2,  getEastDirs()        );
        board[2][3]   = new SingleSpeedConveyor(     2,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[3][3]   = new RotatorCounterClockwise( 3,  3,  null                 );
        board[4][3]   = new SingleSpeedConveyor(     4,  3,  null,                getWestDirs(), Direction.EAST);
        board[5][3]   = new SingleSpeedConveyor(     5,  3,  getSouthDirs(),      getNorthWestDirs(), Direction.EAST);
        board[6][3]   = new SingleSpeedConveyor(     6,  3,  null,                getWestDirs(), Direction.EAST);
        board[7][3]   = new SingleSpeedConveyor(     7,  3,  null,                getWestDirs(), Direction.EAST);
        board[8][3]   = new RotatorCounterClockwise( 8,  3,  null                 );
        board[9][3]   = new SingleSpeedConveyor(     9,  3,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][4]   = new BasicElement(            0,  4,  getWestDirs()        );
        board[2][4]   = new SingleSpeedConveyor(     2,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[3][4]   = new SingleSpeedConveyor(     3,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[5][4]   = new SingleSpeedConveyor(     5,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[6][4]   = new BasicElement(            6,  4,  null,                0, BasicElementType.HOLE);
        board[7][4]   = new BasicElement(            7,  4,  null,                0, BasicElementType.HOLE);
        board[8][4]   = new SingleSpeedConveyor(     8,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[9][4]   = new SingleSpeedConveyor(     9,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][4]  = new BasicElement(            11, 4,  getEastDirs()        );
        board[2][5]   = new SingleSpeedConveyor(     2,  5,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[3][5]   = new SingleSpeedConveyor(     3,  5,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[5][5]   = new SingleSpeedConveyor(     5,  5,  null,                getNorthDirs(), Direction.SOUTH);
        board[7][5]   = new BasicElement(            7,  5,  null,                0, BasicElementType.HOLE);
        board[8][5]   = new SingleSpeedConveyor(     8,  5,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[9][5]   = new SingleSpeedConveyor(     9,  5,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[2][6]   = new SingleSpeedConveyor(     2,  6,  null,                getSouthDirs(), Direction.NORTH);
        board[3][6]   = new SingleSpeedConveyor(     3,  6,  null,                getNorthDirs(), Direction.SOUTH);
        board[4][6]   = new BasicElement(            4,  6,  null,                0, BasicElementType.HOLE);
        board[5][6]   = new BasicElement(            5,  6,  null,                0, BasicElementType.OPTION);
        board[6][6]   = new SingleSpeedConveyor(     6,  6,  null,                getSouthDirs(), Direction.NORTH);
        board[8][6]   = new SingleSpeedConveyor(     8,  6,  null,                getSouthDirs(), Direction.NORTH);
        board[9][6]   = new SingleSpeedConveyor(     9,  6,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][7]   = new BasicElement(            0,  7,  getWestDirs()        );
        board[2][7]   = new SingleSpeedConveyor(     2,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[3][7]   = new SingleSpeedConveyor(     3,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[4][7]   = new BasicElement(            4,  7,  null,                0, BasicElementType.HOLE);
        board[5][7]   = new BasicElement(            5,  7,  null,                0, BasicElementType.HOLE);
        board[6][7]   = new SingleSpeedConveyor(     6,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[8][7]   = new SingleSpeedConveyor(     8,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[9][7]   = new SingleSpeedConveyor(     9,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][7]  = new BasicElement(            11, 7,  getEastDirs()        );
        board[2][8]   = new SingleSpeedConveyor(     2,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[3][8]   = new RotatorCounterClockwise( 3,  8,  null                 );
        board[4][8]   = new SingleSpeedConveyor(     4,  8,  null,                getEastDirs(), Direction.WEST);
        board[5][8]   = new SingleSpeedConveyor(     5,  8,  null,                getEastDirs(), Direction.WEST);
        board[6][8]   = new SingleSpeedConveyor(     6,  8,  getNorthDirs(),      getSouthEastDirs(), Direction.WEST);
        board[7][8]   = new SingleSpeedConveyor(     7,  8,  null,                getEastDirs(), Direction.WEST);
        board[8][8]   = new RotatorCounterClockwise( 8,  8,  null                 );
        board[9][8]   = new SingleSpeedConveyor(     9,  8,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][9]   = new BasicElement(            0,  9,  getWestDirs()        );
        board[1][9]   = new BasicElement(            1,  9,  null,                0, BasicElementType.HOLE);
        board[2][9]   = new RotatorClockwise(        2,  9,  null                 );
        board[3][9]   = new SingleSpeedConveyor(     3,  9,  null,                getWestDirs(), Direction.EAST);
        board[4][9]   = new SingleSpeedConveyor(     4,  9,  null,                getWestDirs(), Direction.EAST);
        board[5][9]   = new SingleSpeedConveyor(     5,  9,  null,                getWestDirs(), Direction.EAST);
        board[6][9]   = new SingleSpeedConveyor(     6,  9,  getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[7][9]   = new SingleSpeedConveyor(     7,  9,  null,                getWestDirs(), Direction.EAST);
        board[8][9]   = new SingleSpeedConveyor(     8,  9,  null,                getWestDirs(), Direction.EAST);
        board[9][9]   = new SingleSpeedConveyor(     9,  9,  null,                getWestDirs(), Direction.EAST);
        board[10][9]  = new BasicElement(            10, 9,  null,                0, BasicElementType.HOLE);
        board[11][9]  = new BasicElement(            11, 9,  getEastDirs()        );
        board[1][10]  = new BasicElement(            1,  10, null,                0, BasicElementType.HOLE);
        board[2][10]  = new BasicElement(            2,  10, null,                0, BasicElementType.HOLE);
        board[9][10]  = new BasicElement(            9,  10, null,                0, BasicElementType.HOLE);
        board[10][10] = new BasicElement(            10, 10, null,                0, BasicElementType.HOLE);
        board[0][11]  = new BasicElement(            0,  11, null,                0, BasicElementType.REPAIR);
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        */
    }

    /**
     * Adds the elements for the Exchange board to the builder, given the x and yOffsets
     * 
     * @param xOffset   The x-Offset
     * @param yOffset   The y-Offset
     * @param turnSteps The number of steps to turn this board
     *  
     */
    public void addExchange( int xOffset, int yOffset, int turnSteps ) 
    {
        AbstractBoardElement temp = null;
        setBoardVariables( xOffset, yOffset, turnSteps );
        // row 0
        addElement( createBasicElement( BasicElementType.REPAIR ), 
                    new Point( 0, 0 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 2, 0 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 4, 0 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 7, 0 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 9, 0 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 10, 0 ) );
    
        // row 1
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 0, 1 ) );
        addElement( createClockwiseRotator(), 
                    new Point( 1, 1 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 1 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 1 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 1 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 1 ) );
        addElement( createBasicElement( Direction.of( Direction.NORTH ) ), 
                    new Point( 10, 1 ) );
        addElement( createBasicElement( BasicElementType.HOLE ), 
                    new Point( 11, 1 ) );
        
        // row 2
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 0, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 1, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 2 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 2 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 2 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 11, 2 ) );
    
        // row 3
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 0, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 1, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 2, 3 ) );
        addElement( createCounterClockwiseRotator(), 
                    new Point( 3, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 3 ) );
        addElement( createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 3 ) );
        addElement( createCounterClockwiseRotator(), 
                    new Point( 8, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 9, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 10, 3 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 11, 3 ) );
    
        // row 4
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 0, 4 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST, Direction.SOUTH ), BasicElementType.OPTION ), 
                    new Point( 4, 4 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 4 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 4 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST, Direction.SOUTH ) ), 
                    new Point( 7, 4 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 11, 4 ) );
    
        // row 5
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 0, 5 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 1, 5 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 2, 5 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 3, 5 ) );
        addElement( createDualSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 4, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 7, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 8, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 9, 5 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 10, 5 ) );
    
        // row 6
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 0, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 1, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 2, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 3, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 4, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 7, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 8, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 9, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 10, 6 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 11, 6 ) );
        
        // row 7
        addElement( createBasicElement( Direction.of( Direction.WEST ) ), 
                    new Point( 0, 7 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST, Direction.NORTH ) ), 
                    new Point( 4, 7 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 7 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 7 ) );
        addElement( createBasicElement( Direction.of( Direction.WEST, Direction.NORTH ) ), 
                    new Point( 7, 7 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 11, 7 ) );
    
        // row 8
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 0, 8 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 1, 8 ) );
        addElement( createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 2, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 8 ) );
        addElement( createCounterClockwiseRotator(), 
                    new Point( 8, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 9, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 10, 8 ) );
        addElement( createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                    new Point( 11, 8 ) );
    
        // row 9
        temp = createBasicElement( Direction.of( Direction.WEST ) );
        addElement( temp, 
                    new Point( 0, 9 ) );
        addLaserMount( temp, Direction.WEST, 1 );
        
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 2, 9 ) );
        addElement( createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 9 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 9 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 9 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 9 ) );
        addElement( createBasicElement( Direction.of( Direction.EAST ) ), 
                    new Point( 11, 9 ) );
    
        // row 10
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 0, 10 ) );
        addElement( createClockwiseRotator(), 
                    new Point( 1, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 10 ) );
        addElement( createBasicElement( BasicElementType.HOLE ), 
                    new Point( 9, 10 ) );
        addElement( createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                    new Point( 11, 10 ) );
    
        // row 11
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 1, 11 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 2, 11 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 3, 11 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 4, 11 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 5, 11 ) );
        addElement( createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                    new Point( 6, 11 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 7, 11 ) );
        addElement( createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                    new Point( 8, 11 ) );
        addElement( createBasicElement( Direction.of( Direction.SOUTH ) ), 
                    new Point( 9, 11 ) );
        addElement( createBasicElement( BasicElementType.REPAIR ), 
                    new Point( 11, 11 ) );
    }
}
