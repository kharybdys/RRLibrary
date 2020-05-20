package kharybdys.roborally.game.board;

import java.awt.Graphics;

/**
 *
 * @author MHK
 */
public class Board {

    private int xOffset;
    private int yOffset;
    private BoardElement[][] board;

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public int getxSize()
    {
        return board.length;
    }

    public int getySize()
    {
        BoardElement[] row = board[0];
        return row.length;
    }

    public BoardElement getElement(int x, int y) {
        return board[x][y];
    }

    public Board(int xOffset, int yOffset, BoardElement[][] board)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.board = board;
    }

    public void paint(Graphics g, int ySizePanel, int factor) {
        for (BoardElement[] row : board) {
            for (BoardElement element : row) {
                element.paint(g, xOffset, yOffset, ySizePanel, factor);
            }
        }
    }
}
