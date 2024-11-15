package com.example.bloxtrixgame.presentacion;

public interface modeloJuego {
    int FPS = 110;
    int SPEED = 65;

    void init();
    int getGameSize();
    void newGame();
    void startGame(PresenterObserver<Puntos[][]> onGameDrawnListener);
    void pauseGame();
    void turn(TurnoJuego turn);
    void setGameOverListener(PresenterCompletableObserver onGameOverListener);
    void setScoreUpdatedListener(PresenterObserver<Integer> onScoreUpdatedListener);

    // Agrega este método para obtener el puntaje actual
    int getScore();
}
