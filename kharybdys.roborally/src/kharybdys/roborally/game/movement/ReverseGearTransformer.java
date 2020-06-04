package kharybdys.roborally.game.movement;

import java.util.ArrayList;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.MovementCardDefinition.MovementCardType;

public class ReverseGearTransformer implements MovementCardTransformer {

	@Override
	public Collection<Movement> getMovements(Collection<MovementCardDefinition> cards, Direction facingDirection) 
	{
		// Verify the input is as expected, ie one Backup. If not, throw an exception
		boolean backupFound = false;
		int priority = -1;

		for( MovementCardDefinition card : cards )
		{
			if( card.getType().equals( MovementCardType.BACKUP ) )
			{
				backupFound = true;
				priority = card.getPriority();
			}
		}
		
		if( cards.size() != 1 || ! backupFound )
		{
			throw new UnsupportedOperationException( "Using Reverse Gear with anything other than a BACKUP is not supported" );
		}
		
		Collection<Movement> movements = new ArrayList<Movement>( 2 );
		Direction reverseDirection = facingDirection.processRotate( 2 );
		movements.add( new Movement( reverseDirection, 0, 1, priority ) );
		movements.add( new Movement( reverseDirection, 0, 1, priority ) );

		return movements;
	}

}
