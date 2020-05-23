package kharybdys.roborally.game;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.*;
import kharybdys.roborally.game.definition.Direction;

/**
 * Factory with helper methods that creates a Game object
 */
public class GameFactory {

	private static final Logger logger = LoggerFactory.getLogger( GameFactory.class );
    private static final int STANDARD_BOARD_SIZE = 12;
    private static final int START_BOARD_YSIZE = 4;
    /*
     * Helper methods
     */
    @SuppressWarnings("unused")
	private static Map<Direction, Integer> getMapNorthDirs(int number) {
        Map<Direction, Integer> walls = new HashMap<Direction, Integer>();
        walls.put(Direction.NORTH, number);
        return walls;
    }

    @SuppressWarnings("unused")
    private static Map<Direction, Integer> getMapSouthDirs(int number) {
        Map<Direction, Integer> walls = new HashMap<Direction, Integer>();
        walls.put(Direction.SOUTH, number);
        return walls;
    }

    private static Map<Direction, Integer> getMapEastDirs(int number) {
        Map<Direction, Integer> walls = new HashMap<Direction, Integer>();
        walls.put(Direction.EAST, number);
        return walls;
    }

    @SuppressWarnings("unused")
    private static Map<Direction, Integer> getMapWestDirs(int number) {
        Map<Direction, Integer> walls = new HashMap<Direction, Integer>();
        walls.put(Direction.WEST, number);
        return walls;
    }

    private static List<Direction> getNorthDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.NORTH);
        return walls;
    }

    private static List<Direction> getSouthDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.SOUTH);
        return walls;
    }

    private static List<Direction> getEastDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.EAST);
        return walls;
    }

    private static List<Direction> getWestDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        return walls;
    }

    private static List<Direction> getNorthWestDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        walls.add(Direction.NORTH);
        return walls;
    }

    private static List<Direction> getSouthWestDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        walls.add(Direction.SOUTH);
        return walls;
    }

    private static List<Direction> getEastWestDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        walls.add(Direction.EAST);
        return walls;
    }

    private static List<Direction> getNorthEastDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.EAST);
        walls.add(Direction.NORTH);
        return walls;
    }

    private static List<Direction> getNorthSouthDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.SOUTH);
        walls.add(Direction.NORTH);
        return walls;
    }

    private static List<Direction> getSouthEastDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.EAST);
        walls.add(Direction.SOUTH);
        return walls;
    }

    @SuppressWarnings("unused")
    private static List<Direction> getNorthWestEastDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        walls.add(Direction.NORTH);
        walls.add(Direction.EAST);
        return walls;
    }

    @SuppressWarnings("unused")
    private static List<Direction> getNorthWestSouthDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        walls.add(Direction.NORTH);
        walls.add(Direction.SOUTH);
        return walls;
    }

    @SuppressWarnings("unused")
    private static List<Direction> getSouthWestEastDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.WEST);
        walls.add(Direction.SOUTH);
        walls.add(Direction.EAST);
        return walls;
    }

    @SuppressWarnings("unused")
    private static List<Direction> getNorthEastSouthDirs() {
        List<Direction> walls = new ArrayList<Direction>();
        walls.add(Direction.SOUTH);
        walls.add(Direction.NORTH);
        walls.add(Direction.EAST);
        return walls;
    }

    private static BoardElement[][] prepareStandardBoard() 
    {
        BoardElement[][] board = new BoardElement[STANDARD_BOARD_SIZE][STANDARD_BOARD_SIZE];
        for (int i = 0; i < STANDARD_BOARD_SIZE; i++) {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) {
                board[j][i] = null;
            }
        }
        return board;
    }

    private static BoardElement[][] fillOutStandardBoard(BoardElement[][] board) 
    {
        for (int i = 0; i < STANDARD_BOARD_SIZE; i++) {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) {
                if (board[j][i] == null) {
                    board[j][i] = new BasicElement(j, i, null  );
                }
            }
        }
        return board;
    }

    private static BoardElement[][] turnBoard( int turnSteps, BoardElement[][] oldBoard ) 
    {   // turnSteps determines rotation of this board. Normal is that the name of the board is in the lowerleft or lowerright corner.
        BoardElement[][] newBoard = prepareStandardBoard();
        int newX = 0;
        int newY = 0;
        AffineTransform transformation = AffineTransform.getQuadrantRotateInstance( -1 * turnSteps, 
        		                                                                    ( STANDARD_BOARD_SIZE - 1 ) / 2.0, 
        		                                                                    ( STANDARD_BOARD_SIZE - 1 ) / 2.0
        		                                                                  );

        for (int i = 0; i < STANDARD_BOARD_SIZE; i++) 
        {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) 
            {

                Point2D newP = new Point(0, 0);
                newP = transformation.transform( new Point( i, j ), new Point( 0, 0 ) );
                newX = (int) newP.getX();
                newY = (int) newP.getY();
                newBoard[newX][newY] = oldBoard[i][j].turn( turnSteps, newX, newY );
            }
        }
        return newBoard;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the Exchange board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getExchangeElements(BoardElement[][] board) 
    {
        board[1][0]   = new SingleSpeedConveyor(     1,  0, null,                 getNorthDirs(), Direction.SOUTH );
        board[2][0]   = new BasicElement(            2,  0, getSouthDirs()        );
        board[3][0]   = new SingleSpeedConveyor(     3,  0, null,                 getSouthDirs(), Direction.NORTH );
        board[4][0]   = new BasicElement(            4,  0, getSouthDirs()        );
        board[5][0]   = new SingleSpeedConveyor(     5,  0, null,                 getNorthDirs(), Direction.SOUTH );
        board[6][0]   = new SingleSpeedConveyor(     6,  0, null,                 getSouthDirs(), Direction.NORTH );
        board[7][0]   = new BasicElement(            7,  0, getSouthDirs()        );
        board[8][0]   = new SingleSpeedConveyor(     8,  0, null,                 getNorthDirs(), Direction.SOUTH );
        board[9][0]   = new BasicElement(            9,  0, getSouthDirs()        );
        board[11][0]  = new BasicElement(            11, 0, null,                 0, BasicElementType.REPAIR );
        board[0][1]   = new SingleSpeedConveyor(     0,  1, null,                 getWestDirs(), Direction.EAST );
        board[1][1]   = new RotatorClockwise(        1,  1, null                  );
        board[3][1]   = new SingleSpeedConveyor(     3,  1, null,                 getSouthDirs(), Direction.NORTH );
        board[5][1]   = new SingleSpeedConveyor(     5,  1, null,                 getNorthDirs(), Direction.SOUTH );
        board[6][1]   = new SingleSpeedConveyor(     6,  1, null,                 getSouthDirs(), Direction.NORTH );
        board[8][1]   = new SingleSpeedConveyor(     8,  1, null,                 getNorthDirs(), Direction.SOUTH );
        board[9][1]   = new BasicElement(            9,  1, null,                 0, BasicElementType.HOLE );
        board[11][1]  = new SingleSpeedConveyor(     11, 1, null,                 getWestDirs(), Direction.EAST );
        board[0][2]   = new BasicElement(            0,  2, getWestDirs()         ); // TODO: single laser mount west
        board[2][2]   = new BasicElement(            2,  2, getEastDirs()         );
        board[3][2]   = new SingleSpeedConveyor(     3,  2, getWestDirs(),        getSouthDirs(), Direction.NORTH );
        board[5][2]   = new SingleSpeedConveyor(     5,  2, null,                 getNorthDirs(), Direction.SOUTH );
        board[6][2]   = new SingleSpeedConveyor(     6,  2, null,                 getSouthDirs(), Direction.NORTH );
        board[8][2]   = new SingleSpeedConveyor(     8,  2, null,                 getNorthDirs(), Direction.SOUTH );
        board[11][2]  = new BasicElement(            11, 2, getEastDirs()         );
        board[0][3]   = new DualSpeedConveyor(       0,  3, null,                 getEastDirs(), Direction.WEST );
        board[1][3]   = new DualSpeedConveyor(       1,  3, null,                 getEastDirs(), Direction.WEST );
        board[2][3]   = new DualSpeedConveyor(       2,  3, null,                 getEastDirs(), Direction.WEST );
        board[3][3]   = new SingleSpeedConveyor(     3,  3, null,                 getSouthDirs(), Direction.NORTH );
        board[5][3]   = new SingleSpeedConveyor(     5,  3, null,                 getNorthDirs(), Direction.SOUTH );
        board[6][3]   = new SingleSpeedConveyor(     6,  3, null,                 getSouthDirs(), Direction.NORTH );
        board[8][3]   = new RotatorCounterClockwise( 8,  3, null                  );
        board[9][3]   = new SingleSpeedConveyor(     9,  3, null,                 getEastDirs(), Direction.WEST );
        board[10][3]  = new SingleSpeedConveyor(     10, 3, null,                 getEastDirs(), Direction.WEST );
        board[11][3]  = new SingleSpeedConveyor(     11, 3, null,                 getEastDirs(), Direction.WEST );
        board[0][4]   = new BasicElement(            0,  4, getWestDirs()         );
        board[4][4]   = new BasicElement(            4,  4, getNorthEastDirs()    );
        board[5][4]   = new SingleSpeedConveyor(     5,  4, getWestDirs(),        getNorthDirs(), Direction.SOUTH );
        board[6][4]   = new SingleSpeedConveyor(     6,  4, getEastDirs(),        getSouthDirs(), Direction.NORTH );
        board[7][4]   = new BasicElement(            7,  4, getNorthWestDirs()    );
        board[11][4]  = new BasicElement(            11, 4, getEastDirs()         );
        board[0][5]   = new SingleSpeedConveyor(     0,  5, null,                 getWestDirs(), Direction.EAST );
        board[1][5]   = new SingleSpeedConveyor(     1,  5, null,                 getWestDirs(), Direction.EAST );
        board[2][5]   = new SingleSpeedConveyor(     2,  5, null,                 getWestDirs(), Direction.EAST );
        board[3][5]   = new SingleSpeedConveyor(     3,  5, null,                 getWestDirs(), Direction.EAST );
        board[4][5]   = new SingleSpeedConveyor(     4,  5, getSouthDirs(),       getWestDirs(), Direction.EAST );
        board[7][5]   = new SingleSpeedConveyor(     7,  5, getSouthDirs(),       getWestDirs(), Direction.EAST );
        board[8][5]   = new SingleSpeedConveyor(     8,  5, null,                 getWestDirs(), Direction.EAST );
        board[9][5]   = new SingleSpeedConveyor(     9,  5, null,                 getWestDirs(), Direction.EAST );
        board[10][5]  = new SingleSpeedConveyor(     10, 5, null,                 getWestDirs(), Direction.EAST );
        board[11][5]  = new SingleSpeedConveyor(     11, 5, null,                 getWestDirs(), Direction.EAST );
        board[0][6]   = new DualSpeedConveyor(       0,  6, null,                 getEastDirs(), Direction.WEST );
        board[1][6]   = new DualSpeedConveyor(       1,  6, null,                 getEastDirs(), Direction.WEST );
        board[2][6]   = new DualSpeedConveyor(       2,  6, null,                 getEastDirs(), Direction.WEST );
        board[3][6]   = new DualSpeedConveyor(       3,  6, null,                 getEastDirs(), Direction.WEST );
        board[4][6]   = new DualSpeedConveyor(       4,  6, getNorthDirs(),       getEastDirs(), Direction.WEST );
        board[7][6]   = new SingleSpeedConveyor(     7,  6, getNorthDirs(),       getEastDirs(), Direction.WEST );
        board[8][6]   = new SingleSpeedConveyor(     8,  6, null,                 getEastDirs(), Direction.WEST );
        board[9][6]   = new SingleSpeedConveyor(     9,  6, null,                 getEastDirs(), Direction.WEST );
        board[10][6]  = new SingleSpeedConveyor(     10, 6, null,                 getEastDirs(), Direction.WEST );
        board[0][7]   = new BasicElement(            0,  7, getWestDirs()         );
        board[4][7]   = new BasicElement(            4,  7, getSouthEastDirs(),   0, BasicElementType.OPTION );
        board[5][7]   = new SingleSpeedConveyor(     5,  7, getWestDirs(),        getNorthDirs(), Direction.SOUTH );
        board[6][7]   = new DualSpeedConveyor(       6,  7, getEastDirs(),        getSouthDirs(), Direction.NORTH );
        board[7][7]   = new BasicElement(            7,  7, getSouthWestDirs()    );
        board[11][7]  = new BasicElement(            11, 7, getEastDirs()         );
        board[0][8]   = new SingleSpeedConveyor(     0,  8, null,                 getWestDirs(), Direction.EAST );
        board[1][8]   = new SingleSpeedConveyor(     1,  8, getNorthDirs(),       getWestDirs(), Direction.EAST );
        board[2][8]   = new SingleSpeedConveyor(     2,  8, null,                 getWestDirs(), Direction.EAST );
        board[3][8]   = new RotatorCounterClockwise( 3,  8, null                  );
        board[5][8]   = new SingleSpeedConveyor(     5,  8, null,                 getNorthDirs(), Direction.SOUTH );
        board[6][8]   = new DualSpeedConveyor(       6,  8, null,                 getSouthDirs(), Direction.NORTH );
        board[8][8]   = new RotatorCounterClockwise( 8,  8, null                  );
        board[9][8]   = new SingleSpeedConveyor(     9,  8, null,                 getWestDirs(), Direction.EAST );
        board[10][8]  = new SingleSpeedConveyor(     10, 8, null,                 getWestDirs(), Direction.EAST );
        board[11][8]  = new SingleSpeedConveyor(     11, 8, null,                 getWestDirs(), Direction.EAST );
        board[0][9]   = new BasicElement(            0,  9, getWestDirs()         );
        board[1][9]   = new BasicElement(            1,  9, getSouthDirs()        );
        board[3][9]   = new SingleSpeedConveyor(     3,  9, null,                 getSouthDirs(), Direction.NORTH );
        board[5][9]   = new SingleSpeedConveyor(     5,  9, null,                 getNorthDirs(), Direction.SOUTH );
        board[6][9]   = new DualSpeedConveyor(       6,  9, null,                 getSouthDirs(), Direction.NORTH );
        board[8][9]   = new SingleSpeedConveyor(     8,  9, null,                 getNorthDirs(), Direction.SOUTH );
        board[11][9]  = new BasicElement(            11, 9, getEastDirs()         );
        board[0][10]  = new SingleSpeedConveyor(     0,  10, null,                getEastDirs(), Direction.WEST );
        board[1][10]  = new RotatorClockwise(        1,  10, null                 );
        board[3][10]  = new SingleSpeedConveyor(     3,  10, null,                getSouthDirs(), Direction.NORTH );
        board[5][10]  = new SingleSpeedConveyor(     5,  10, null,                getNorthDirs(), Direction.SOUTH );
        board[6][10]  = new DualSpeedConveyor(       6,  10, null,                getSouthDirs(), Direction.NORTH );
        board[8][10]  = new SingleSpeedConveyor(     8,  10, null,                getNorthDirs(), Direction.SOUTH );
        board[10][10] = new BasicElement(            10, 10, getNorthDirs()       );
        board[11][10] = new BasicElement(            11, 10, null,                0, BasicElementType.HOLE );
        board[0][11]  = new BasicElement(            0,  11, null,                0, BasicElementType.REPAIR );
        board[1][11]  = new SingleSpeedConveyor(     1,  11, null,                getNorthDirs(), Direction.SOUTH );
        board[2][11]  = new BasicElement(            2,  11, getNorthDirs()       );
        board[3][11]  = new SingleSpeedConveyor(     3,  11, null,                getSouthDirs(), Direction.NORTH );
        board[4][11]  = new BasicElement(            4,  11, getNorthDirs()       );
        board[6][11]  = new DualSpeedConveyor(       6,  11, null,                getSouthDirs(), Direction.NORTH );
        board[7][11]  = new BasicElement(            7,  11, getNorthDirs()       );
        board[8][11]  = new SingleSpeedConveyor(     8,  11, null,                getNorthDirs(), Direction.SOUTH );
        board[9][11]  = new BasicElement(            9,  11, getNorthDirs()       );
        board[10][11] = new BasicElement(            10, 11, getSouthDirs()       );
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the Island board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getIslandElements(BoardElement[][] board) 
    {
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
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the ChopShop board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getChopShopElements(BoardElement[][] board) 
    {
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
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the Chess board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getChessElements(BoardElement[][] board) 
    {
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
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the Cross board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getCrossElements(BoardElement[][] board) 
    {
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
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the Vault board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getVaultElements(BoardElement[][] board) 
    {
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
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the SpinZone board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getSpinZoneElements(BoardElement[][] board) 
    {
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
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the Maelstrom board.
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getMaelstromElements(BoardElement[][] board) 
    {
        board[0][0]   = new BasicElement(            0,  0,  null,                0, BasicElementType.REPAIR);
        board[2][0]   = new Pusher24(                2,  0,  getSouthDirs(),      Direction.NORTH);
        board[4][0]   = new Pusher135(               4,  0,  getSouthDirs(),      Direction.NORTH);
        board[6][0]   = new SingleSpeedConveyor(     6,  0,  null,                getSouthDirs(), Direction.NORTH);
        board[7][0]   = new Pusher135(               7,  0,  getSouthDirs(),      Direction.NORTH);
        board[8][0]   = new BasicElement(            8,  0,  null,                0, BasicElementType.OPTION);
        board[9][0]   = new Pusher24(                9,  0,  getSouthDirs(),      Direction.NORTH);
        board[10][0]  = new SingleSpeedConveyor(     10, 0,  null,                getSouthDirs(), Direction.NORTH);
        board[0][1]   = new DualSpeedConveyor(       0,  1,  null,                getWestDirs(), Direction.EAST);
        board[1][1]   = new DualSpeedConveyor(       1,  1,  null,                getWestDirs(), Direction.NORTH);
        board[2][1]   = new SingleSpeedConveyor(     2,  1,  null,                getEastDirs(), Direction.NORTH);
        board[3][1]   = new SingleSpeedConveyor(     3,  1,  null,                getEastDirs(), Direction.WEST);
        board[4][1]   = new SingleSpeedConveyor(     4,  1,  null,                getEastDirs(), Direction.WEST);
        board[5][1]   = new SingleSpeedConveyor(     5,  1,  null,                getEastDirs(), Direction.WEST);
        board[6][1]   = new SingleSpeedConveyor(     6,  1,  null,                getSouthEastDirs(), Direction.WEST);
        board[7][1]   = new SingleSpeedConveyor(     7,  1,  null,                getEastDirs(), Direction.WEST);
        board[8][1]   = new SingleSpeedConveyor(     8,  1,  null,                getEastDirs(), Direction.WEST);
        board[9][1]   = new SingleSpeedConveyor(     9,  1,  null,                getEastDirs(), Direction.WEST);
        board[10][1]  = new SingleSpeedConveyor(     10, 1,  null,                getNorthSouthDirs(), Direction.WEST);
        board[0][2]   = new Pusher24(                0,  2,  getWestDirs(),       Direction.EAST);
        board[1][2]   = new DualSpeedConveyor(       1,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[2][2]   = new SingleSpeedConveyor(     2,  2,  null,                getSouthDirs(), Direction.NORTH);
        board[3][2]   = new DualSpeedConveyor(       3,  2,  null,                getEastDirs(), Direction.NORTH);
        board[4][2]   = new DualSpeedConveyor(       4,  2,  null,                getEastDirs(), Direction.WEST);
        board[5][2]   = new DualSpeedConveyor(       5,  2,  null,                getEastDirs(), Direction.WEST);
        board[6][2]   = new DualSpeedConveyor(       6,  2,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[7][2]   = new DualSpeedConveyor(       7,  2,  null,                getEastDirs(), Direction.WEST);
        board[8][2]   = new DualSpeedConveyor(       8,  2,  null,                getEastDirs(), Direction.WEST);
        board[9][2]   = new DualSpeedConveyor(       9,  2,  null,                getNorthDirs(), Direction.WEST);
        board[10][2]  = new SingleSpeedConveyor(     10, 2,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][2]  = new Pusher24(                11, 2,  getEastDirs(),       Direction.WEST);
        board[1][3]   = new DualSpeedConveyor(       1,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[2][3]   = new SingleSpeedConveyor(     2,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[3][3]   = new DualSpeedConveyor(       3,  3,  null,                getSouthDirs(), Direction.NORTH);
        board[4][3]   = new SingleSpeedConveyor(     4,  3,  null,                getEastDirs(), Direction.NORTH);
        board[5][3]   = new SingleSpeedConveyor(     5,  3,  getNorthDirs(),      getEastDirs(), Direction.WEST);
        board[6][3]   = new SingleSpeedConveyor(     6,  3,  getSouthDirs(),      getEastDirs(), Direction.WEST);
        board[7][3]   = new SingleSpeedConveyor(     7,  3,  null,                getEastDirs(), Direction.WEST);
        board[8][3]   = new SingleSpeedConveyor(     8,  3,  null,                getNorthDirs(), Direction.WEST);
        board[9][3]   = new DualSpeedConveyor(       9,  3,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][3]  = new SingleSpeedConveyor(     10, 3,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][4]   = new Pusher135(               0,  4,  getWestDirs(),       Direction.EAST);
        board[1][4]   = new DualSpeedConveyor(       1,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[2][4]   = new SingleSpeedConveyor(     2,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[3][4]   = new DualSpeedConveyor(       3,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[4][4]   = new SingleSpeedConveyor(     4,  4,  null,                getSouthDirs(), Direction.NORTH);
        board[5][4]   = new DualSpeedConveyor(       5,  4,  getSouthDirs(),      getEastDirs(), Direction.NORTH);
        board[6][4]   = new DualSpeedConveyor(       6,  4,  null,                getEastDirs(), Direction.WEST);
        board[7][4]   = new DualSpeedConveyor(       7,  4,  null,                getNorthDirs(), Direction.WEST);
        board[8][4]   = new SingleSpeedConveyor(     8,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][4]   = new DualSpeedConveyor(       9,  4,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][4]  = new SingleSpeedConveyor(     10, 4,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][4]  = new Pusher135(               11, 4,  getEastDirs(),       Direction.WEST);
        board[0][5]   = new DualSpeedConveyor(       0,  5,  null,                getWestDirs(), Direction.EAST);
        board[1][5]   = new DualSpeedConveyor(       1,  5,  null,                getSouthWestDirs(), Direction.NORTH);
        board[2][5]   = new SingleSpeedConveyor(     2,  5,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[3][5]   = new DualSpeedConveyor(       3,  5,  getWestDirs(),       getSouthDirs(), Direction.NORTH); // Laser mount west, strength 1
        board[4][5]   = new SingleSpeedConveyor(     4,  5,  null,                getSouthDirs(), Direction.NORTH);
        board[5][5]   = new BasicElement(            5,  5,  null,                0, BasicElementType.HOLE);
        board[6][5]   = new BasicElement(            6,  5,  null,                0, BasicElementType.HOLE);
        board[7][5]   = new DualSpeedConveyor(       7,  5,  getEastDirs(),       getNorthDirs(), Direction.SOUTH);
        board[8][5]   = new SingleSpeedConveyor(     8,  5,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[9][5]   = new DualSpeedConveyor(       9,  5,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][5]  = new SingleSpeedConveyor(     10, 5,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][5]  = new SingleSpeedConveyor(     11, 5,  null,                getWestDirs(), Direction.EAST);
        board[1][6]   = new DualSpeedConveyor(       1,  6,  null,                getSouthDirs(), Direction.NORTH);
        board[2][6]   = new SingleSpeedConveyor(     2,  6,  null,                getSouthDirs(), Direction.NORTH);
        board[3][6]   = new DualSpeedConveyor(       3,  6,  getEastDirs(),       getSouthDirs(), Direction.NORTH);
        board[4][6]   = new SingleSpeedConveyor(     4,  6,  getWestDirs(),       getSouthDirs(), Direction.NORTH); // Laser mount west, strength 1
        board[5][6]   = new BasicElement(            5,  6,  null,                0, BasicElementType.HOLE);
        board[6][6]   = new BasicElement(            6,  6,  null,                0, BasicElementType.HOLE);
        board[7][6]   = new DualSpeedConveyor(       7,  6,  null,                getNorthDirs(), Direction.SOUTH);
        board[8][6]   = new SingleSpeedConveyor(     8,  6,  getEastDirs(),       getNorthDirs(), Direction.SOUTH);
        board[9][6]   = new DualSpeedConveyor(       9,  6,  getWestDirs(),       getNorthDirs(), Direction.SOUTH);
        board[10][6]  = new SingleSpeedConveyor(     10, 6,  null,                getNorthEastDirs(), Direction.SOUTH);
        board[11][6]  = new SingleSpeedConveyor(     11, 6,  null,                getEastDirs(), Direction.WEST);
        board[0][7]   = new Pusher135(               0,  7,  getWestDirs(),       Direction.EAST);
        board[1][7]   = new DualSpeedConveyor(       1,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[2][7]   = new SingleSpeedConveyor(     2,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[3][7]   = new DualSpeedConveyor(       3,  7,  null,                getSouthDirs(), Direction.NORTH);
        board[4][7]   = new SingleSpeedConveyor(     4,  7,  null,                getSouthDirs(), Direction.EAST);
        board[5][7]   = new SingleSpeedConveyor(     5,  7,  null,                getWestDirs(), Direction.EAST);
        board[6][7]   = new SingleSpeedConveyor(     6,  7,  getNorthDirs(),      getWestDirs(), Direction.SOUTH); // Laser mount north, strength 1
        board[7][7]   = new DualSpeedConveyor(       7,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[8][7]   = new SingleSpeedConveyor(     8,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][7]   = new DualSpeedConveyor(       9,  7,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][7]  = new SingleSpeedConveyor(     10, 7,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][7]  = new Pusher135(               11, 7,  getEastDirs(),       Direction.WEST);
        board[1][8]   = new DualSpeedConveyor(       1,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[2][8]   = new SingleSpeedConveyor(     2,  8,  null,                getSouthDirs(), Direction.NORTH);
        board[3][8]   = new DualSpeedConveyor(       3,  8,  null,                getSouthDirs(), Direction.EAST);
        board[4][8]   = new DualSpeedConveyor(       4,  8,  null,                getWestDirs(), Direction.EAST);
        board[5][8]   = new DualSpeedConveyor(       5,  8,  getNorthDirs(),      getWestDirs(), Direction.EAST); // Laser mount north, strength 1
        board[6][8]   = new DualSpeedConveyor(       6,  8,  getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[7][8]   = new DualSpeedConveyor(       7,  8,  null,                getWestDirs(), Direction.SOUTH);
        board[8][8]   = new SingleSpeedConveyor(     8,  8,  null,                getNorthDirs(), Direction.SOUTH);
        board[9][8]   = new DualSpeedConveyor(       9,  8,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][8]  = new SingleSpeedConveyor(     10, 8,  null,                getNorthDirs(), Direction.SOUTH);
        board[0][9]   = new Pusher24(                0,  9,  getWestDirs(),       Direction.EAST);
        board[1][9]   = new DualSpeedConveyor(       1,  9,  null,                getSouthDirs(), Direction.NORTH);
        board[2][9]   = new SingleSpeedConveyor(     2,  9,  null,                getSouthDirs(), Direction.EAST);
        board[3][9]   = new SingleSpeedConveyor(     3,  9,  null,                getWestDirs(), Direction.EAST);
        board[4][9]   = new SingleSpeedConveyor(     4,  9,  null,                getWestDirs(), Direction.EAST);
        board[5][9]   = new SingleSpeedConveyor(     5,  9,  getSouthDirs(),      getWestDirs(), Direction.EAST);
        board[6][9]   = new SingleSpeedConveyor(     6,  9,  null,                getWestDirs(), Direction.EAST);
        board[7][9]   = new SingleSpeedConveyor(     7,  9,  null,                getWestDirs(), Direction.EAST);
        board[8][9]   = new SingleSpeedConveyor(     8,  9,  null,                getWestDirs(), Direction.SOUTH);
        board[9][9]   = new DualSpeedConveyor(       9,  9,  null,                getNorthDirs(), Direction.SOUTH);
        board[10][9]  = new SingleSpeedConveyor(     10, 9,  null,                getNorthDirs(), Direction.SOUTH);
        board[11][9]  = new Pusher24(                11, 9,  getEastDirs(),       Direction.WEST);
        board[1][10]  = new DualSpeedConveyor(       1,  10, null,                getNorthSouthDirs(), Direction.EAST);
        board[2][10]  = new DualSpeedConveyor(       2,  10, null,                getWestDirs(), Direction.EAST);
        board[3][10]  = new DualSpeedConveyor(       3,  10, null,                getWestDirs(), Direction.EAST);
        board[4][10]  = new DualSpeedConveyor(       4,  10, null,                getWestDirs(), Direction.EAST);
        board[5][10]  = new DualSpeedConveyor(       5,  10, null,                getNorthWestDirs(), Direction.EAST);
        board[6][10]  = new DualSpeedConveyor(       6,  10, null,                getWestDirs(), Direction.EAST);
        board[7][10]  = new DualSpeedConveyor(       7,  10, null,                getWestDirs(), Direction.EAST);
        board[8][10]  = new DualSpeedConveyor(       8,  10, null,                getWestDirs(), Direction.EAST);
        board[9][10]  = new DualSpeedConveyor(       9,  10, null,                getWestDirs(), Direction.SOUTH);
        board[10][10] = new SingleSpeedConveyor(     10, 10, null,                getEastDirs(), Direction.SOUTH);
        board[11][10] = new SingleSpeedConveyor(     11, 10, null,                getEastDirs(), Direction.WEST);
        board[1][11]  = new DualSpeedConveyor(       1,  11, null,                getNorthDirs(), Direction.SOUTH);
        board[2][11]  = new Pusher24(                2,  11, getNorthDirs(),      Direction.SOUTH);
        board[3][11]  = new BasicElement(            3,  11, null,                0, BasicElementType.OPTION);
        board[4][11]  = new Pusher135(               4,  11, getNorthDirs(),      Direction.SOUTH);
        board[5][11]  = new DualSpeedConveyor(       5,  11, null,                getNorthDirs(), Direction.SOUTH);
        board[6][11]  = new SingleSpeedConveyor(     6,  11, null,                getSouthDirs(), Direction.NORTH);
        board[7][11]  = new Pusher135(               7,  11, getNorthDirs(),      Direction.SOUTH);
        board[9][11]  = new Pusher24(                9,  11, getNorthDirs(),      Direction.SOUTH);
        board[11][11] = new BasicElement(            11, 11, null,                0, BasicElementType.REPAIR);
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the starting board, version 1
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getFirstStartingBoard() 
    {
        BoardElement[][] board = new BoardElement[STANDARD_BOARD_SIZE][START_BOARD_YSIZE];
        for (int i = 0; i < START_BOARD_YSIZE; i++) {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) {
                board[j][i] = null;
            }
        }
        board[2][0]  = new BasicElement( 2,  0, getSouthDirs()     );
        board[4][0]  = new BasicElement( 4,  0, getSouthDirs()     );
        board[7][0]  = new BasicElement( 7,  0, getSouthDirs()     );
        board[9][0]  = new BasicElement( 9,  0, getSouthDirs()     );
        board[0][1]  = new BasicElement( 0,  1, getEastDirs(),     7, BasicElementType.STARTING);
        board[1][1]  = new BasicElement( 1,  1, getWestDirs(),     5, BasicElementType.STARTING);
        board[2][1]  = new BasicElement( 2,  1, getEastDirs()      );
        board[3][1]  = new BasicElement( 3,  1, getWestDirs(),     3, BasicElementType.STARTING);
        board[4][1]  = new BasicElement( 4,  1, getEastDirs()      );
        board[5][1]  = new BasicElement( 5,  1, getEastWestDirs(), 1, BasicElementType.STARTING);
        board[6][1]  = new BasicElement( 6,  1, getEastWestDirs(), 2, BasicElementType.STARTING);
        board[7][1]  = new BasicElement( 7,  1, getWestDirs()      );
        board[8][1]  = new BasicElement( 8,  1, getEastDirs(),     4, BasicElementType.STARTING);
        board[9][1]  = new BasicElement( 9,  1, getWestDirs()      );
        board[10][1] = new BasicElement( 10, 1, getEastDirs(),     6, BasicElementType.STARTING);
        board[11][1] = new BasicElement( 11, 1, getWestDirs(),     8, BasicElementType.STARTING);
        board[2][3]  = new BasicElement( 2,  3, getNorthDirs()     );
        board[4][3]  = new BasicElement( 4,  3, getNorthDirs()     );
        board[7][3]  = new BasicElement( 7,  3, getNorthDirs()     );
        board[9][3]  = new BasicElement( 9,  3, getNorthDirs()     );
        
        for (int i = 0; i < START_BOARD_YSIZE; i++) {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) {
                if (board[j][i] == null) {
                    board[j][i] = new BasicElement(j, i, null  );
                }
            }
        }
        return board;
    }

    /**
     * Generate a 2D BoardElement array containing the boardElements of the starting board, version 2
     * Ignores lasers
     *  
     * @param board The array to write this into
     * @return      The written array
     */
    private static BoardElement[][] getSecondStartingBoard() 
    {
        BoardElement[][] board = new BoardElement[STANDARD_BOARD_SIZE][START_BOARD_YSIZE];
        for (int i = 0; i < START_BOARD_YSIZE; i++) {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) {
                board[j][i] = null;
            }
        }
        board[2][0]  = new SingleSpeedConveyor( 2,  0, null,               getNorthDirs(), Direction.EAST);
        board[3][0]  = new SingleSpeedConveyor( 3,  0, null,               getWestDirs(), Direction.EAST);
        board[4][0]  = new SingleSpeedConveyor( 4,  0, null,               getWestDirs(), Direction.EAST);
        board[5][0]  = new BasicElement(        5,  0, getEastDirs(),      1, BasicElementType.STARTING);
        board[6][0]  = new BasicElement(        6,  0, getWestDirs(),      2, BasicElementType.STARTING);
        board[7][0]  = new SingleSpeedConveyor( 7,  0, null,               getEastDirs(), Direction.WEST);
        board[8][0]  = new SingleSpeedConveyor( 8,  0, null,               getEastDirs(), Direction.WEST);
        board[9][0]  = new SingleSpeedConveyor( 9,  0, null,               getNorthDirs(), Direction.WEST);
        board[0][1]  = new SingleSpeedConveyor( 0,  1, null,               getWestDirs(), Direction.EAST);
        board[1][1]  = new SingleSpeedConveyor( 1,  1, null,               getWestDirs(), Direction.EAST);
        board[2][1]  = new SingleSpeedConveyor( 2,  1, null,               getWestDirs(), Direction.SOUTH);
        board[3][1]  = new BasicElement(        3,  1, null,               3, BasicElementType.STARTING);
        board[5][1]  = new BasicElement(        5,  1, getEastDirs()       );
        board[6][1]  = new BasicElement(        6,  1, getWestDirs()       );
        board[8][1]  = new BasicElement(        8,  1, null,               4, BasicElementType.STARTING);
        board[9][1]  = new SingleSpeedConveyor( 9,  1, null,               getEastDirs(), Direction.SOUTH);
        board[10][1] = new SingleSpeedConveyor( 10, 1, null,               getEastDirs(), Direction.WEST);
        board[11][1] = new SingleSpeedConveyor( 11, 1, null,               getEastDirs(), Direction.WEST);
        board[0][2]  = new BasicElement(        0,  2, getEastDirs()       );
        board[1][2]  = new BasicElement(        1,  2, getEastWestDirs(),  5, BasicElementType.STARTING);
        board[2][2]  = new BasicElement(        2,  2, getWestDirs()       );
        board[9][2]  = new BasicElement(        9,  2, getEastDirs()       );
        board[10][2] = new BasicElement(        10, 2, getEastWestDirs(),  6, BasicElementType.STARTING);
        board[11][2] = new BasicElement(        11, 2, getWestDirs()       );
        board[0][3]  = new BasicElement(        0,  3, null,               7, BasicElementType.STARTING);
        board[2][3]  = new BasicElement(        2,  3, getNorthDirs()      );
        board[3][3]  = new BasicElement(        3,  3, getEastDirs()       );
        board[4][3]  = new BasicElement(        4,  3, getNorthWestDirs()  );
        board[7][3]  = new BasicElement(        7,  3, getNorthEastDirs()  );
        board[8][3]  = new BasicElement(        8,  3, getWestDirs()       );
        board[9][3]  = new BasicElement(        9,  3, getNorthDirs()      );
        board[11][3] = new BasicElement(        11, 3, null,               8, BasicElementType.STARTING);
        
        for (int i = 0; i < START_BOARD_YSIZE; i++) {
            for (int j = 0; j < STANDARD_BOARD_SIZE; j++) {
                if (board[j][i] == null) {
                    board[j][i] = new BasicElement(j, i, null );
                }
            }
        }
        return board;
    }

    private static BoardElement[][] getMaelstrom() {
        return fillOutStandardBoard(getMaelstromElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getChopShop() {
        return fillOutStandardBoard(getChopShopElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getIsland() {
        return fillOutStandardBoard(getIslandElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getExchange() {
        return fillOutStandardBoard(getExchangeElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getChess() {
        return fillOutStandardBoard(getChessElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getVault() {
        return fillOutStandardBoard(getVaultElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getCross() {
        return fillOutStandardBoard(getCrossElements(prepareStandardBoard()));
    }

    private static BoardElement[][] getSpinZone() {
        return fillOutStandardBoard(getSpinZoneElements(prepareStandardBoard()));
    }

    public static Game getTestScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, getSecondStartingBoard()));
        boards.add(new Board(0, 12, getMaelstrom()));
        boards.add(new Board(0, 24, getSpinZone()));
        boards.add(new Board(0, 36, getChess()));
        boards.add(new Board(12, 12, getIsland()));
        boards.add(new Board(12, 24, getChopShop()));
        boards.add(new Board(12, 36, getCross()));
        boards.add(new Board(12, 48, getExchange()));
        boards.add(new Board(0, 48, getVault()));
        return new Game(STANDARD_BOARD_SIZE*2, 5 * STANDARD_BOARD_SIZE, boards);
    }

    public static List<Flag> getTestStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(4, 5, 1));
        return flags;
    }

    public static Game getMovingTargetsScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getMaelstrom())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getMovingTargetsStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(1, 15, 1));
        flags.add(Flag.getFlag(10, 4, 2));
        flags.add(Flag.getFlag(11, 10, 3));
        flags.add(Flag.getFlag(0, 9, 4));
        return flags;
    }

    public static Game getCheckmateScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getChess())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getCheckmateStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(7, 13, 1));
        flags.add(Flag.getFlag(3, 7, 2));
        return flags;
    }

    public static Game getRiskyExchangeScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getSecondStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(2, getExchange())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getRiskyExchangeStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(7, 14, 1));
        flags.add(Flag.getFlag(9, 8, 2));
        flags.add(Flag.getFlag(1, 11, 3));
        return flags;
    }

    public static Game getDizzyDashScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getSecondStartingBoard()));
        boards.add(new Board(0, 4, getSpinZone()));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getDizzyDashStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(5, 11, 1));
        flags.add(Flag.getFlag(10, 4, 2));
        flags.add(Flag.getFlag(1, 9, 3));
        return flags;
    }

    public static Game getIslandHopScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getIsland())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getIslandHopStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(6, 14, 1));
        flags.add(Flag.getFlag(1, 9, 2));
        flags.add(Flag.getFlag(11, 11, 3));
        return flags;
    }

    public static Game getChopShopChallengeScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(1, getChopShop())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getChopShopChallengeStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(4, 6, 1));
        flags.add(Flag.getFlag(9, 4, 2));
        flags.add(Flag.getFlag(1, 5, 3));
        flags.add(Flag.getFlag(11, 8, 4));
        return flags;
    }

    public static Game getTwisterScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getSecondStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(2, getSpinZone())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getTwisterStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(2, 6, 1));
        flags.add(Flag.getFlag(3, 13, 2));
        flags.add(Flag.getFlag(9, 13, 3));
        flags.add(Flag.getFlag(8, 6, 4));
        return flags;
    }

    public static Game getBloodbathChessScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getChess())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getBloodbathChessStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(6, 10, 1));
        flags.add(Flag.getFlag(2, 6, 2));
        flags.add(Flag.getFlag(8, 8, 3));
        flags.add(Flag.getFlag(3, 11, 4));
        return flags;
    }

    public static Game getAroundTheWorldScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(1, getSpinZone())));
        boards.add(new Board(0, 16, turnBoard(1, getIsland())));
        return new Game(STANDARD_BOARD_SIZE, 2*STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getAroundTheWorldStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(9, 15, 1));
        flags.add(Flag.getFlag(6, 26, 2));
        flags.add(Flag.getFlag(5, 5, 3));
        return flags;
    }

    public static Game getDeathTrapScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getIsland())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getDeathTrapStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(7, 8, 1));
        flags.add(Flag.getFlag(0, 11, 2));
        flags.add(Flag.getFlag(6, 10, 3));
        return flags;
    }

    public static Game getPilgrimageScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, getExchange()));
        boards.add(new Board(0, 16, turnBoard(1, getCross())));
        return new Game(STANDARD_BOARD_SIZE, 2*STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getPilgrimageStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(4, 19, 1));
        flags.add(Flag.getFlag(9, 8, 2));
        flags.add(Flag.getFlag(2, 13, 3));
        return flags;
    }

    public static Game getVaultAssaultScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getSecondStartingBoard()));
        boards.add(new Board(0, 4, getVault()));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getVaultAssaultStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(6, 12, 1));
        flags.add(Flag.getFlag(4, 4, 2));
        flags.add(Flag.getFlag(8, 10, 3));
        return flags;
    }

    public static Game getWhirlwindTourScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1,getMaelstrom())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getWhirlwindTourStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(8, 15, 1));
        flags.add(Flag.getFlag(3, 4, 2));
        flags.add(Flag.getFlag(11, 9, 3));
        return flags;
    }

    public static Game getLostBearingsScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getCross())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getLostBearingsStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(1, 13, 1));
        flags.add(Flag.getFlag(10, 6, 2));
        flags.add(Flag.getFlag(2, 7, 3));
        return flags;
    }

    public static Game getRobotStewScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getSecondStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(-1, getChopShop())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getRobotStewStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(0, 11, 1));
        flags.add(Flag.getFlag(9, 8, 2));
        flags.add(Flag.getFlag(2, 5, 3));
        return flags;
    }

    public static Game getOddestSeaScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(1, getMaelstrom())));
        boards.add(new Board(0, 16, turnBoard(-1, getVault())));
        return new Game(STANDARD_BOARD_SIZE, 2*STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getOddestSeaStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(8, 21, 1));
        flags.add(Flag.getFlag(1, 23, 2));
        flags.add(Flag.getFlag(5, 19, 3));
        flags.add(Flag.getFlag(9, 25, 4));
        return flags;
    }

    public static Game getAgainstTheGrainScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, getChess()));
        boards.add(new Board(0, 16, turnBoard(-1, getChopShop())));
        return new Game(STANDARD_BOARD_SIZE, 2*STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getAgainstTheGrainStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(10, 18, 1));
        flags.add(Flag.getFlag(3, 24, 2));
        flags.add(Flag.getFlag(5, 10, 3));
        return flags;
    }

    public static Game getIslandKingScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getFirstStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(1, getIsland())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getIslandKingStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(5, 11, 1));
        flags.add(Flag.getFlag(7, 8, 2));
        flags.add(Flag.getFlag(5, 9, 3));
        return flags;
    }

    public static Game getTricksyScenario() {
        List<Board> boards = new ArrayList<Board>();
        boards.add(new Board(0, 0, getSecondStartingBoard()));
        boards.add(new Board(0, 4, turnBoard(1, getCross())));
        return new Game(STANDARD_BOARD_SIZE, STANDARD_BOARD_SIZE + START_BOARD_YSIZE, boards);
    }

    public static List<Flag> getTricksyStandardFlags() {
        List<Flag> flags = new ArrayList<Flag>();
        flags.add(Flag.getFlag(9, 14, 1));
        flags.add(Flag.getFlag(0, 14, 2));
        flags.add(Flag.getFlag(8, 4, 3));
        flags.add(Flag.getFlag(3, 8, 4));
        return flags;
    }

    public static Game getScenario(ImplementedScenario scenarioName) { // should call the function get<scenarioName>Scenario
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

    public static List<Flag> getStandardFlags(ImplementedScenario scenarioName) { // should call the function get<scenarioName>StandardFlags
            try {
                Method standardFlagsMethod = GameFactory.class.getDeclaredMethod("get" + scenarioName + "StandardFlags", (Class[]) null);
                return (List<Flag>) standardFlagsMethod.invoke(null, (Object[]) null);
            } catch (NoSuchMethodException e) {
                logger.warn("Couldn't find scenario with name " + scenarioName, e);
            } catch (IllegalAccessException e) {
                logger.warn("Not allowed to access the creation method for scenario with name " + scenarioName, e);
            } catch (InvocationTargetException e) {
                logger.warn("Unknown exception throw by the creation method for scenario with name " + scenarioName, e);
            }
            return getMovingTargetsStandardFlags();
        }
}
