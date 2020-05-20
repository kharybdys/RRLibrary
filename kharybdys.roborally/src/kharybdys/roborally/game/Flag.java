package kharybdys.roborally.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.definition.Movement;

/**
 *
 * @author MHK
 */
public class Flag extends AbstractMovingElement {
    private Integer id;
    private Game game;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return null;  //Flags cannot have an archive object
    }

    @Override
    public void setArchiveObject(AbstractMovingElement ame) {
        //Flags cannot have an archive object
    }

    @Override
    public Integer getLives() {
        return 3;
    }

    @Override
    public void setLives(Integer lives) {
        //Do nothing, flags do not have lives
    }


    public static Flag getFlag(int xCoord, int yCoord, int number)
    {
        Flag flag = new Flag();
        flag.setArchiveXCoord(xCoord);
        flag.setArchiveYCoord(yCoord);
        flag.setOrderNumber(number);
        flag.setxCoord(xCoord);
        flag.setyCoord(yCoord);
        return flag;
    }

    @Override
    public void paintElement(Graphics g, int baseX, int baseY, int size, int factor)
    {
        g.setColor(Color.green);
        g.fillRect(baseX, baseY, size, size);
        g.setColor(new Color(0, 0, 0, 128));
        int fontHeight = (size);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontHeight));
        FontMetrics fm = g.getFontMetrics();
        int flagNumber = getOrderNumber();
        int xCorr = (size-2*factor - fm.stringWidth(flagNumber+"")) / 2;
        g.drawString(flagNumber+"", baseX+factor+xCorr, baseY+size -factor);
    }

    @Override
    public void processMovement(Movement rrm)
    {
        // flags can only be moved by board movement:
        if (rrm.getPriority() != Movement.RoboRallyMovementPriority.ROBOT_MOVEMENT)
        {
            rrm.updateXAndYCoords(this);
        }
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
        if (!(object instanceof Flag)) {
            return false;
        }
        Flag other = (Flag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
