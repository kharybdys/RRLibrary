package kharybdys.roborally.game;

import java.awt.*;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.util.Utils;

/**
 *
 * @author MHK
 */
public class Bot extends AbstractMovingElement {

    public static final Integer INITIAL_HEALTH = 9 ;

    private Integer id;
    private Integer damage = 0;
    private Integer lives = 3;
    private Game game;
    private Flag archiveObject;
    private String displayName;
    private Direction facingDirection;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        if (damage >= INITIAL_HEALTH)
        { // you died
            processDeath();
        }
        else
        {
            this.damage = damage;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Integer getLives() {
        return lives;
    }

    @Override
    public void setLives(Integer lives) {
        this.lives = lives;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public AbstractMovingElement getArchiveObject() {
        return archiveObject;
    }

    @Override
    public void setArchiveObject(AbstractMovingElement ame) {
        if (ame instanceof Flag)
        {
        archiveObject = (Flag) ame;
        }
        else
        {
            throw new IllegalArgumentException("Cannot set a bot (" + ame + ") as an archive object.");
        }
    }

    public static Bot getBot(int xCoord, int yCoord, int number, String name)
    {
        Bot bot = new Bot();
        bot.setArchiveXCoord(xCoord);
        bot.setArchiveYCoord(yCoord);
        bot.setxCoord(xCoord);
        bot.setyCoord(yCoord);
        bot.setOrderNumber(number);
        bot.setDisplayName(name);
        bot.setFacingDirection(Direction.NORTH);
        return bot;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bot)) {
            return false;
        }
        Bot other = (Bot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int size, int factor) {
        g.setColor(new Color(255, 0, 255, 255));
        switch (facingDirection) {
            case NORTH:
                g.fillPolygon(Utils.createTriangle(baseX, baseY + size / 2, baseX + size, baseY + size / 2, baseX + size / 2, baseY));
                g.fillRect(baseX, baseY + size / 2, size, size / 2);
                break;
            case SOUTH:
                g.fillPolygon(Utils.createTriangle(baseX, baseY + size / 2, baseX + size, baseY + size / 2, baseX + size / 2, baseY + size));
                g.fillRect(baseX, baseY, size, size / 2);
                break;
            case WEST:
                g.fillPolygon(Utils.createTriangle(baseX + size / 2, baseY, baseX + size / 2, baseY + size, baseX, baseY + size / 2));
                g.fillRect(baseX + size / 2, baseY, size / 2, size);
                break;
            case EAST:
                g.fillPolygon(Utils.createTriangle(baseX + size / 2, baseY, baseX + size / 2, baseY + size, baseX + size, baseY + size / 2));
                g.fillRect(baseX, baseY, size / 2, size);
                break;
        }
        g.setColor(new Color(0, 0, 0, 128));
        int fontHeight = (size);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontHeight));
        FontMetrics fm = g.getFontMetrics();
        int number = getOrderNumber();
        int xCorr = (size-2*factor - fm.stringWidth(number+"")) / 2;
        g.drawString(number+"", baseX+factor+xCorr, baseY+size -factor);
    }

    @Override
    public void processMovement(Movement rrm)
    {
        rrm.updateXAndYCoords(this);
        this.setFacingDirection(rrm.getNewFacingDirection(this.getFacingDirection()));
    }

    public void processDeath()
    {
        super.processDeath();
        setLives(getLives() - 1);
        setDamage(2);
        setFacingDirection(Direction.NORTH);
    }
}
