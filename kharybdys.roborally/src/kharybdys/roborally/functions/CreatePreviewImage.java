package kharybdys.roborally.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.document.types.Binary;
import com.dexels.navajo.expression.api.FunctionInterface;
import com.dexels.navajo.expression.api.TMLExpressionException;

import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.board.ImplementedScenario;

public final class CreatePreviewImage extends FunctionInterface {

	
	private static final Logger logger = LoggerFactory.getLogger(CreatePreviewImage.class);
	
	@Override
	public final Object evaluate() {
		if ( getOperands().isEmpty() ) 
		{
			throw new TMLExpressionException(this, "Missing argument for scenarioName");
		}
		String scenarioName = getStringOperand( 0 );
		try 
		{
			ImplementedScenario scenario = ImplementedScenario.valueOf( scenarioName );
			// zero bots (preview image), factor 4 (TODO: make factor an optional input)
			return new Binary( scenario.getPreviewImage( 0, Game.DEFAULT_FACTOR ) );
			
		}
		catch( IllegalArgumentException e )
		{
			logger.error( "Don't know the scenario with name {}", scenarioName );
			return null;
		}
		
	}
    @Override
	public boolean isPure() {
    		return true;
    }

	@Override
	public String remarks() {
		return "Creates a preview image for a given scenario (by name)";
	}

	@Override
	public String usage() {
		return "CreatePreviewImage( string )";
	}

}
