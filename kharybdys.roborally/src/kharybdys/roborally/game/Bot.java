package kharybdys.roborally.game;

import java.awt.*;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.board.BoardElement;
import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.definition.Movement;

/**
 *
 * @author MHK
 */
public class Bot extends AbstractMovingElement {

	public static final Integer INITIAL_HEALTH = 9 ;

    private Integer id;
    private Integer damage = 0;
    private Game game;
    private String displayName;
    private Direction facingDirection;

    public Bot( Integer orderNumber ) 
    {
		super( orderNumber );
	}

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
                g.fillPolygon(BoardElement.createTriangle(baseX, baseY + size / 2, baseX + size, baseY + size / 2, baseX + size / 2, baseY));
                g.fillRect(baseX, baseY + size / 2, size, size / 2);
                break;
            case SOUTH:
                g.fillPolygon(BoardElement.createTriangle(baseX, baseY + size / 2, baseX + size, baseY + size / 2, baseX + size / 2, baseY + size));
                g.fillRect(baseX, baseY, size, size / 2);
                break;
            case WEST:
                g.fillPolygon(BoardElement.createTriangle(baseX + size / 2, baseY, baseX + size / 2, baseY + size, baseX, baseY + size / 2));
                g.fillRect(baseX + size / 2, baseY, size / 2, size);
                break;
            case EAST:
                g.fillPolygon(BoardElement.createTriangle(baseX + size / 2, baseY, baseX + size / 2, baseY + size, baseX + size, baseY + size / 2));
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

		currentLocation.setBot( null );
		currentLocation = null;

		this.lives--;
        setDamage(2);
        setFacingDirection(Direction.NORTH);
    }
}
