package com.example.bloxtrixgame.vistas;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloxtrixgame.presentacion.EstadoJuego;
import com.example.bloxtrixgame.presentacion.Puntos;
import com.example.bloxtrixgame.presentacion.vistajuego;

public class VistaJuegoImpl implements vistajuego {

    private final marcojuego mGameFrame;
    private final TextView mGameScoreText;
    private final TextView mGameStatusText;
    private final Button mGameCtlBtn;

    VistaJuegoImpl(marcojuego gameFrame, TextView gameScoreText, TextView gameStatusText, Button gameCtlBtn) {
        mGameFrame = gameFrame;
        mGameScoreText = gameScoreText;
        mGameStatusText = gameStatusText;
        mGameCtlBtn = gameCtlBtn;
    }

    @Override
    public void init(int gameSize) {
        mGameFrame.init(gameSize);
    }

    @Override
    public void draw(Puntos[][] points) {
        mGameFrame.setPoints(points);
        mGameFrame.invalidate();
    }

    @Override
    public void setScore(int score) {
        mGameScoreText.setText("Score: " + score);
    }

    @Override
    public void setStatus(EstadoJuego status) {
        mGameStatusText.setText(status.getValue());
        mGameStatusText.setVisibility(status == EstadoJuego.PLAYING ? View.INVISIBLE : View.VISIBLE);
        mGameCtlBtn.setText(status == EstadoJuego.PLAYING ? "Pause" : "Start");
    }
}
