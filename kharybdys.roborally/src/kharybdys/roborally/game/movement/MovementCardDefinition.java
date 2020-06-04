package kharybdys.roborally.game.movement;

import java.util.ArrayList;
import java.util.Collection;

import kharybdys.roborally.game.definition.Direction;

/**
 *  Defines all the possible movement cards with conversion method to Movement object(s)
 */
public enum MovementCardDefinition 
{
    MC_10(  10,  MovementCardType.UTURN ),
    MC_20(  20,  MovementCardType.UTURN ),
    MC_30(  30,  MovementCardType.UTURN ),
    MC_40(  40,  MovementCardType.UTURN ),
    MC_50(  50,  MovementCardType.UTURN ),
    MC_60(  60,  MovementCardType.UTURN ),
    MC_70(  70,  MovementCardType.TURNLEFT ),
    MC_80(  80,  MovementCardType.TURNRIGHT ),
    MC_90(  90,  MovementCardType.TURNLEFT ),
    MC_100( 100, MovementCardType.TURNRIGHT ),
    MC_110( 110, MovementCardType.TURNLEFT ),
    MC_120( 120, MovementCardType.TURNRIGHT ),
    MC_130( 130, MovementCardType.TURNLEFT ),
    MC_140( 140, MovementCardType.TURNRIGHT ),
    MC_150( 150, MovementCardType.TURNLEFT ),
    MC_160( 160, MovementCardType.TURNRIGHT ),
    MC_170( 170, MovementCardType.TURNLEFT ),
    MC_180( 180, MovementCardType.TURNRIGHT ),
    MC_190( 190, MovementCardType.TURNLEFT ),
    MC_200( 200, MovementCardType.TURNRIGHT ),
    MC_210( 210, MovementCardType.TURNLEFT ),
    MC_220( 220, MovementCardType.TURNRIGHT ),
    MC_230( 230, MovementCardType.TURNLEFT ),
    MC_240( 240, MovementCardType.TURNRIGHT ),
    MC_250( 250, MovementCardType.TURNLEFT ),
    MC_260( 260, MovementCardType.TURNRIGHT ),
    MC_270( 270, MovementCardType.TURNLEFT ),
    MC_280( 280, MovementCardType.TURNRIGHT ),
    MC_290( 290, MovementCardType.TURNLEFT ),
    MC_300( 300, MovementCardType.TURNRIGHT ),
    MC_310( 310, MovementCardType.TURNLEFT ),
    MC_320( 320, MovementCardType.TURNRIGHT ),
    MC_330( 330, MovementCardType.TURNLEFT ),
    MC_340( 340, MovementCardType.TURNRIGHT ),
    MC_350( 350, MovementCardType.TURNLEFT ),
    MC_360( 360, MovementCardType.TURNRIGHT ),
    MC_370( 370, MovementCardType.TURNLEFT ),
    MC_380( 380, MovementCardType.TURNRIGHT ),
    MC_390( 390, MovementCardType.TURNLEFT ),
    MC_400( 400, MovementCardType.TURNRIGHT ),
    MC_410( 410, MovementCardType.TURNLEFT ),
    MC_420( 420, MovementCardType.TURNRIGHT ),
    MC_430( 430, MovementCardType.BACKUP ),
    MC_440( 440, MovementCardType.BACKUP ),
    MC_450( 450, MovementCardType.BACKUP ),
    MC_460( 460, MovementCardType.BACKUP ),
    MC_470( 470, MovementCardType.BACKUP ),
    MC_480( 480, MovementCardType.BACKUP ),
    MC_490( 490, MovementCardType.MOVE1 ),
    MC_500( 500, MovementCardType.MOVE1 ),
    MC_510( 510, MovementCardType.MOVE1 ),
    MC_520( 520, MovementCardType.MOVE1 ),
    MC_530( 530, MovementCardType.MOVE1 ),
    MC_540( 540, MovementCardType.MOVE1 ),
    MC_550( 550, MovementCardType.MOVE1 ),
    MC_560( 560, MovementCardType.MOVE1 ),
    MC_570( 570, MovementCardType.MOVE1 ),
    MC_580( 580, MovementCardType.MOVE1 ),
    MC_590( 590, MovementCardType.MOVE1 ),
    MC_600( 600, MovementCardType.MOVE1 ),
    MC_610( 610, MovementCardType.MOVE1 ),
    MC_620( 620, MovementCardType.MOVE1 ),
    MC_630( 630, MovementCardType.MOVE1 ),
    MC_640( 640, MovementCardType.MOVE1 ),
    MC_650( 650, MovementCardType.MOVE1 ),
    MC_660( 660, MovementCardType.MOVE1 ),
    MC_670( 670, MovementCardType.MOVE2 ),
    MC_680( 680, MovementCardType.MOVE2 ),
    MC_690( 690, MovementCardType.MOVE2 ),
    MC_700( 700, MovementCardType.MOVE2 ),
    MC_710( 710, MovementCardType.MOVE2 ),
    MC_720( 720, MovementCardType.MOVE2 ),
    MC_730( 730, MovementCardType.MOVE2 ),
    MC_740( 740, MovementCardType.MOVE2 ),
    MC_750( 750, MovementCardType.MOVE2 ),
    MC_760( 760, MovementCardType.MOVE2 ),
    MC_770( 770, MovementCardType.MOVE2 ),
    MC_780( 780, MovementCardType.MOVE2 ),
    MC_790( 790, MovementCardType.MOVE3 ),
    MC_800( 800, MovementCardType.MOVE3 ),
    MC_810( 810, MovementCardType.MOVE3 ),
    MC_820( 820, MovementCardType.MOVE3 ),
    MC_830( 830, MovementCardType.MOVE3 ),
    MC_840( 840, MovementCardType.MOVE3 );

	private int priority;
	private MovementCardType type;
	
	private MovementCardDefinition( int priority, MovementCardType type )
	{
		this.priority = priority;
		this.type = type;
	}
	
	protected enum MovementCardType 
	{
		MOVE3(     3, 0 ), 
		MOVE2(     2, 0 ), 
		MOVE1(     1, 0 ), 
		BACKUP(   -1, 0 ), 
		TURNRIGHT( 0, 1 ), 
		TURNLEFT(  0, -1 ), 
		UTURN(     0, 2 );
		
		private int movingSteps = 0;
		private int turnSteps = 0;
		
		private MovementCardType( int movingSteps, int turnSteps )
		{
			this.movingSteps = movingSteps;
			this.turnSteps = turnSteps;
		}

		protected int getMovingSteps() 
		{
			return movingSteps;
		}

		protected int getTurnSteps() 
		{
			return turnSteps;
		}
	}
	
	public Collection<Movement> getMovements( Direction facingDirection )
	{
		Collection<Movement> movements = new ArrayList<Movement>();
		return movements;
	}

	protected int getPriority() 
	{
		return priority;
	}

	protected MovementCardType getType() 
	{
		return type;
	}
}
