package kharybdys.roborally.game.movement;

import java.util.Collection;
import java.util.Collections;

import kharybdys.roborally.game.definition.Direction;
import kharybdys.roborally.game.movement.MovementCardDefinition.MovementCardType;

public class DualProcessorTransformer implements MovementCardTransformer {

	@Override
	public Collection<Movement> getMovements(Collection<MovementCardDefinition> cards, Direction facingDirection) 
	{
		// Verify the input is as expected, ie one moveType card and one turnType card. If not, throw an exception
		int priority = -1;
		int turnSteps = 0;
		int movingSteps = 0;
		
		boolean moveFound = false;
		boolean turnFound = false;

		for( MovementCardDefinition card : cards )
		{
			if( isMoveType( card.getType() ) )
			{
				priority = card.getPriority();
				moveFound = true;
				// a backup will get reduced to zero steps anyhow, so take the absolute here
				movingSteps = Math.abs( card.getType().getMovingSteps() );
			}
			
			if( isTurnType( card.getType() ) )
			{
				turnSteps = card.getType().getTurnSteps();
				turnFound = true;
			}
		}
		
		if( cards.size() != 2 || !( turnFound && moveFound ) )
		{
			throw new UnsupportedOperationException( "Using Dual Processor with anything other than one moveType card and one turnType card is not supported" );
		}
		
		
		return Collections.singletonList( new Movement( facingDirection, Math.max( 0, movingSteps - Math.abs( turnSteps ) ), turnSteps, priority ) );
	}

	private boolean isMoveType( MovementCardType type ) 
	{
		return type.equals( MovementCardType.MOVE1 ) || type.equals( MovementCardType.MOVE2 ) || type.equals( MovementCardType.MOVE3 ) || type.equals( MovementCardType.BACKUP );
	}

	private boolean isTurnType( MovementCardType type ) 
	{
		return type.equals( MovementCardType.TURNLEFT ) || type.equals( MovementCardType.TURNRIGHT ) || type.equals( MovementCardType.UTURN );
	}
}


