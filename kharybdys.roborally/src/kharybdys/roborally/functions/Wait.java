package kharybdys.roborally.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexels.navajo.expression.api.FunctionInterface;
import com.dexels.navajo.expression.api.TMLExpressionException;

public final class Wait extends FunctionInterface {

	
	private static final Logger logger = LoggerFactory.getLogger(Wait.class);
	
	@Override
	public final Object evaluate() {
		if ( getOperands().isEmpty() ) {
			throw new TMLExpressionException(this, "Missing argument");
		}
		int w = getIntegerOperand(0);
		try {
			Thread.sleep(w);
			} catch (InterruptedException e) {
				logger.error("Error: ", e);
			}
		return null;
	}
    @Override
	public boolean isPure() {
    		return false;
    }

	@Override
	public String remarks() {
		return "Wait for specified number of milliseconds";
	}

	@Override
	public String usage() {
		return "Wait(integer)";
	}

}
