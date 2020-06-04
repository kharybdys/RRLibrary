package kharybdys.roborally.game.movement;

import java.util.ArrayList;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

public class BaseMovementTransformer implements MovementCardTransformer {

	@Override
	public Collection<Movement> getMovements( Collection<MovementCardDefinition> cards, Direction facingDirection ) 
	{
		Collection<Movement> movements = new ArrayList<Movement>();
		for( MovementCardDefinition card : cards )
		{
			switch( card.getType() )
			{
				case MOVE3:
					movements.add( new Movement( facingDirection, 0, 1, card.getPriority() ) );
					// intentional no break
				case MOVE2:
					movements.add( new Movement( facingDirection, 0, 1, card.getPriority() ) );
					// 	intentional no break
				case MOVE1:
					movements.add( new Movement( facingDirection, 0, 1, card.getPriority() ) );
					break;
				case BACKUP:
					movements.add( new Movement( facingDirection.processRotate( 2 ), 0, 1, card.getPriority() ) );
					break;
				case TURNLEFT:
					movements.add( new Movement( null, -1, 0, card.getPriority() ) );
					break;
				case TURNRIGHT:
					movements.add( new Movement( null, 1, 0, card.getPriority() ) );
					break;
				case UTURN:
					movements.add( new Movement( null, 2, 0, card.getPriority() ) );
					break;
			}
		}
		return movements;
	}

}
