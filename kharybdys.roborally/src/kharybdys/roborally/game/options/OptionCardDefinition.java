package kharybdys.roborally.game.options;

import kharybdys.roborally.game.movement.BaseMovementTransformer;
import kharybdys.roborally.game.movement.BrakesTransformer;
import kharybdys.roborally.game.movement.CrabLegsTransformer;
import kharybdys.roborally.game.movement.DualProcessorTransformer;
import kharybdys.roborally.game.movement.FourthGearTransformer;
import kharybdys.roborally.game.movement.MovementCardTransformer;
import kharybdys.roborally.game.movement.ReverseGearTransformer;

/**
 *  Defines all the possible option cards
 *  TODO: Decide whether to implement and do so
 */
public enum OptionCardDefinition 
{
	// shot replacers
	TRACTOR_BEAM, // shot replacer, pull 1 space towards you if bot is more than one space away. 
	PRESSOR_BEAM, // shot replacer, push 1 space away from you
	MINI_HOWITZER, // shot replacer, push 1 space away AND 1 damage. Max nr of uses is 5
	FIRE_CONTROL, // shot replacer, destroy an option or lock a register on the target   ** How do you get such a locked register unlocked?  ** Complexity because of integration with DOUBLE_BARRELED and HIGH_POWERED
	RADIO_CONTROL, // shot replacer, for the rest of the round the target will copy the original bot's movements exactly, with priority 1 lower than original bot's
	SCRAMBLER, // shot replacer, replace next movement card for the target with a random movement card, if any actions are left in the round.
	
	// other shot effects
	DOUBLE_BARRELED_LASER, // Does two damage with the forward laser instead. Also doubles effect of FIRE_CONTROL and HIGH_POWER_LASER
	REAR_FIRING_LASER, // extra shot from your rear. Unsure whether shot replacer options apply to this shot too
	HIGH_POWER_LASER, // When shooting, shoot through one robot or wall before hitting a potential second one. If shooting through a robot, that one takes full damage. Both bots will be hit by FIRE_CONTROL or DOUBLE_BARRELED_LASER if applicable
	
	// planning items (ie needs integration in planning UI)
	CRAB_LEGS, // combine MOVE1 with TURNLEFT/TURNRIGHT to create a step sideways action instead. Priority of MOVE1
	BRAKES, // When using a MOVE1 one can opt to move zero spaces instead of one.
	FOURTH_GEAR, // When using a MOVE3 one can opt to make it move four places instead of three.
	REVERSE_GEAR, // When using a BACKUP, one can opt to make it move two spaces instead of one.
	DUAL_PROCESSOR, // One can use two cards at once in a register phase: one MOVE (incl BACKUP) card and one TURN card. If you do, the MOVE effect is reduced by one per step to turn (not going to negative), executed, then the TURN is executed. Priority is of the MOVE card.
	FLYWHEEL, // reserve one movement card from hand that is available next round
	GYROSCOPIC_STABILIZER, // if on this round, do not get rotated by the board this entire round (rotators, conveyor belts)
	RECOMPILE, // Once each round get a new hand of movement cards in exchange for one damage (after)
	
	// passive effects
	POWER_DOWN_SHIELD, // while powered down, ignore the first damage each phase from each of the four directions
	SUPERIOR_ARCHIVE, // on Revive do not take the two initial damage
	MECHANICAL_ARM, // flags, repair sites and option sites get touched even when one space away (orthogonally and diagonally)
	ABLATIVE_COAT, // Absorbs the next three damage then gets discarded
	EXTRA_MEMORY, // Get one extra movement card each round to choose from
	
	// other
	CIRCUIT_BREAKER, // at 3 or more damage end of round automatic and forced power down  ** NOT IMPLEMENT?
	CONDITIONAL_PROGRAM, // reserve one movement card from hand on this option to be used to replace one phase if announced at beginning of phase ** UI PROBLEM
	ABORT_SWITCH, // replace rest of phases with random movement cards once/round ** UI PROBLEM
	RAMMING_GEAR; // When pushing a bot (regardless whether the other moves), the target takes one damage

	/**
	 * Factory method to get the correct MovementCardTransformer object based on this option card
	 * 
	 * @return The movementCardTransformer object corresponding to this option card
	 */
	public MovementCardTransformer getMovementCardTransformer() 
	{
		switch( this )
		{
			case CRAB_LEGS:
				return new CrabLegsTransformer();
			case BRAKES:
				return new BrakesTransformer();
			case FOURTH_GEAR:
				return new FourthGearTransformer();
			case REVERSE_GEAR:
				return new ReverseGearTransformer();
			case DUAL_PROCESSOR:
				return new DualProcessorTransformer();
			default:
				return new BaseMovementTransformer();
		}
	}
}
