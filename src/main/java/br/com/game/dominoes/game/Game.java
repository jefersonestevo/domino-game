package br.com.game.dominoes.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.game.dominoes.domain.GameSide;
import br.com.game.dominoes.model.Domino;
import br.com.game.dominoes.model.GameRules;

public class Game implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    private String id;
    private GameRules gameRules;
    private List<PlayerInGame> players = new ArrayList<PlayerInGame>();
    private LinkedList<Domino> extraDominoes = new LinkedList<Domino>();
    private LinkedList<DominoWithExposedSide> table = new LinkedList<DominoWithExposedSide>();
    private LinkedList<Round> rounds = new LinkedList<Round>();

    private PlayerInGame winner = null;

    protected boolean tableHasChangedOnTheLastRound = false;
    protected int lastPlayer;

    public Game(String id, GameRules gameRules, PlayerInGame initialPlayer) {
        this.id = id;
        this.gameRules = gameRules;
        this.players.add(initialPlayer);
    }

    public void addPlayer(PlayerInGame playerInGame) {
        if (this.players.size() > 3) {
            throw new IllegalArgumentException("Cannot have more than 4 players");
        }
        this.players.add(playerInGame);
    }

    public void startGame() {
        if (winner != null) {
            throw new IllegalStateException("This game has already been finished");
        }

        giveDominoesToPlayers();

        Round lastRound = null;
        firstMove();
        while (!hasWinner()) {
            if (hasEveryonePassedLastRound(lastRound)) {
                log("Everyone has passe the round {}", lastRound.getNumber());
                if (changeTableSides()) {
                    tableHasChangedOnTheLastRound = true;
                }
                else {
                    defineWinner(true);
                    log("Cannot change the table sides in round {}", lastRound.getNumber());
                    break;
                }
            } else {
                tableHasChangedOnTheLastRound = false;
            }

            Round round = new Round(rounds.size() + 1);
            log("Starting round {}", round.getNumber());
            for (int i = 0; i < players.size(); i++) {
                PlayerInGame player = getNextPlayer();
                int[] exposedValues = getExposedValuesInTable();
                log("Player {} is gonna play on upValue={} and downValue={}", player.getPlayer().getNickName(),
                        exposedValues[0], exposedValues[1]);
                PlayerMove move = makePlayerMove(player, exposedValues[0], exposedValues[1]);
                // TODO - Check if the rules let buy dominos and buy it
                notifyPlayerMove(player, move);
                round.addPlayerMove(move);
                log("Table:");
                for (int j = this.table.size() - 1; j >= 0; j--) {
                    DominoWithExposedSide dominoes = this.table.get(j);
                    log("   {}", dominoes.getDomino().getId());
                }
            }
            lastRound = round;
            this.rounds.add(round);
        }

        log("Winner = {}", winner);
    }

    protected PlayerMove makePlayerMove(PlayerInGame player, int exposedValueUp, int exposedValueDown) {
        PlayerMove move = player.getStrategy().play(exposedValueUp, exposedValueDown);
        move.setPlayerInGame(player);
        log("Player move={}", move);
        if (move.isPassed()) {
            log("Player ({}) passed the move", player);
            if (gameRules.isCanBuy() && this.extraDominoes.size() > 0) {
                log("Player ({}) will buy a new domino", player);
                Domino domino = this.extraDominoes.pollFirst();
                player.giveDominoToHand(domino);
                return makePlayerMove(player, exposedValueUp, exposedValueDown);
            }
        } else {
            addMoveToTable(move);
        }
        return move;
    }

    protected void giveDominoesToPlayers() {
        LinkedList<Domino> shuffledDominoes = new LinkedList<Domino>(Arrays.asList(Domino.getDominoes()));

        Collections.shuffle(shuffledDominoes);

        for (PlayerInGame player : players) {
            for (int i = 0; i < 7; i++) {
                player.giveDominoToHand(shuffledDominoes.pollFirst());
            }
        }

        extraDominoes = shuffledDominoes;

        for (PlayerInGame player : players) {
            log("Player {} with hand: ", player.getPlayer().getNickName());
            for (Domino domino : player.getHand()) {
                log("   {}", domino.getId());
            }
        }
    }

    private boolean changeTableSides() {
        if (gameRules.isCanClose() || tableHasChangedOnTheLastRound) {
            return false;
        }

        log("Checking if we can change the table");
        DominoWithExposedSide downSide = this.table.getFirst();
        DominoWithExposedSide upSide = this.table.getLast();

        int upExposedValue = getExposedSideValue(upSide, true);
        int downExposedValue = getExposedSideValue(downSide, true);

        if (upExposedValue == downExposedValue) {
            log("Changing the domino {} to the top", downSide.getDomino().getId());
            downSide.setExposedSide(downSide.getExposedSide().getOpposite());

            this.table.removeFirst();
            this.table.addLast(downSide);
            return true;
        }
        log("Cannot change the table");
        return false;
    }

    private boolean hasEveryonePassedLastRound(Round lastRound) {
        if (lastRound == null) {
            return false;
        }
        for (PlayerMove playerMove : lastRound.getPlayerMoves()) {
            if (!playerMove.isPassed()) {
                return false;
            }
        }
        return true;
    }

    protected void firstMove() {
        log("Game First Move");
        PlayerMove highestMove = null;
        int highestValue = Integer.MIN_VALUE;
        for (PlayerInGame player : players) {
            PlayerMove move = player.getStrategy().getHighestDomino();
            move.setPlayerInGame(player);
            if (move.getDomino().getSummedValue() > highestValue) {
                highestMove = move;
                highestValue = move.getDomino().getSummedValue();
            }
        }

        PlayerInGame player = highestMove.getPlayerInGame();
        log("Player ({}) has the first move with domino ({})", player.getPlayer().getNickName(),
                highestMove.getDomino().getId());
        lastPlayer = this.players.indexOf(player);
        highestMove.setGameSide(GameSide.BOTH);
        highestMove.setPlayerInGame(player);
        addMoveToTable(highestMove);

//        for (int i = 0; i < players.size(); i++) {
//            PlayerInGame player = players.get(i);
//            PlayerMove move = player.getStrategy().getHighestDomino();
//            if (move != null) {
//                log("Player ({}) has the first move with domino ({})", player.getPlayer().getNickName(),
//                        move.getDomino().getId());
//                lastPlayer = i;
//                move.setGameSide(GameSide.BOTH);
//                move.setPlayerInGame(player);
//                addMoveToTable(move);
//                return;
//            }
//        }
    }

    protected boolean hasWinner() {
        defineWinner(false);
        return winner != null;
    }

    protected void isValidMove(PlayerMove playerMove) {
        // TODO
    }

    protected PlayerInGame getNextPlayer() {
        if (lastPlayer >= this.players.size() - 1) {
            lastPlayer = -1;
        }
        return this.players.get(++lastPlayer);
    }

    protected void addMoveToTable(PlayerMove move) {
        DominoWithExposedSide newDominoWithExposedSide = null;
        switch (move.getGameSide()) {
            case BOTH:
                newDominoWithExposedSide = new DominoWithExposedSide(move.getDomino(), GameSide.BOTH);
                this.table.add(newDominoWithExposedSide);
                break;
            case DOWN:
                DominoWithExposedSide exposedDownSide = this.table.getFirst();
                if (move.getDomino().getUpValue().equals(getExposedSideValue(exposedDownSide, false))) {
                    newDominoWithExposedSide = new DominoWithExposedSide(move.getDomino(), GameSide.DOWN);
                }
                else if (move.getDomino().getDownValue().equals(getExposedSideValue(exposedDownSide, false))) {
                    newDominoWithExposedSide = new DominoWithExposedSide(move.getDomino(), GameSide.UP);
                }
                if (newDominoWithExposedSide == null) {
                    throw new IllegalStateException("Domino cannot be null");
                }
                this.table.addFirst(newDominoWithExposedSide);
                break;
            case UP:
                DominoWithExposedSide exposedUpSide = this.table.getLast();
                if (move.getDomino().getUpValue().equals(getExposedSideValue(exposedUpSide, true))) {
                    newDominoWithExposedSide = new DominoWithExposedSide(move.getDomino(), GameSide.DOWN);
                }
                else if (move.getDomino().getDownValue().equals(getExposedSideValue(exposedUpSide, true))) {
                    newDominoWithExposedSide = new DominoWithExposedSide(move.getDomino(), GameSide.UP);
                }
                if (newDominoWithExposedSide == null) {
                    throw new IllegalStateException("Domino cannot be null");
                }
                this.table.addLast(newDominoWithExposedSide);
                break;
        }
    }

    private Integer getExposedSideValue(DominoWithExposedSide exposedSide, boolean isUpValue) {
        switch (exposedSide.getExposedSide()) {
            case UP:
                return exposedSide.getDomino().getUpValue();
            case DOWN:
                return exposedSide.getDomino().getDownValue();
            case BOTH:
                if (isUpValue) {
                    return exposedSide.getDomino().getUpValue();
                } else {
                    return exposedSide.getDomino().getDownValue();
                }
        }
        throw new IllegalStateException("Domino with exposed side cannot be null");
    }

    protected int[] getExposedValuesInTable() {
        DominoWithExposedSide downDomino = this.table.getFirst();
        DominoWithExposedSide upDomino = this.table.getLast();

        int upValue = selectValue(upDomino, true);
        int downValue = selectValue(downDomino, false);

        return new int[] { upValue, downValue };
    }

    private int selectValue(DominoWithExposedSide dominoWithExposedSide, boolean isUpValue) {
        Integer value = null;
        switch (dominoWithExposedSide.getExposedSide()) {
            case UP:
                value = dominoWithExposedSide.getDomino().getUpValue();
                break;
            case DOWN:
                value = dominoWithExposedSide.getDomino().getDownValue();
                break;
            case BOTH:
                if (isUpValue) {
                    value = dominoWithExposedSide.getDomino().getUpValue();
                } else {
                    value = dominoWithExposedSide.getDomino().getDownValue();
                }
                break;
        }
        return value;
    }

    protected void notifyPlayerMove(PlayerInGame actualPlayer, PlayerMove playerMove) {
        int[] exposedValues = getExposedValuesInTable();
        for (PlayerInGame player : players) {
            if (!actualPlayer.equals(player)) {
                player.getStrategy().notifyPlayerMove(playerMove, exposedValues[0], exposedValues[1]);
            }
        }
    }

    public void defineWinner(boolean countHands) {
        PlayerInGame internalWinner = null;
        for (PlayerInGame player : players) {
            if (player.getHand().size() == 0) {
                internalWinner = player;
                break;
            }
        }

        if (internalWinner == null && countHands) {
            log("Defining winner by counting hands");
            Integer value = Integer.MAX_VALUE;
            for (PlayerInGame player : players) {
                Integer valueOfPlayer = 0;
                for (Domino domino : player.getHand()) {
                    valueOfPlayer += domino.getUpValue();
                    valueOfPlayer += domino.getDownValue();
                }
                if (valueOfPlayer < value) {
                    internalWinner = player;
                    value = valueOfPlayer;
                }
            }
        }

        this.winner = internalWinner;
    }

    public String getId() {
        return id;
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    public List<PlayerInGame> getPlayers() {
        return new ArrayList<PlayerInGame>(players);
    }

    public LinkedList<DominoWithExposedSide> getTable() {
        return new LinkedList<DominoWithExposedSide>(table);
    }

    public LinkedList<Round> getRounds() {
        return new LinkedList<Round>(rounds);
    }

    public PlayerInGame getWinner() {
        return winner;
    }

    private static void log(String message, Object... params) {
        LOG.info(message, params);
    }
}
