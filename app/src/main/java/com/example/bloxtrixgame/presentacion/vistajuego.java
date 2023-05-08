package com.example.bloxtrixgame.presentacion;

public interface vistajuego {
     void init(int gameSize);
     void draw(Puntos[][] points);
     void setScore(int score);
     void setStatus(EstadoJuego status);
}
