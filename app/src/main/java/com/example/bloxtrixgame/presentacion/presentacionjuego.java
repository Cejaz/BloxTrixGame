package com.example.bloxtrixgame.presentacion;

import com.example.bloxtrixgame.modelos.BlockNineModelo;

public class presentacionjuego {

    private modeloJuego mGameModel;
    private vistajuego mGameView;
    private EstadoJuego mStatus;
    private BlockNineModelo blockNineModelo; // Instancia para guardar el puntaje

    public presentacionjuego() {
        blockNineModelo = new BlockNineModelo(); // Inicializamos BlockNineModelo para guardar el puntaje
    }

    public void setGameModel(modeloJuego gameModel) {
        mGameModel = gameModel;
    }

    public void setGameView(vistajuego gameView) {
        mGameView = gameView;
    }

    public void init() {
        mGameModel.init();
        mGameView.init(mGameModel.getGameSize());
        mGameModel.setGameOverListener(this::gameOver); // Llama a gameOver cuando termina el juego
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

    public void restartGame() {
        mGameView.setScore(0);
        mGameModel.newGame();
        startGame();
    }

    private void setStatus(EstadoJuego status) {
        if (mStatus == EstadoJuego.OVER || status == EstadoJuego.START) {
            mGameModel.newGame();
        }
        mStatus = status;
        mGameView.setStatus(status);
    }

    private void gameOver() {
        setStatus(EstadoJuego.OVER);

        // Usa el método `getScore()` de `mGameModel` en lugar de `mGameView`
        int finalScore = mGameModel.getScore();
        blockNineModelo.guardarPuntajeFinal(finalScore); // Guarda el puntaje en Firestore
    }
}
