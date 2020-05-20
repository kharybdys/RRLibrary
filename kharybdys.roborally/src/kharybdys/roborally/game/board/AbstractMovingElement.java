package kharybdys.roborally.game.board;

import java.awt.Graphics;

import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.util.Coordinates;

/**
 *
 * @author MHK
 */
public abstract class AbstractMovingElement {

	private Integer xCoord;
	private Integer yCoord;
	private Integer archiveXCoord;
	private Integer archiveYCoord;
	private Integer orderNumber;

	private Boolean diedThisTurn = false;

	public AbstractMovingElement() {
	}

	public Integer getArchiveXCoord() {
		return archiveXCoord;
	}

	public void setArchiveXCoord(Integer archiveXCoord) {
		this.archiveXCoord = archiveXCoord;
	}

	public Integer getArchiveYCoord() {
		return archiveYCoord;
	}

	public void setArchiveYCoord(Integer archiveYCoord) {
		this.archiveYCoord = archiveYCoord;
	}

	public Integer getxCoord() {
		return xCoord;
	}

	public void setxCoord(Integer xCoord) {
		this.xCoord = xCoord;
	}

	public Integer getyCoord() {
		return yCoord;
	}

	public void setyCoord(Integer yCoord) {
		this.yCoord = yCoord;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void paint(Graphics g, int ySizePanel, int factor) {
		int size = AbstractBoardElement.baseSize * factor;
		int baseX = xCoord * size;
		int baseY = ySizePanel - ((yCoord + 1) * size);
		paintElement(g, baseX + 6 * factor, baseY + 6 * factor, size - 12 * factor, factor);

	}

	public BoardElement getBoardElement() {
		return this.getGame().getBoardElement(this.getxCoord(), this.getyCoord());
	}

	public abstract void paintElement(Graphics g, int baseX, int baseY, int size, int factor);

	public abstract void processMovement(Movement movement);

	public void processDeath() {
		setDiedThisTurn(Boolean.TRUE);
		if (getArchiveObject() != null) {
			setxCoord(getArchiveObject().getxCoord());
			setyCoord(getArchiveObject().getyCoord());
		} else {
			setxCoord(getArchiveXCoord());
			setyCoord(getArchiveYCoord());
		}
	}

	public abstract Game getGame();

	public abstract void setGame(Game game);

	public abstract AbstractMovingElement getArchiveObject();

	public abstract void setArchiveObject(AbstractMovingElement ame);

	public Coordinates getCoords() {
		return new Coordinates(getxCoord(), getyCoord());
	}

	public void setCoords(Coordinates coords) {
		setxCoord(coords.getxCoord());
		setyCoord(coords.getyCoord());
	}

	public Boolean getDiedThisTurn() {
		return diedThisTurn;
	}

	public void setDiedThisTurn(Boolean diedThisTurn) {
		this.diedThisTurn = diedThisTurn;
	}

	public abstract Integer getLives();

	public abstract void setLives(Integer lives);
}
