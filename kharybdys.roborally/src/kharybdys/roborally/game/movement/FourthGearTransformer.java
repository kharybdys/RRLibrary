package kharybdys.roborally.game.movement;

import java.util.ArrayList;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.MovementCardDefinition.MovementCardType;

public class FourthGearTransformer implements MovementCardTransformer {

	@Override
	public Collection<Movement> getMovements(Collection<MovementCardDefinition> cards, Direction facingDirection) 
	{
		// Verify the input is as expected, ie one MOVE3. If not, throw an exception
		boolean move3Found = false;
		int priority = -1;

		for( MovementCardDefinition card : cards )
		{
			if( card.getType().equals( MovementCardType.MOVE3 ) )
			{
				move3Found = true;
				priority = card.getPriority();
			}
		}
		
		if( cards.size() != 1 || ! move3Found )
		{
			throw new UnsupportedOperationException( "Using Fourth Gear with anything other than a MOVE3 is not supported" );
		}

		Collection<Movement> movements = new ArrayList<Movement>( 4 );
		movements.add( new Movement( facingDirection, 0, 1, priority ) );
		movements.add( new Movement( facingDirection, 0, 1, priority ) );
		movements.add( new Movement( facingDirection, 0, 1, priority ) );
		movements.add( new Movement( facingDirection, 0, 1, priority ) );

		return movements;
	}

}
