package br.com.game.dominoes.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.game.dominoes.model.GameRules;

public class Game implements Serializable {
    private String id;
    private GameRules gameRules;
    private List<PlayerInGame> players = new ArrayList<PlayerInGame>();
    private LinkedList<DominoWithExposedSide> table = new LinkedList<DominoWithExposedSide>();
    private LinkedList<Round> rounds = new LinkedList<Round>();

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
        // TODO
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
}
