package kharybdys.roborally.game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.AbstractBoardElement;
import kharybdys.roborally.game.board.BasicElementType;
import kharybdys.roborally.game.board.GameBuilder;
import kharybdys.roborally.game.board.ImplementedScenario;
import kharybdys.roborally.game.definition.Direction;

/**
 * Factory with helper methods that creates a Game object
 */
public class GameFactory {

    private static final Logger logger = LoggerFactory.getLogger( GameFactory.class );

    /**
   	 * Adds the elements for the Exchange board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addExchange( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
        builder.addElement( builder.createBasicElement( BasicElementType.REPAIR ), 
                new Point( 0, 0 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 1, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 2, 0 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 4, 0 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 7, 0 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 9, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                new Point( 10, 0 ) );

        // row 1
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 0, 1 ) );
        builder.addElement( builder.createClockwiseRotator(), 
                new Point( 1, 1 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 1 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 1 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 1 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 10, 1 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.HOLE ), 
                new Point( 11, 1 ) );
        
        // row 2
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 0, 2 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 2 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 2 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 11, 2 ) );

        // row 3
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 0, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 1, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 2, 3 ) );
        builder.addElement( builder.createCounterClockwiseRotator(), 
                new Point( 3, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 3 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 3 ) );
        builder.addElement( builder.createCounterClockwiseRotator(), 
                new Point( 8, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 9, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 10, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 11, 3 ) );

        // row 4
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 0, 4 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST, Direction.SOUTH ), BasicElementType.OPTION ), 
                new Point( 4, 4 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 4 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 4 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST, Direction.SOUTH ) ), 
                new Point( 7, 4 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 11, 4 ) );

        // row 5
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 0, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 1, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 2, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 3, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 4, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 7, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 8, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 9, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 10, 5 ) );

        // row 6
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 0, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 1, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 2, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 3, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 4, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 7, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 8, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 9, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 10, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 11, 6 ) );
        
        // row 7
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 0, 7 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST, Direction.NORTH ) ), 
                new Point( 4, 7 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 7 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 7 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST, Direction.NORTH ) ), 
                new Point( 7, 7 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 11, 7 ) );

        // row 8
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 0, 8 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 1, 8 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 2, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 8 ) );
        builder.addElement( builder.createCounterClockwiseRotator(), 
                new Point( 8, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 9, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 10, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 11, 8 ) );

        // row 9
        temp = builder.createBasicElement( Direction.of( Direction.WEST ) );
        builder.addElement( temp, 
                new Point( 0, 9 ) );
        builder.addLaserMount( temp, Direction.WEST, 1 );
        
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 2, 9 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 9 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 9 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 9 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 9 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 11, 9 ) );

        // row 10
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 0, 10 ) );
        builder.addElement( builder.createClockwiseRotator(), 
                new Point( 1, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 10 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.HOLE ), 
                new Point( 9, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 11, 10 ) );

        // row 11
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 1, 11 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 11 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 11 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                new Point( 4, 11 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 11 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 11 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                new Point( 7, 11 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 11 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                new Point( 9, 11 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.REPAIR ), 
                new Point( 11, 11 ) );
    }

    /**
   	 * Adds the elements for the Island board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addIsland( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
    	/*
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
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
   	 * Adds the elements for the ChopShop board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addChopShop( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
    	/*
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
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
   	 * Adds the elements for the Chess board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addChess( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
    	/*
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
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
   	 * Adds the elements for the Cross board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addCross( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
    	/*
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
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
   	 * Adds the elements for the Vault board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addVault( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
    	/*
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
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
   	 * Adds the elements for the SpinZone board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addSpinZone( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
    	/*
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
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
   	 * Adds the elements for the Maelstrom board to the builder, given the x and yOffsets
   	 * 
   	 * @param builder   The builder to add the elements to
   	 * @param xOffset   The x-Offset
   	 * @param yOffset   The y-Offset
   	 * @param turnSteps The number of steps to turn this board
     *  
     */
    private static void addMaelstrom( GameBuilder builder, int xOffset, int yOffset, int turnSteps ) 
    {
    	AbstractBoardElement temp = null;
    	builder.setBoardVariables( xOffset, yOffset, turnSteps );
    	// row 0
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 1, 0 ) );
        builder.addElement( builder.create24Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                new Point( 2, 0 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.OPTION ), 
                new Point( 3, 0 ) );
        builder.addElement( builder.create135Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                new Point( 4, 0 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 5, 0 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 0 ) );
        builder.addElement( builder.create135Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                new Point( 7, 0 ) );
        builder.addElement( builder.create24Pusher( Direction.of( Direction.NORTH ), Direction.SOUTH ), 
                new Point( 9, 0 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.REPAIR ), 
                new Point( 11, 0 ) );
        
        // row 1
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.NORTH, Direction.SOUTH ) ), 
                new Point( 1, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 2, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 3, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 4, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST, Direction.NORTH ) ), 
                new Point( 5, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 6, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 7, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 8, 1 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                new Point( 9, 1 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.EAST ) ), 
                new Point( 10, 1 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 11, 1 ) );
        
        // row 2
        builder.addElement( builder.create24Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                new Point( 0, 2 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 3, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 4, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 5, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 6, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 7, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                new Point( 8, 2 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 2 ) );
        builder.addElement( builder.create24Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                new Point( 11, 2 ) );

        // row 3
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 3 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 3 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 4, 3 ) );

        temp = builder.createDualSpeedConveyor( Direction.of( Direction.NORTH ), Direction.EAST, Direction.of( Direction.WEST ) );
        builder.addElement( temp, 
                new Point( 5, 3 ) );
        builder.addLaserMount( temp, Direction.NORTH, 1 );

        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 6, 3 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                new Point( 7, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 3 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 3 ) );

        // row 4
        builder.addElement( builder.create135Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                new Point( 0, 4 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 4 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 4 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 4 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.SOUTH ) ), 
                new Point( 4, 4 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 5, 4 ) );

        temp = builder.createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.SOUTH, Direction.of( Direction.WEST ) );
        builder.addElement( temp, 
                new Point( 6, 4 ) );
        builder.addLaserMount( temp, Direction.NORTH, 1 );

        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 7, 4 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 4 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 4 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 4 ) );
        builder.addElement( builder.create135Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                new Point( 11, 4 ) );

        // row 5
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 5 ) );

        temp = builder.createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.NORTH, Direction.of( Direction.SOUTH ) );
        builder.addElement( temp, 
                new Point( 4, 5 ) );
        builder.addLaserMount( temp, Direction.WEST, 1 );

        builder.addElement( builder.createBasicElement( BasicElementType.HOLE ), 
                new Point( 5, 5 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.HOLE ), 
                new Point( 6, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 7, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.EAST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 5 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH, Direction.EAST ) ), 
                new Point( 10, 5 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 11, 5 ) );

        // row 6
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 0, 6 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.WEST, Direction.SOUTH ) ), 
                new Point( 1, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.EAST ), Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 6 ) );
        
        temp = builder.createDualSpeedConveyor( Direction.of( Direction.WEST ), Direction.NORTH, Direction.of( Direction.SOUTH ) );
        builder.addElement( temp, 
                new Point( 3, 6 ) );
        builder.addLaserMount( temp, Direction.WEST, 1 );
        
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 4, 6 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.HOLE ), 
                new Point( 5, 6 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.HOLE ), 
                new Point( 6, 6 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.EAST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 7, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.WEST ), Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 6 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 6 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 11, 6 ) );

        // row 7
        builder.addElement( builder.create135Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                new Point( 0, 7 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 7 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 7 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 7 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 4, 7 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.NORTH, Direction.of( Direction.EAST ) ), 
                new Point( 5, 7 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 6, 7 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                new Point( 7, 7 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 7 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 7 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 7 ) );
        builder.addElement( builder.create135Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                new Point( 11, 7 ) );

        // row 8
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 8 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 3, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.EAST ) ), 
                new Point( 4, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 5, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.of( Direction.SOUTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 6, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 7, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                new Point( 8, 8 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 8 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 8 ) );

        // row 9
        builder.addElement( builder.create24Pusher( Direction.of( Direction.WEST ), Direction.EAST ), 
                new Point( 0, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 1, 9 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 2, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.EAST ) ), 
                new Point( 3, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 4, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 5, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.of( Direction.NORTH ), Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 6, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 7, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 8, 9 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 9 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.NORTH ) ), 
                new Point( 10, 9 ) );
        builder.addElement( builder.create24Pusher( Direction.of( Direction.EAST ), Direction.WEST ), 
                new Point( 11, 9 ) );
        
        // row 10
        builder.addElement( builder.createDualSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 0, 10 ) );
        builder.addElement( builder.createDualSpeedConveyor( Direction.NORTH, Direction.of( Direction.WEST ) ), 
                new Point( 1, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.EAST ) ), 
                new Point( 2, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 3, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 4, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 5, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST, Direction.SOUTH ) ), 
                new Point( 6, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 7, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 8, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 9, 10 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH, Direction.SOUTH ) ), 
                new Point( 10, 10 ) );

        // row 11
        builder.addElement( builder.createBasicElement( BasicElementType.REPAIR ), 
                new Point( 0, 11 ) );
        builder.addElement( builder.create24Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                new Point( 2, 11 ) );
        builder.addElement( builder.create135Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                new Point( 4, 11 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 6, 11 ) );
        builder.addElement( builder.create135Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                new Point( 7, 11 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.OPTION ), 
                new Point( 8, 11 ) );
        builder.addElement( builder.create24Pusher( Direction.of( Direction.SOUTH ), Direction.NORTH ), 
                new Point( 9, 11 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.NORTH, Direction.of( Direction.SOUTH ) ), 
                new Point( 10, 11 ) );
    }

    /**
   	 * Adds the elements for the first starting board to the builder, given the x and yOffsets
   	 * TODO: Support turning?
   	 * 
   	 * @param builder The builder to add the elements to
   	 * @param xOffset The x-Offset
   	 * @param yOffset The y-Offset
     *  
     */
   private static void addFirstStartingBoard( GameBuilder builder, int xOffset, int yOffset ) 
    {
	   builder.setBoardVariables( xOffset, yOffset, 0 );

	   // row 0
       builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                           new Point( 2, 0 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                           new Point( 4, 0 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                           new Point( 7, 0 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                           new Point( 9, 0 ) );
       // 2
       builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_7 ), 
                           new Point( 0, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_5 ), 
                           new Point( 1, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                           new Point( 2, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_3 ), 
                           new Point( 3, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                           new Point( 4, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_1 ), 
                           new Point( 5, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_2 ), 
                           new Point( 6, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                           new Point( 7, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_4 ), 
                           new Point( 8, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                           new Point( 9, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_6 ), 
                           new Point( 10, 2 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_8 ), 
                           new Point( 11, 2 ) );

       // row 3
       builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                           new Point( 2, 3 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                           new Point( 4, 3 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                           new Point( 7, 3 ) );
       builder.addElement( builder.createBasicElement( Direction.of( Direction.SOUTH ) ), 
                           new Point( 9, 3 ) );
    }

   	/**
   	 * Adds the elements for the second starting board to the builder, given the x and yOffsets
   	 * TODO: Support turning?
   	 * 
   	 * @param builder The builder to add the elements to
   	 * @param xOffset The x-Offset
   	 * @param yOffset The y-Offset
   	 */
    private static void addSecondStartingBoard( GameBuilder builder, int xOffset, int yOffset ) 
    {
 	   builder.setBoardVariables( xOffset, yOffset, 0 );

    	// row 0
        builder.addElement( builder.createBasicElement( BasicElementType.STARTING_7 ), 
                new Point( 0, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 2, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 3, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH, Direction.WEST ) ), 
                new Point( 4, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH, Direction.EAST ) ), 
                new Point( 7, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 8, 0 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.NORTH ) ), 
                new Point( 9, 0 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.STARTING_8 ), 
                new Point( 11, 0 ) );

        // row 1
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 0, 1 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_5 ), 
                new Point( 1, 1 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 2, 1 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 9, 1 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST, Direction.EAST ), BasicElementType.STARTING_6 ), 
                new Point( 10, 1 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 11, 1 ) );

        // row 2
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 0, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 1, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.WEST ) ), 
                new Point( 2, 2 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.STARTING_3 ), 
                new Point( 3, 2 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ) ), 
                new Point( 5, 2 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ) ), 
                new Point( 6, 2 ) );
        builder.addElement( builder.createBasicElement( BasicElementType.STARTING_4 ), 
                new Point( 8, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.SOUTH, Direction.of( Direction.EAST ) ), 
                new Point( 9, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 10, 2 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 11, 2 ) );

        // row 3
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.NORTH ) ), 
                new Point( 2, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 3, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.EAST, Direction.of( Direction.WEST ) ), 
                new Point( 4, 3 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.EAST ), BasicElementType.STARTING_1 ), 
                new Point( 5, 3 ) );
        builder.addElement( builder.createBasicElement( Direction.of( Direction.WEST ), BasicElementType.STARTING_2 ), 
                new Point( 6, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 7, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.EAST ) ), 
                new Point( 8, 3 ) );
        builder.addElement( builder.createSingleSpeedConveyor( Direction.WEST, Direction.of( Direction.NORTH ) ), 
                new Point( 9, 3 ) );
    }

    public static Game getTestScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 24, 52 );
        addFirstStartingBoard( builder,  0,  48 );
        addSecondStartingBoard( builder, 12, 48 );
        
        addMaelstrom( builder, 0,  36, 0 );
        addSpinZone( builder,  12, 36, 0 );
        addChess( builder,     0,  24, 0 );
        addIsland( builder,    12, 24, 0 );
        addChopShop( builder,  0,  12, 0 );
        addCross( builder,     12, 12, 0 );
        addExchange( builder,  0,  0,  0 );
        addVault( builder,     12, 0,  0 );
        
        builder.addFlag( 4, 5, 1 );

        return builder.asGame();
    }

    public static Game getMovingTargetsScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addMaelstrom( builder, 0, 0, 1 );

        builder.addFlag( 1, 0, 1 );
        builder.addFlag( 10, 11, 2 );
        builder.addFlag( 11, 5, 3 );
        builder.addFlag( 0, 6, 4 );
        
        return builder.asGame();
    }

    public static Game getCheckmateScenario() 
    {

        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addChess( builder, 0, 0, 1 );

        builder.addFlag( 7, 2, 1 );
        builder.addFlag( 3, 8, 2 );
        
        return builder.asGame();
    }

    public static Game getRiskyExchangeScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder,  0,  12 );
        addExchange( builder, 0, 0, 2 );

        builder.addFlag( 7, 1, 1 );
        builder.addFlag( 9, 7, 2 );
        builder.addFlag( 1, 4, 3 );
        
        return builder.asGame();
    }

    public static Game getDizzyDashScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder,  0,  12 );
        addSpinZone( builder, 0, 0, 0 );

        builder.addFlag( 5, 4, 1 );
        builder.addFlag( 10, 11, 2 );
        builder.addFlag( 1, 6, 3 );
        
        return builder.asGame();
    }

    public static Game getIslandHopScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addIsland( builder, 0, 0, 1 );

        builder.addFlag( 6, 1, 1 );
        builder.addFlag( 1, 6, 2 );
        builder.addFlag( 11, 4, 3 );
        
        return builder.asGame();
    }

    public static Game getChopShopChallengeScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addChopShop( builder, 0, 0, -1 );

        builder.addFlag( 4, 9, 1 );
        builder.addFlag( 9, 11, 2 );
        builder.addFlag( 1, 10, 3 );
        builder.addFlag( 11, 7, 4 );
        
        return builder.asGame();
    }

    public static Game getTwisterScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder,  0,  12 );
        addSpinZone( builder, 0, 0, 2 );

        builder.addFlag( 2, 9, 1 );
        builder.addFlag( 3, 2, 2 );
        builder.addFlag( 9, 2, 3 );
        builder.addFlag( 8, 9, 4 );
        
        return builder.asGame();
    }

    public static Game getBloodbathChessScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addChess( builder, 0, 0, 1 );

        builder.addFlag( 6, 5, 1 );
        builder.addFlag( 2, 9, 2 );
        builder.addFlag( 8, 7, 3 );
        builder.addFlag( 3, 4, 4 );
        
        return builder.asGame();
    }

    public static Game getAroundTheWorldScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 28 );
        addSecondStartingBoard( builder,  0,  24 );
        addSpinZone( builder, 0, 12, -1 );
        addIsland( builder, 0, 0, -1 );

        builder.addFlag( 9, 12, 1 );
        builder.addFlag( 6, 1, 2 );
        builder.addFlag( 5, 22, 3 );
        
        return builder.asGame();
    }

    public static Game getDeathTrapScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addIsland( builder, 0, 0, 1 );

        builder.addFlag( 7, 7, 1 );
        builder.addFlag( 0, 4, 2 );
        builder.addFlag( 6, 5, 3 );
        
        return builder.asGame();
    }

    public static Game getPilgrimageScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 28 );
        addFirstStartingBoard( builder,  0,  24 );
        addExchange( builder, 0, 12, 0 );
        addCross( builder, 0, 0, -1 );

        builder.addFlag( 4, 15, 1 );
        builder.addFlag( 9, 26, 2 );
        builder.addFlag( 2, 5, 3 );
        
        return builder.asGame();
    }

    public static Game getVaultAssaultScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder, 0, 12 );
        addVault( builder, 0, 0, 0 );

        builder.addFlag( 6, 3, 1 );
        builder.addFlag( 4, 11, 2 );
        builder.addFlag( 8, 5, 3 );
        
        return builder.asGame();
    }

    public static Game getWhirlwindTourScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addMaelstrom( builder, 0, 0, 1 );

        builder.addFlag( 8, 0, 1 );
        builder.addFlag( 3, 11, 2 );
        builder.addFlag( 11, 6, 3 );
        
        return builder.asGame();
    }

    public static Game getLostBearingsScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder,  0,  12 );
        addIsland( builder, 0, 0, -1 );

        builder.addFlag( 1, 2, 1 );
        builder.addFlag( 10, 9, 2 );
        builder.addFlag( 2, 8, 3 );
        
        return builder.asGame();
    }

    public static Game getRobotStewScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder,  0,  12 );
        addChopShop( builder, 0, 0, 1 );

        builder.addFlag( 0, 4, 1 );
        builder.addFlag( 9, 7, 2 );
        builder.addFlag( 2, 10, 3 );
        
        return builder.asGame();
    }

    public static Game getOddestSeaScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 28 );
        addFirstStartingBoard( builder,  0,  24 );
        addMaelstrom( builder, 0, 12, -1 );
        addVault( builder, 0, 0, 1 );

        builder.addFlag( 8, 6, 1 );
        builder.addFlag( 1, 3, 2 );
        builder.addFlag( 5, 8, 3 );
        builder.addFlag( 9, 2, 4 );
        
        return builder.asGame();
    }
    public static Game getAgainstTheGrainScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 28 );
        addFirstStartingBoard( builder,  0,  24 );
        addChess( builder, 0, 12, 0 );
        addChopShop( builder, 0, 0, 1 );

        builder.addFlag( 10, 9, 1 );
        builder.addFlag( 3, 3, 2 );
        builder.addFlag( 5, 17, 3 );
        
        return builder.asGame();
    }

    public static Game getIslandKingScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addFirstStartingBoard( builder,  0,  12 );
        addIsland( builder, 0, 0, -1 );

        builder.addFlag( 5, 4, 1 );
        builder.addFlag( 7, 7, 2 );
        builder.addFlag( 5, 6, 3 );
        
        return builder.asGame();
    }

    public static Game getTricksyScenario() 
    {
        GameBuilder builder = new GameBuilder();
        builder.initializeBoard( 12, 16 );
        addSecondStartingBoard( builder,  0,  12 );
        addCross( builder, 0, 0, -1 );

        builder.addFlag( 9, 1, 1 );
        builder.addFlag( 0, 1, 2 );
        builder.addFlag( 8, 11, 3 );
        builder.addFlag( 3, 7, 4 );
        
        return builder.asGame();
    }

    /**
     * should call the function get<scenarioName>Scenario
     * @param scenarioName
     * @param nrOfBots     
     * 
     * @return 
     */
    public static Game getScenario( ImplementedScenario scenarioName, int nrOfBots ) 
    { 
        try {
            Method scenarioMethod = GameFactory.class.getDeclaredMethod("get" + scenarioName + "Scenario", (Class[]) null);
            return (Game) scenarioMethod.invoke(null, (Object[]) null);
        } catch (NoSuchMethodException e) {
            logger.warn("Couldn't find scenario with name " + scenarioName, e);
        } catch (IllegalAccessException e) {
            logger.warn("Not allowed to access the creation method for scenario with name " + scenarioName, e);
        } catch (InvocationTargetException e) {
            logger.warn("Unknown exception throw by the creation method for scenario with name " + scenarioName, e);
        }
        return getMovingTargetsScenario();
    }

	public static BufferedImage getPreviewImage( ImplementedScenario scenarioName, int nrOfBots, int factor ) 
	{
	    Game scenario = getScenario( scenarioName, nrOfBots );
	    return scenario.getImage(factor);
	}
}
