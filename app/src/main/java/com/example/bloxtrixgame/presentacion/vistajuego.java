package com.example.bloxtrixgame.presentacion;

public interface vistajuego {
     void iniciar(int espacioJuego);
     void dibujar(Puntos[][]puntos);
     void enviarPuntaje (int puntaje);
     void enviarEstado(EstadoJuego estado);
}
