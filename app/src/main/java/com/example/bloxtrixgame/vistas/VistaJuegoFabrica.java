package com.example.bloxtrixgame.vistas;

import android.widget.Button;
import android.widget.TextView;

import com.example.bloxtrixgame.presentacion.vistajuego;
public class VistaJuegoFabrica {
    private VistaJuegoFabrica() {
    }

    public static vistajuego vistaNuevoJuego(marcojuego marcoJuego, TextView textoPuntajeJuego, TextView textoEstadoJuego,
                                             Button botonJuego){
        return new VistaJuegoImpl(marcoJuego, textoPuntajeJuego, textoEstadoJuego,botonJuego);
    }
}
