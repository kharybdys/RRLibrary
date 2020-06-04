package kharybdys.roborally.game.movement;

import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

public interface MovementCardTransformer 
{
	/**
	 * Transforms the given MovementCardDefinitions to movements taking into account optional rules from optionCards, if applicable
	 * 
	 * @param cards
	 * @return
	 */
	public Collection<Movement> getMovements( Collection<MovementCardDefinition> cards, Direction facingDirection );

	public static MovementCardTransformer getDefaultMovementCardTransformer()
	{
		return new BaseMovementTransformer();
	} 
}
