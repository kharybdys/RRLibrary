package kharybdys.roborally.game.movement;

import java.util.Collection;
import java.util.Collections;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.MovementCardDefinition.MovementCardType;

public class BrakesTransformer implements MovementCardTransformer {

	@Override
	public Collection<Movement> getMovements(Collection<MovementCardDefinition> cards, Direction facingDirection) 
	{
		// Verify the input is as expected, ie one MOVE1. If not, throw an exception
		boolean move1Found = false;

		for( MovementCardDefinition card : cards )
		{
			if( card.getType().equals( MovementCardType.MOVE1 ) )
			{
				move1Found = true;
			}
		}
		
		if( cards.size() != 1 || ! move1Found )
		{
			throw new UnsupportedOperationException( "Using Brakes with anything other than a MOVE1 is not supported" );
		}
		
		return Collections.singletonList( new Movement( null, 0, 0, 0 ) );
	}

}
