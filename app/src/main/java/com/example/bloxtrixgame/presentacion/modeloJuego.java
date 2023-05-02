package com.example.bloxtrixgame.presentacion;

public interface modeloJuego {

    int FPS = 60;
    int SPEED = 25;
    void inicio();
    int obtenerEspacioJuego();
    void nuevoJuego();
    void iniciarJuego(PresenterObserver<Puntos[][]> dibujandoJuego);
    void pausarJuego();
    void voltear(TurnoJuego voltear);
    void enviarJuegoterminado(PresenterCompletableObserver juegoTerminadoActivado);
    void enviarActualizacionPuntaje(PresenterObserver<Integer> actualizacionPuntajeActivada);
}
