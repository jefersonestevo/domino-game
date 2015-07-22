package br.com.game.dominoes.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Round implements Serializable {
    private Integer number;
    private List<PlayerMove> playerMoves;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<PlayerMove> getPlayerMoves() {
        return new ArrayList<PlayerMove>(playerMoves);
    }

    public void setPlayerMoves(List<PlayerMove> playerMoves) {
        this.playerMoves = playerMoves;
    }
}
