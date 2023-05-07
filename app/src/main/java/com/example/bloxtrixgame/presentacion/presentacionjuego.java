package com.example.bloxtrixgame.presentacion;

public class presentacionjuego {

    private modeloJuego mGameModel;
    private vistajuego mGameView;
    private EstadoJuego mStatus;

    public void setGameModel(modeloJuego gameModel) {
        mGameModel = gameModel;
    }

    public void setGameView(vistajuego gameView) {
        mGameView = gameView;
    }

    public void init() {
        mGameModel.init();
        mGameView.init(mGameModel.getGameSize());
        mGameModel.setGameOverListener(() -> setStatus(EstadoJuego.OVER));
        mGameModel.setScoreUpdatedListener(mGameView::setScore);
        setStatus(EstadoJuego.START);
    }

    public void turn(TurnoJuego turn) {
        mGameModel.turn(turn);
    }

    public void changeStatus() {
        if (mStatus == EstadoJuego.PLAYING) {
            pauseGame();
        } else {
            startGame();
        }
    }

    private void pauseGame() {
        setStatus(EstadoJuego.PAUSED);
        mGameModel.pauseGame();
    }

    private void startGame() {
        setStatus(EstadoJuego.PLAYING);
        mGameModel.startGame(mGameView::draw);
    }

    private void setStatus(EstadoJuego status) {
        if (mStatus == EstadoJuego.OVER || status == EstadoJuego.START) {
            mGameModel.newGame();
        }
        mStatus = status;
        mGameView.setStatus(status);
    }
}
