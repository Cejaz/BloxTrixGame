package com.example.bloxtrixgame.vistas;

import android.widget.Button;
import android.widget.TextView;

import com.example.bloxtrixgame.presentacion.vistajuego;
public class VistaJuegoFabrica {
    private VistaJuegoFabrica() {
    }

    public static vistajuego newGameView(marcojuego gameFrame, TextView gameScoreText, TextView gameStatusText, Button gameCtlBtn, Button mGameRestBtn, Button homeCtlBtn) {
        return new VistaJuegoImpl(gameFrame, gameScoreText, gameStatusText, gameCtlBtn, mGameRestBtn, homeCtlBtn);
    }
}
