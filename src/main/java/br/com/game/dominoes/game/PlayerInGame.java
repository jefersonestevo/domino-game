package br.com.game.dominoes.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.game.dominoes.api.PlayerStrategy;
import br.com.game.dominoes.domain.GameSide;
import br.com.game.dominoes.domain.PositionInGame;
import br.com.game.dominoes.model.Domino;
import br.com.game.dominoes.model.Player;

public abstract class PlayerInGame implements Serializable {
    private Player player;
    private PositionInGame position;
    private Integer team;
    private List<Domino> hand = new ArrayList<Domino>();

    public abstract PlayerStrategy getStrategy();

    public PlayerInGame(Player player, Integer team) {
        this.player = player;
        this.team = team;
    }

    protected PlayerMove playAMove(Domino domino, GameSide gameSide) {
        getHand().remove(domino);
        return new PlayerMove(domino, gameSide);
    }

    public Player getPlayer() {
        return player;
    }

    public PositionInGame getPosition() {
        return position;
    }

    public void setPosition(PositionInGame position) {
        this.position = position;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public List<Domino> getHand() {
        return hand;
    }

    public void cleanHand() {
        this.hand.clear();
    }

    public void giveDominoToHand(Domino domino) {
        this.hand.add(domino);
    }
}
