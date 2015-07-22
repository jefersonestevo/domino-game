package br.com.game.dominoes.game;

import java.io.Serializable;

import br.com.game.dominoes.api.PlayerStrategy;
import br.com.game.dominoes.domain.PositionInGame;
import br.com.game.dominoes.model.Domino;
import br.com.game.dominoes.model.Player;

public abstract class PlayerInGame implements Serializable {
    private Player player;
    private PositionInGame position;
    private Integer team;
    private Domino[] hand = new Domino[21];

    public abstract PlayerStrategy getStrategy();

    public PlayerInGame(Player player, Integer team) {
        this.player = player;
        this.team = team;
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

    public Domino[] getHand() {
        return hand;
    }

    public void setHand(Domino[] hand) {
        this.hand = hand;
    }
}
