package com.example.bloxtrixgame.vistas;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloxtrixgame.presentacion.EstadoJuego;
import com.example.bloxtrixgame.presentacion.Puntos;
import com.example.bloxtrixgame.presentacion.vistajuego;

public class VistaJuegoImpl implements vistajuego {

    private final marcojuego mMarcoJuego;
    private final TextView mTextoPuntajeJuego;
    private final TextView mTextoEstadoJuego;
    private Button mBotonJuego;

    VistaJuegoImpl(marcojuego marcoJuego, TextView textoPuntajeJuego, TextView textoEstadoJuego,Button botonJuego){
        mMarcoJuego = marcoJuego;
        mTextoPuntajeJuego = textoPuntajeJuego;
        mTextoEstadoJuego = textoEstadoJuego;
        mBotonJuego = botonJuego;
    }

    @Override
    public void iniciar(int espacioJuego) {
        mMarcoJuego.inicio(espacioJuego);

    }

    @Override
    public void dibujar(Puntos[][] puntos) {

        mMarcoJuego.enviarPuntos(puntos);
        mMarcoJuego.invalidate();

    }

    @Override
    public void enviarPuntaje(int puntaje) {
        mTextoPuntajeJuego.setText("Puntaje:" + puntaje);

    }

    @Override
    public void enviarEstado(EstadoJuego estado) {
        mTextoEstadoJuego.setText(estado.ObtenerValue());
        mTextoEstadoJuego.setVisibility(estado == EstadoJuego.JUGANDO ? View.VISIBLE : View.INVISIBLE);
        mBotonJuego.setText(estado == EstadoJuego.JUGANDO ? "Pausa":"Iniciar");
    }

}
