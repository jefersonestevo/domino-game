package br.com.game.dominoes.api;

import br.com.game.dominoes.game.PlayerInGame;
import br.com.game.dominoes.game.PlayerMove;

public interface PlayerStrategy {

    PlayerMove play(int topValue, int downValue);

    PlayerMove getHighestDomino();

    void notifyPlayerMove(PlayerMove playerMove, int topValue, int downValue);

    void notifyPlayerHasBoughtADomino(PlayerInGame playerInGame);

}
