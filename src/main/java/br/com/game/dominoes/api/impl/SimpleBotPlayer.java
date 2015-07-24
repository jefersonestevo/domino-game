package br.com.game.dominoes.api.impl;

import br.com.game.dominoes.api.PlayerStrategy;
import br.com.game.dominoes.domain.GameSide;
import br.com.game.dominoes.game.PlayerInGame;
import br.com.game.dominoes.game.PlayerMove;
import br.com.game.dominoes.model.Domino;
import br.com.game.dominoes.model.Player;

public class SimpleBotPlayer extends PlayerInGame {

    public SimpleBotPlayer(Player player, Integer team) {
        super(player, team);
    }

    @Override
    public PlayerStrategy getStrategy() {
        return new SimpleBotPlayerStrategy();
    }

    @Override
    public String toString() {
        return this.getPlayer().toString();
    }

    private class SimpleBotPlayerStrategy implements PlayerStrategy {
        @Override
        public PlayerMove play(int topValue, int downValue) {
            // TODO - Improve intelligence
            for (Domino domino : getHand()) {
                if (domino.getUpValue().equals(topValue) || domino.getDownValue().equals(topValue)) {
                    return playAMove(domino, GameSide.UP);
                }
                if (domino.getUpValue().equals(downValue) || domino.getDownValue().equals(downValue)) {
                    return playAMove(domino, GameSide.DOWN);
                }
            }
            return new PlayerMove();
        }

        @Override
        public PlayerMove getHighestDomino() {
            Domino highest = null;
            int highestValue = Integer.MIN_VALUE;
            for (Domino domino : getHand()) {
                if (domino.getSummedValue() > highestValue) {
                    if (highest == null || !highest.isSameValue() || domino.isSameValue()) {
                        highest = domino;
                        highestValue = domino.getSummedValue();
                    }
                }
            }
            return playAMove(highest, GameSide.BOTH);
        }

        @Override
        public void notifyPlayerMove(PlayerMove playerMove, int topValue, int downValue) {

        }

        @Override
        public void notifyPlayerHasBoughtADomino(PlayerInGame playerInGame) {

        }
    }

}
