package kharybdys.roborally.game.board;

public enum ImplementedScenario
{
     MovingTargets,
     Test,
     AgainstTheGrain,
     Tricksy,
     IslandKing,
     OddestSea,
     RobotStew,
     LostBearings,
     WhirlwindTour,
     VaultAssault,
     Pilgrimage,
     DeathTrap,
     AroundTheWorld,
     BloodbathChess,
     Twister,
     ChopShopChallenge,
     IslandHop,
     DizzyDash,
     Checkmate,
     RiskyExchange;
	
	public byte[] getPreviewImage( int nrOfBots, int factor )
	{
	    GameBuilder builder = this.getGameBuilder( true );
	    builder.addDummyBots( nrOfBots );
	    return builder.asGame( -1, 0 ).getImage( factor );
		
	}
	
	public GameBuilder getGameBuilder( boolean standardFlags )
	{
		switch( this )
		{
			case MovingTargets:
				return getMovingTargetsScenario( standardFlags );
			case AgainstTheGrain:
				return getAgainstTheGrainScenario( standardFlags );
			case Tricksy:
				return getTricksyScenario( standardFlags );
			case IslandKing:
				return getIslandKingScenario( standardFlags );
			case OddestSea:
				return getOddestSeaScenario( standardFlags );
			case RobotStew:
				return getRobotStewScenario( standardFlags );
			case LostBearings:
				return getLostBearingsScenario( standardFlags );
			case WhirlwindTour:
				return getWhirlwindTourScenario( standardFlags );
			case VaultAssault:
				return getVaultAssaultScenario( standardFlags );
			case Pilgrimage:
				return getPilgrimageScenario( standardFlags );
			case DeathTrap:
				return getDeathTrapScenario( standardFlags );
			case AroundTheWorld:
				return getAroundTheWorldScenario( standardFlags );
			case BloodbathChess:
				return getBloodbathChessScenario( standardFlags );
			case Twister:
				return getTwisterScenario( standardFlags );
			case ChopShopChallenge:
				return getChopShopChallengeScenario( standardFlags );
			case IslandHop:
				return getIslandHopScenario( standardFlags );
			case DizzyDash:
				return getDizzyDashScenario( standardFlags );
			case Checkmate:
				return getCheckmateScenario( standardFlags );
			case RiskyExchange:
				return getRiskyExchangeScenario( standardFlags );
			case Test:
				return getTestScenario( standardFlags );
			default:
				// Shouldn't be able to reach here
				return getTestScenario( standardFlags );
		}
	}

	public int getNrOfFlags() 
	{
		switch( this )
		{
			case MovingTargets:
			case Tricksy:
			case BloodbathChess:
			case Twister:
			case ChopShopChallenge:
				return 4;
			case IslandKing:
			case AgainstTheGrain:
			case OddestSea:
			case RobotStew:
			case LostBearings:
			case WhirlwindTour:
			case VaultAssault:
			case Pilgrimage:
			case DeathTrap:
			case AroundTheWorld:
			case IslandHop:
			case DizzyDash:
			case RiskyExchange:
				return 3;
			case Checkmate:
				return 2;
			case Test:
			default:
				return 1;
		}
	}

	private GameBuilder getTricksyScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard( 0, 12 );
	    builder.addCross( 0, 0, -1 );

	    if( standardFlags )
	    {
		    builder.addFlag( 9, 1, 1 );
		    builder.addFlag( 0, 1, 2 );
		    builder.addFlag( 8, 11, 3 );
		    builder.addFlag( 3, 7, 4 );
	    }
	    
	    return builder;
	}

	private GameBuilder getIslandKingScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addIsland( 0, 0, -1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 5, 4, 1 );
		    builder.addFlag( 7, 7, 2 );
		    builder.addFlag( 5, 6, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getAgainstTheGrainScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 28 );
	    builder.addFirstStartingBoard( 0, 24 );
	    builder.addChess( 0, 12, 0 );
	    builder.addChopShop( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 10, 9, 1 );
		    builder.addFlag( 3, 3, 2 );
		    builder.addFlag( 5, 17, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getOddestSeaScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 28 );
	    builder.addFirstStartingBoard( 0, 24 );
	    builder.addMaelstrom( 0, 12, -1 );
	    builder.addVault( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 8, 6, 1 );
		    builder.addFlag( 1, 3, 2 );
		    builder.addFlag( 5, 8, 3 );
		    builder.addFlag( 9, 2, 4 );
	    }
	    
	    return builder;
	}

	private GameBuilder getRobotStewScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard(  0, 12 );
	    builder.addChopShop( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 0, 4, 1 );
		    builder.addFlag( 9, 7, 2 );
		    builder.addFlag( 2, 10, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getLostBearingsScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard( 0, 12 );
	    builder.addIsland( 0, 0, -1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 1, 2, 1 );
		    builder.addFlag( 10, 9, 2 );
		    builder.addFlag( 2, 8, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getWhirlwindTourScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addMaelstrom( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 8, 0, 1 );
		    builder.addFlag( 3, 11, 2 );
		    builder.addFlag( 11, 6, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getVaultAssaultScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard( 0, 12 );
	    builder.addVault( 0, 0, 0 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 6, 3, 1 );
		    builder.addFlag( 4, 11, 2 );
		    builder.addFlag( 8, 5, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getPilgrimageScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 28 );
	    builder.addFirstStartingBoard( 0, 24 );
	    builder.addExchange( 0, 12, 0 );
	    builder.addCross( 0, 0, -1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 4, 15, 1 );
		    builder.addFlag( 9, 26, 2 );
		    builder.addFlag( 2, 5, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getDeathTrapScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addIsland( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 7, 7, 1 );
		    builder.addFlag( 0, 4, 2 );
		    builder.addFlag( 6, 5, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getAroundTheWorldScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 28 );
	    builder.addSecondStartingBoard( 0, 24 );
	    builder.addSpinZone( 0, 12, -1 );
	    builder.addIsland( 0, 0, -1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 9, 12, 1 );
		    builder.addFlag( 6, 1, 2 );
		    builder.addFlag( 5, 22, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getBloodbathChessScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addChess( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 6, 5, 1 );
		    builder.addFlag( 2, 9, 2 );
		    builder.addFlag( 8, 7, 3 );
		    builder.addFlag( 3, 4, 4 );
	    }
	    
	    return builder;
	}

	private GameBuilder getTwisterScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard( 0, 12 );
	    builder.addSpinZone( 0, 0, 2 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 2, 9, 1 );
		    builder.addFlag( 3, 2, 2 );
		    builder.addFlag( 9, 2, 3 );
		    builder.addFlag( 8, 9, 4 );
	    }
	    
	    return builder;
	}

	private GameBuilder getChopShopChallengeScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addChopShop( 0, 0, -1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 4, 9, 1 );
		    builder.addFlag( 9, 11, 2 );
		    builder.addFlag( 1, 10, 3 );
		    builder.addFlag( 11, 7, 4 );
	    }
	    
	    return builder;
	}

	private GameBuilder getIslandHopScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addIsland( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 6, 1, 1 );
		    builder.addFlag( 1, 6, 2 );
		    builder.addFlag( 11, 4, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getDizzyDashScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard( 0, 12 );
	    builder.addSpinZone( 0, 0, 0 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 5, 4, 1 );
		    builder.addFlag( 10, 11, 2 );
		    builder.addFlag( 1, 6, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getRiskyExchangeScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addSecondStartingBoard( 0, 12 );
	    builder.addExchange( 0, 0, 2 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 7, 1, 1 );
		    builder.addFlag( 9, 7, 2 );
		    builder.addFlag( 1, 4, 3 );
	    }
	    
	    return builder;
	}

	private GameBuilder getCheckmateScenario( boolean standardFlags ) 
	{
	
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addChess( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 7, 2, 1 );
		    builder.addFlag( 3, 8, 2 );
	    }
	    
	    return builder;
	}

	private GameBuilder getMovingTargetsScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 12, 16 );
	    builder.addFirstStartingBoard( 0, 12 );
	    builder.addMaelstrom( 0, 0, 1 );
	
	    if( standardFlags )
	    {
		    builder.addFlag( 1, 0, 1 );
		    builder.addFlag( 10, 11, 2 );
		    builder.addFlag( 11, 5, 3 );
		    builder.addFlag( 0, 6, 4 );
	    }
	    
	    return builder;
	}

	private GameBuilder getTestScenario( boolean standardFlags ) 
	{
	    GameBuilder builder = new GameBuilder();
	    builder.initializeBoard( 24, 52 );
	    builder.addFirstStartingBoard(  0,  48 );
	    builder.addSecondStartingBoard( 12, 48 );
	    
	    builder.addMaelstrom( 0,  36, 0 );
	    builder.addSpinZone(  12, 36, 0 );
	    builder.addChess(     0,  24, 0 );
	    builder.addIsland(    12, 24, 0 );
	    builder.addChopShop(  0,  12, 0 );
	    builder.addCross(     12, 12, 0 );
	    builder.addExchange(  0,  0,  0 );
	    builder.addVault(     12, 0,  0 );
	    
	    if( standardFlags )
	    {
	    	builder.addFlag( 4, 5, 1 );
	    }
	
	    return builder;
	}
}