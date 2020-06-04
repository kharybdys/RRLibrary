package kharybdys.roborally.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.document.types.Binary;
import com.dexels.navajo.expression.api.FunctionInterface;
import com.dexels.navajo.expression.api.TMLExpressionException;

import kharybdys.roborally.game.Game;
import kharybdys.roborally.game.board.ImplementedScenario;

public final class GetNrOfFlags extends FunctionInterface {

	
	private static final Logger logger = LoggerFactory.getLogger(GetNrOfFlags.class);
	
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
			return scenario.getNrOfFlags();
			
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
		return "Returns the number of flags in the given scenario";
	}

	@Override
	public String usage() {
		return "GetNrOfFlags( string )";
	}

}
