package kharybdys.roborally.adapters;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.document.types.Binary;
import com.dexels.navajo.script.api.Access;
import com.dexels.navajo.script.api.Mappable;
import com.dexels.navajo.script.api.MappableException;
import com.dexels.navajo.script.api.UserException;

import kharybdys.roborally.game.GameFactory;
import kharybdys.roborally.game.board.ImplementedScenario;

//import kharybdys.roborally.game.Game;

public class RoboRallyGameAdapter implements Mappable {

	public Integer gameId;
//	private Game game;

	public boolean doInitializeGame;

	private static final Logger logger = LoggerFactory.getLogger(RoboRallyGameAdapter.class);

	@Override
	public void load(Access access) throws MappableException, UserException {
		// TODO: Probably initialize the game object based on the given gameId (ie go
		// the DB and get all that's necessary)
	}

	@Override
	public void store() throws MappableException, UserException {
	}

	@Override
	public void kill() {
	}

	public boolean isDoInitializeGame() {
		return doInitializeGame;
	}

	public void setDoInitializeGame(boolean doInitializeGame) throws UserException {
		this.doInitializeGame = doInitializeGame;
		// Decide starting order for the player and initialize their bots and the
		// archive markers
		// Shuffle and deal movement cards
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Binary getBoardImage() {
//    	return new Binary( game.getBoardImage() );
		try {
			BufferedImage bi = GameFactory.getPreviewImage(ImplementedScenario.Test, 5, 4);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", baos);
			return new Binary(baos.toByteArray());
		} catch (IOException e) {
			logger.error("Something went wrong outputting the boardimage for game {} as a byte array", gameId, e);
			return null;
		}
	}

}
