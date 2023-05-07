package com.example.bloxtrixgame.presentacion;

public interface modeloJuego {

    int FPS = 60;
    int SPEED = 25;
    void init();
    int getGameSize();
    void newGame();
    void startGame(PresenterObserver<Puntos[][]> onGameDrawnListener);
    void pauseGame();
    void turn(TurnoJuego turn);
    void setGameOverListener(PresenterCompletableObserver onGameOverListener);
    void setScoreUpdatedListener(PresenterObserver<Integer> onScoreUpdatedListener);

}
