package kharybdys.roborally.game;

import java.util.Collections;
import java.util.List;

import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.MovementCardDefinition;

/**
 *
 * @author MHK
 */
public class MovementCard implements Comparable<MovementCard> {

    private Integer id;
    private Bot bot;
    private Integer round;
    private Integer phase;
    private Boolean frozen = false;
    private MovementCardDefinition cardDefinition;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public MovementCardDefinition getCardDefinition() {
        return cardDefinition;
    }

    public void setCardDefinition(MovementCardDefinition cardDefinition) {
        this.cardDefinition = cardDefinition;
    }

    public MovementCard copy()
    {
        MovementCard tempCard = new MovementCard();
        tempCard.setBot(getBot());
        tempCard.setCardDefinition(getCardDefinition());
        tempCard.setRound(getRound());
        tempCard.setFrozen(getFrozen());
        tempCard.setPhase(getPhase());
        return tempCard;
    }

    public List<Movement> getMovements()
    {
        int repeat = 1;
        int facingSteps=0;
        int movementSteps=0;
        int priority = getCardDefinition().getPriority();
        switch (getCardDefinition().getType())
        {
            case MOVE3: movementSteps=1; repeat = 3; break;
            case MOVE2: movementSteps=1; repeat = 2; break;
            case MOVE1: movementSteps=1; break;
            case BACKUP: movementSteps=-1; break;
            case TURNLEFT: facingSteps=-1; break;
            case TURNRIGHT: facingSteps=1; break;
            case UTURN: facingSteps=2; break;
        }
        return Collections.nCopies(repeat, new Movement(getBot().getFacingDirection(), facingSteps, Movement.RoboRallyMovementPriority.ROBOT_MOVEMENT, movementSteps, priority));
    }
    
    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovementCard)) {
            return false;
        }
        MovementCard other = (MovementCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (getCardDefinition() != null)
        {
            hash += getCardDefinition().getPriority().hashCode();
        }
        else
        {
            hash += (id != null ? id.hashCode() : 0);
        }
        return hash;
    }

    @Override
    public int compareTo(MovementCard o) {
        return this.getCardDefinition().getPriority().compareTo(o.getCardDefinition().getPriority());
    }
}
