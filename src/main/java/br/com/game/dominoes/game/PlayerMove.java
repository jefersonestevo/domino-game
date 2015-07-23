package br.com.game.dominoes.game;

import java.io.Serializable;

import br.com.game.dominoes.domain.GameSide;
import br.com.game.dominoes.model.Domino;

public class PlayerMove implements Serializable {
    private PlayerInGame playerInGame;
    private Domino domino;
    private GameSide gameSide;
    private boolean passed = false;

    public PlayerMove() {
        this.passed = true;
    }

    public PlayerMove(Domino domino, GameSide gameSide) {
        this.domino = domino;
        this.gameSide = gameSide;
    }

    public PlayerInGame getPlayerInGame() {
        return playerInGame;
    }

    public void setPlayerInGame(PlayerInGame playerInGame) {
        this.playerInGame = playerInGame;
    }

    public Domino getDomino() {
        return domino;
    }

    public GameSide getGameSide() {
        return gameSide;
    }

    public void setGameSide(GameSide gameSide) {
        this.gameSide = gameSide;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    @Override
    public String toString() {
        return "PlayerMove{" +
                "playerInGame=" + playerInGame +
                ", domino=" + domino +
                ", gameSide=" + gameSide +
                ", passed=" + passed +
                '}';
    }
}
