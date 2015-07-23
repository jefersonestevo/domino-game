package br.com.game.dominoes.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Round implements Serializable {
    private Integer number;
    private List<PlayerMove> playerMoves = new ArrayList<PlayerMove>();

    public Round(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public List<PlayerMove> getPlayerMoves() {
        return new ArrayList<PlayerMove>(playerMoves);
    }

    public void addPlayerMove(PlayerMove playerMove) {
        this.playerMoves.add(playerMove);
    }
}
