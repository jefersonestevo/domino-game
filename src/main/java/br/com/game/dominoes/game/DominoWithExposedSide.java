package br.com.game.dominoes.game;

import br.com.game.dominoes.domain.GameSide;
import br.com.game.dominoes.model.Domino;

public class DominoWithExposedSide {
    private Domino domino;
    private GameSide exposedSide;

    public DominoWithExposedSide(Domino domino, GameSide exposedSide) {
        this.domino = domino;
        this.exposedSide = exposedSide;
    }

    public Domino getDomino() {
        return domino;
    }

    public GameSide getExposedSide() {
        return exposedSide;
    }

    public void setExposedSide(GameSide exposedSide) {
        this.exposedSide = exposedSide;
    }

}
