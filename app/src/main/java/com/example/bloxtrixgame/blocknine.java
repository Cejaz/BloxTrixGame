package com.example.bloxtrixgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloxtrixgame.modelos.TipoJuego;
import com.example.bloxtrixgame.presentacion.ModeloJuegoFactory;
import com.example.bloxtrixgame.presentacion.TurnoJuego;
import com.example.bloxtrixgame.presentacion.presentacionjuego;
import com.example.bloxtrixgame.vistas.VistaJuegoFabrica;
import com.example.bloxtrixgame.vistas.marcojuego;

@SuppressLint("MissingInflatedId")
public class blocknine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocknine);

        marcojuego marcoJuego = findViewById(R.id.contenedorjuego);
        TextView textoPuntajeJuego = findViewById(R.id.puntaje);
        TextView textoEstadoJuego = findViewById(R.id.estado);
        Button botonJuego = findViewById(R.id.botonjuego_ctl);

        presentacionjuego juegoPresentacion = new presentacionjuego();
        juegoPresentacion.enviaModeloJuego(ModeloJuegoFactory.nuevoModeloJuego(TipoJuego.BLOCKNINE));
        juegoPresentacion.enviaVistaJuego(VistaJuegoFabrica.vistaNuevoJuego(marcoJuego,textoPuntajeJuego,
                textoEstadoJuego,botonJuego));

        Button arriba = findViewById(R.id.arriba);
        Button abajo = findViewById(R.id.down);
        Button izquierda = findViewById(R.id.letf);
        Button derecha = findViewById(R.id.derecho);
        Button fire = findViewById(R.id.fire_bton);

        arriba.setOnClickListener(v -> juegoPresentacion.voltear(TurnoJuego.ARRIBA));
        abajo.setOnClickListener(v -> juegoPresentacion.voltear(TurnoJuego.ABAJO));
        izquierda.setOnClickListener(v -> juegoPresentacion.voltear(TurnoJuego.IZQUIERDA));
        derecha.setOnClickListener(v -> juegoPresentacion.voltear(TurnoJuego.DERECHA));
        fire.setOnClickListener(v -> juegoPresentacion.voltear(TurnoJuego.FIRE));

        botonJuego.setOnClickListener(v -> juegoPresentacion.cambiarEstado());

        juegoPresentacion.iniciar();


    }
}