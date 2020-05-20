package kharybdys.roborally.game.definition;

/**
 *
 * @author MHK
 */
public enum Direction {

	NORTH, EAST, SOUTH, WEST;

	/**
	 * positive is clockwise, negative is counterclockwise
	 * 
	 * @param steps
	 * @return
	 */
	public Direction processRotate(int steps) {
		int newDirectionOrd = this.ordinal() + steps;
		while (newDirectionOrd < 0 || newDirectionOrd > 3) {
			if (newDirectionOrd > 3) {
				newDirectionOrd -= 4;
			}
			if (newDirectionOrd < 0) {
				newDirectionOrd += 4;
			}
		}
		return Direction.values()[newDirectionOrd];
	}

	public int getTurns(Direction newDirection) {
		int turn = this.ordinal() - newDirection.ordinal();
		while (turn < 0 || turn > 3) {
			if (turn < 0) {
				turn += 4;
			}
			if (turn > 3) {
				turn -= 4;
			}
		}
		return turn;
	}
}
