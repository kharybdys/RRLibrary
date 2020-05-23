package kharybdys.roborally.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.AbstractBoardElement;
import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.board.BasicElement;
import kharybdys.roborally.game.board.Board;
import kharybdys.roborally.game.board.BoardElement;
import kharybdys.roborally.game.board.BasicElementType;
import kharybdys.roborally.game.board.ImplementedScenario;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.util.Coordinates;

/**
 *
 * @author MHK
 */
public class Game {

    private List<Board> boards;
    private int xSize;
    private int ySize;
    private List<Flag> flags;
    private Map<Integer, Bot> bots;
    private Integer currentRound = 0;

	private static final Logger logger = LoggerFactory.getLogger( Game.class );

	public Game(int xSize, int ySize, List<Board> boards) {
        this.boards = boards;
        this.xSize = xSize;
        this.ySize = ySize;
        this.flags = new ArrayList<Flag>();
        this.bots = new HashMap<Integer, Bot>();
    }

    public Dimension getDimension(int factor)
    {
        return new Dimension(xSize*factor*AbstractBoardElement.baseSize, ySize*factor*AbstractBoardElement.baseSize);
    }

    public void setBotCoords(int botNumber, Coordinates coords)
    {
        Bot bot = bots.get(botNumber);
        if (bot != null) {
            bot.setCoords(coords);
        }
    }

    public Coordinates getBotCoords(int botNumber)
    {
        Bot bot = bots.get(botNumber);
        if (bot != null) {
            return bot.getCoords();
        } else {
            return null;
        }
    }

    public void setBotXCoord(int botNumber, int newX) {
        Bot bot = bots.get(botNumber);
        if (bot != null) {
            bot.setxCoord(newX);
        }
    }

    public int getBotXCoord(int botNumber) {
        Bot bot = bots.get(botNumber);
        if (bot != null) {
            return bot.getxCoord();
        } else {
            return -1;
        }
    }

    public void setBotYCoord(int botNumber, int newY) {
        Bot bot = bots.get(botNumber);
        if (bot != null) {
            bot.setyCoord(newY);
        }
    }

    public int getBotYCoord(int botNumber) {
        Bot bot = bots.get(botNumber);
        if (bot != null) {
            return bot.getyCoord();
        } else {
            return -1;
        }
    }

    public void setFlags(List<Flag> flags)
    {
        this.flags = flags;
    }

    public void setBots(List<Bot> bots)
    {
        for (Bot b : bots)
        {
            this.bots.put(b.getOrderNumber(), b);
        }
    }

    public boolean outOfYBounds(int y)
    {
        return y<0 || y>=ySize;
    }

    public boolean outOfXBounds(int x)
    {
        return x<0 || x>=xSize;
    }

    public boolean outOfBounds(Coordinates coords)
    {
        return outOfXBounds(coords.getxCoord()) || outOfYBounds(coords.getyCoord());
    }

    public boolean existsBot(int botNumber)
    {
        return bots != null && bots.containsKey(botNumber);
    }

    public Bot getBotOn(Coordinates coords)
    {
        for (Bot b : bots.values())
        {
            if (b.getCoords().equals(coords))
            {
                return b;
            }
        }
        return null;
    }

    public BoardElement getBoardElement(Coordinates coords)
    {
        return getBoardElement(coords.getxCoord(), coords.getyCoord());
    }

    public BoardElement getBoardElement(Integer xCoord, Integer yCoord) {
        for (Board board : boards)
        {
            if ((xCoord >= board.getxOffset()) && (xCoord < (board.getxOffset() + board.getxSize())))
            {
                if ((yCoord >= board.getyOffset()) && (yCoord < (board.getyOffset() + board.getySize())))
                {
                    return board.getElement(xCoord - board.getxOffset(), yCoord - board.getyOffset());
                }
            }
        }
        // not found on any of the existing boards so it must be outside.
        return BoardElement.outsideElement;
    }

    public static BufferedImage getPreviewImage(ImplementedScenario scenarioName, boolean standardFlags, List<Flag> flags, List<Bot> bots, int factor) {
        Game scenario = GameFactory.getScenario(scenarioName);
        if (standardFlags)
        {
            scenario.setFlags(GameFactory.getStandardFlags(scenarioName));
        }
        else
        {
            scenario.setFlags(flags);
        }
        if (bots!=null)
        {
            scenario.setBots(bots);
        }
        else
        {
            List<Bot> standardBots = new ArrayList<Bot>();
            standardBots.add(scenario.getInitialBot(1));
            standardBots.add(scenario.getInitialBot(2));
            standardBots.add(scenario.getInitialBot(3));
            scenario.setBots(standardBots);
        }
        return scenario.getImage(factor);
    }

    public Bot getInitialBot(int number)
    {
        int x = 0;
        int y = 0;
        boolean found=false;
        for (Board board : boards)
        {
            int i=0;
            while (i<board.getxSize() && !found)
            {
                int j=0;
                while (j<board.getySize() && !found)
                {
                    BoardElement element = board.getElement(i, j);
                    if (element instanceof BasicElement)
                    {
                        if(BasicElementType.STARTING.equals(element.getBoardElementType()) && number==(((BasicElement) element).getNumber()))
                        {
                            found=true;
                            x = board.getxOffset()+i;
                            y = board.getyOffset()+j;
                        }
                    }
                    if (!found)
                    {
                        j++;
                    }
                }
                if (!found)
                {
                    i++;
                }
            }
        }
        return Bot.getBot(x, y, number, "Bot " + number);
    }

    public BufferedImage getImage(int factor) {
        int w = (xSize+2)*factor*AbstractBoardElement.baseSize;
        int h = (ySize+2)*factor*AbstractBoardElement.baseSize;
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.setColor(new Color(31, 31, 31));
        g.fillRect(0, 0, w, h);
        g.translate(factor*AbstractBoardElement.baseSize, factor*AbstractBoardElement.baseSize);
        this.paint(g, factor);
        g.dispose();
        return bi;
    }

    public void paint(Graphics g, int factor) {
        int w = xSize*factor*AbstractBoardElement.baseSize;
        int h = ySize*factor*AbstractBoardElement.baseSize;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(Math.max((w - xSize * AbstractBoardElement.baseSize * factor) / 2, 0), Math.max((h - ySize * AbstractBoardElement.baseSize * factor) / 2, 0));
        for (Board board : boards) {
            board.paint(g, ySize * AbstractBoardElement.baseSize * factor, factor);
        }
        // paint flags first then bots so the bots actually show
        for (Flag flag : flags) {
            flag.paint(g, ySize * AbstractBoardElement.baseSize * factor, factor);
        }
        // TODO, if a bot is on a flag, should the flag number be visible somewhere? But how then.
        for (Bot bot : bots.values()) {
            if (bot!=null)
            {
                bot.paint(g, ySize * AbstractBoardElement.baseSize * factor, factor);
            }
        }
    }


	public Collection<AbstractMovingElement> getBotsAndFlags() {
        List<AbstractMovingElement> botsAndFlags = new ArrayList<AbstractMovingElement>();
        botsAndFlags.addAll( bots.values() );
        botsAndFlags.addAll( flags );
        return botsAndFlags;
	}

	public Collection<Bot> getBots() {
		return bots.values();
	}

	public Integer getCurrentRound() {
		return currentRound;
	}
}
