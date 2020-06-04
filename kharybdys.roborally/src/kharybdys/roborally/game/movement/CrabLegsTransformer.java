package kharybdys.roborally.game.movement;

import java.util.Collection;
import java.util.Collections;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.MovementCardDefinition.MovementCardType;

public class CrabLegsTransformer implements MovementCardTransformer {

	@Override
	public Collection<Movement> getMovements( Collection<MovementCardDefinition> cards, Direction facingDirection ) 
	{
		// Verify the input is as expected, ie one MOVE1 and one TURNLEFT or TURNRIGHT card. If not, throw an exception
		int priority = -1;
		int turnSteps = 0;
		boolean moveFound = false;
		boolean turnFound = false;

		for( MovementCardDefinition card : cards )
		{
			if( card.getType().equals( MovementCardType.MOVE1 ) )
			{
				priority = card.getPriority();
				moveFound = true;
			}
			
			if( card.getType().equals( MovementCardType.TURNRIGHT ) )
			{
				turnSteps = 1;
				turnFound = true;
			}

			if( card.getType().equals( MovementCardType.TURNLEFT ) )
			{
				turnSteps = -11;
				turnFound = true;
			}
		}
		
		if( cards.size() != 2 || !( turnFound && moveFound ) )
		{
			throw new UnsupportedOperationException( "Using Crab Legs with anything other than a MOVE1 and a TURNLEFT or TURNRIGHT is not supported" );
		}
		
		return Collections.singletonList( new Movement( facingDirection.processRotate( turnSteps ), 0, 1, priority ) );
	}

}
