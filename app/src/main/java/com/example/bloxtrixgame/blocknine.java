package com.example.bloxtrixgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloxtrixgame.modelos.ModeloJuegoFactory;
import com.example.bloxtrixgame.modelos.TipoJuego;
import com.example.bloxtrixgame.presentacion.EstadoJuego;
import com.example.bloxtrixgame.presentacion.TurnoJuego;
import com.example.bloxtrixgame.presentacion.presentacionjuego;
import com.example.bloxtrixgame.vistas.VistaJuegoFabrica;
import com.example.bloxtrixgame.vistas.marcojuego;

public class blocknine extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocknine);

        marcojuego gameFrame = findViewById(R.id.contenedorjuego);
        TextView gameScoreText = findViewById(R.id.puntaje);
        TextView gameStatusText = findViewById(R.id.estado);
        Button gameCtlBtn = findViewById(R.id.botonjuego_ctl);
        Button restCtlBtn = findViewById(R.id.botonreinicio_ctl);
        Button homeCtlBtn = findViewById(R.id.btnHome);
        homeCtlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        presentacionjuego gamePresenter = new presentacionjuego();
        gamePresenter.setGameModel(ModeloJuegoFactory.newGameModel(TipoJuego.BLOCKNINE));
        gamePresenter.setGameView(VistaJuegoFabrica.newGameView(gameFrame, gameScoreText, gameStatusText, gameCtlBtn, restCtlBtn, homeCtlBtn));

        Button upBtn = findViewById(R.id.arriba);
        Button downBtn = findViewById(R.id.down);
        Button leftBtn = findViewById(R.id.letf);
        Button rightBtn = findViewById(R.id.derecho);
        Button fireBtn = findViewById(R.id.fire_bton);

        upBtn.setOnClickListener(v -> gamePresenter.turn(TurnoJuego.UP));
        downBtn.setOnClickListener(v -> gamePresenter.turn(TurnoJuego.DOWN));
        leftBtn.setOnClickListener(v -> gamePresenter.turn(TurnoJuego.LEFT));
        rightBtn.setOnClickListener(v -> gamePresenter.turn(TurnoJuego.RIGHT));
        fireBtn.setOnClickListener(v -> gamePresenter.turn(TurnoJuego.FIRE));

        gameCtlBtn.setOnClickListener(v -> gamePresenter.changeStatus());
        restCtlBtn.setOnClickListener(v -> gamePresenter.restartGame());
        gamePresenter.init();

    }
}