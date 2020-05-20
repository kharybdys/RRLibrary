package kharybdys.roborally.game.definition;

/**
 *
 * @author MHK
 */
public class MovementCardDefinition {
	public enum MovementCardType {
		MOVE3, MOVE2, MOVE1, BACKUP, TURNRIGHT, TURNLEFT, UTURN
	}

	private Long id;
	private MovementCardType type;
	private Integer priority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public MovementCardType getType() {
		return type;
	}

	public void setType(MovementCardType type) {
		this.type = type;
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
		if (!(object instanceof MovementCardDefinition)) {
			return false;
		}
		MovementCardDefinition other = (MovementCardDefinition) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}
}
