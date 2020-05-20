package kharybdys.roborally.game;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kharybdys.roborally.game.board.AbstractBoardElement;
import kharybdys.roborally.game.board.AbstractMovingElement;
import kharybdys.roborally.game.board.BoardElement;
import kharybdys.roborally.game.definition.Movement;
import kharybdys.roborally.game.definition.MovementCardDefinition;
import kharybdys.util.Coordinates;

/**
 * Static class with all the functions necessary for calculating a turn.
 * Methods are to be split off into commands once implementation of option cards start.
 */
public class RulesEngine {

	private static final Logger logger = LoggerFactory.getLogger( RulesEngine.class );

    private static final int MAX_PHASE = 5 ;
    private static final Boolean[] READY_PHASES = new Boolean[MAX_PHASE + 1];
    static
    {
        for (int i = 0 ; i <=MAX_PHASE ; i++)
        {
            READY_PHASES[i] = true;
        }
    }

    //TODO: Verify that bots/flags that died do not move on subsequent register phases
    //TODO: Ensure that bots/flags that died do not get hit again by bot or wall lasers on subsequent register phases
    public static void processNextTurn(Game g)
    {
        logger.warn("processNextTurn called!");
        if(gameReadyForTurn(g))
        {
            for (int phase = 1; phase <= MAX_PHASE; phase++)
            {
                processOnePhase(g, phase);
                logger.info("processed phase " + phase + " for game " + g);
            }
        }
        else
        {
            logger.info("processNextTurn decides game " + g + " is not ready for turn ");
        }
    }

    public static boolean gameReadyForTurn(Game g)
    {
        boolean ready = true;
        for (Bot b : g.getBots())
        {
            Boolean[] phases = new Boolean[MAX_PHASE + 1];
            phases[0] = true;
            for (int i=1;i<=MAX_PHASE;i++)
            {
                phases[i]=false;
            }
            // TODO
            for (MovementCard c : (List<MovementCard>) null)
            {
                if (c.getPhase() != null && c.getPhase() >= 1 && c.getPhase() <=MAX_PHASE)
                {
                    phases[c.getPhase()]=true;
                }
            }
            ready = Arrays.deepEquals(phases, READY_PHASES);
            if (!ready)
            {
                break;
            }
        }
        return ready;
    }

    public static void initializeCards(Game g)
    {
        // assumption no cards have been dealt yet for this round.
        List<MovementCardDefinition> cardDefinitions = null;
        Integer amountCardDefinitions = cardDefinitions.size();
        List<MovementCardDefinition> usedCardDefinitions = new ArrayList<MovementCardDefinition>();
        // first process frozen cards:
        if (g.getCurrentRound()>1)
        {
            for (Bot b : g.getBots())
            {
                if (b.getDamage()>=MAX_PHASE)
                {
                	// TODO
                    List<MovementCard> cardsToRepeat = null;
                    for (MovementCard card : cardsToRepeat)
                    {
                        MovementCard tempCard = card.copy();
                        tempCard.setFrozen(true);
                        tempCard.setRound(g.getCurrentRound());
                        usedCardDefinitions.add(tempCard.getCardDefinition());
                    }
                }
            }
        }
        // now divvy out the remaining cards:
        for (Bot b : g.getBots())
        {
            logger.debug("Initializing cards for bot " + b);
            
            for (int i=1;i<=9-b.getDamage();i++)
            {
                MovementCardDefinition currentDefinition = cardDefinitions.get((int)Math.floor(Math.random()*amountCardDefinitions));
                while (usedCardDefinitions.contains(currentDefinition))
                {
                    currentDefinition = cardDefinitions.get((int)Math.floor(Math.random()*amountCardDefinitions));
                }
                MovementCard card = new MovementCard();
                card.setBot(b);
                card.setCardDefinition(currentDefinition);
                card.setRound(g.getCurrentRound());
                logger.trace("Persisting card " + card + " for bot " + b);
                usedCardDefinitions.add(currentDefinition);
            }
        }
    }

    public static void processOnePhase(Game g, int phase)
    {
        // find cards by bot for this phase.
        List<MovementCard> cardsForThisPhase = new ArrayList<MovementCard>();
        // TODO
        logger.info("Processing following cards in this order: " + cardsForThisPhase);
        // process cards in priority order
        for (MovementCard mc : cardsForThisPhase)
        {
            Bot bot = mc.getBot();
            if (!bot.getDiedThisTurn() && bot.getLives() >= 0)            {
                for (Movement movement : mc.getMovements())
                {
                    processAbstractMovingElementMovement(g, bot, movement);
                }
                // check for repair or option site to repair
                BoardElement currentElement = bot.getBoardElement();
                if (currentElement.getBoardElementType().equals(AbstractBoardElement.BoardElementType.REPAIR)
                        || currentElement.getBoardElementType().equals(AbstractBoardElement.BoardElementType.OPTION))
                {
                    //TODO: Verify repair works
                    logger.info("Current bot is repairing: " + bot);
                    bot.setDamage(Math.max(0, bot.getDamage()-1));
                    if (currentElement.getBoardElementType().equals(AbstractBoardElement.BoardElementType.OPTION))
                    {
                        //TODO: Verify option check works. Also verify it is supposed to be here.
                        logger.info("Current bot is on an option site but this is not implemented (yet): " + bot);
                    }
                }
            }
        }
        // process boardmovement in parts:
        // process conveyor belts
        processBoardElementMovementForDualSpeedConveyor(g, phase);
        processBoardElementMovementForPriority(g, phase, Movement.RoboRallyMovementPriority.SINGLE_SPEED_CONVEYOR);
        // did conveyors cause bots to push other bots?
        // process rotators
        processBoardElementMovementForPriority(g, phase, Movement.RoboRallyMovementPriority.ROTATOR);
        // process pushers
        processBoardElementMovementForPriority(g, phase, Movement.RoboRallyMovementPriority.PUSHER);
        processLasers(g);
    }

    public static void processLasers(Game g)
    {
        //TODO: process bot lasers
        for (Bot shooter : g.getBots())
        {
        }
        //TODO: Verify that wall lasers don't hit if the bot already died from bot lasers.
        for (Bot beingShot : g.getBots())
        {
            g.processDamageFromWallLaserForBot(beingShot);
        }

    }

    public static Movement processAbstractMovingElementMovement(Game g, AbstractMovingElement ame, Movement movement) {
        BoardElement currentElement = ame.getBoardElement();
        // wall check
        movement = currentElement.adjustMovementForWalls(movement);
        if (ame instanceof Bot)
        {
            // push check
            movement = processPossiblePushedBot(g, movement, ((Bot) ame).getCoords(), ((Bot) ame).getOrderNumber());
        }
        ame.processMovement(movement);
        // we can have moved so update the element we are on.
        currentElement = ame.getBoardElement();
        // pit check
        if (currentElement.getBoardElementType().equals(AbstractBoardElement.BoardElementType.HOLE))
        {
            logger.info("Current bot or flag just fell of the board or into a pit: " + ame);
            ame.processDeath();
        }
        //TODO: possible tag of flag or repair or option site.
        return movement;
    }

    private static Movement processPossiblePushedBot(Game g, Movement movement, Coordinates coords, Integer orderNumberToSkip) {
        Bot pushedBot = g.getBotOn(coords);
        if (pushedBot != null && ! pushedBot.getOrderNumber().equals( orderNumberToSkip ))
        {
            movement = processAbstractMovingElementMovement(g, pushedBot, movement);
            // I am getting the corrected movement so in case I shouldn't move, the movement returned will be zero.
        }
        return movement;
    }

    public static void processBoardElementMovementForDualSpeedConveyor(Game g, int phase)
    {
        // we might be moved onto another dual speed so call it twice.
        processBoardElementMovementForPriority(g, phase, Movement.RoboRallyMovementPriority.DUAL_SPEED_CONVEYOR);
        processBoardElementMovementForPriority(g, phase, Movement.RoboRallyMovementPriority.DUAL_SPEED_CONVEYOR);
    }

    //TODO: Test pusher board movement
    //TODO: Test conveyor then rotator board movement
    //TODO: Test conveyor then pusher board movement
    //TODO: Test pusher then rotator board movement (shouldn't rotate)
    //TODO: Test pusher then conveyor board movement (shouldn't move on the conveyor)
    public static void processBoardElementMovementForPriority(Game g, int phase, Movement.RoboRallyMovementPriority priority)
    {
        Collection<AbstractMovingElement> botsAndFlags = g.getBotsAndFlags();
        for (AbstractMovingElement ame : botsAndFlags)
        {
            if (!ame.getDiedThisTurn() && ame.getLives() >= 0)
            {
                BoardElement currentElement = ame.getBoardElement();
                if (currentElement.getMovementPriority().equals(priority))
                {
                    // move this bot or flag
                    Movement movement = currentElement.getBoardMovement(phase);
                    if (movement != null)
                    {
                        processAbstractMovingElementMovement(g, ame, movement);
                        currentElement = ame.getBoardElement();
                        // to prevent NPE. Also, if it's null there is nothing to correct anyways.
                        if (movement.getMovingDirection() != null)
                        {
                            Movement correctingMovement = currentElement.correctingMovementAfter(movement.getMovingDirection().processRotate(2));
                            if (correctingMovement != null)
                            {
                                logger.info("Performing correcting movement on bot or flag " + ame);
                                processAbstractMovingElementMovement(g, ame, correctingMovement);
                            }
                        }
                    }
                }
            }
        }
    }
}
