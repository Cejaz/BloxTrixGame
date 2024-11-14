package com.example.bloxtrixgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class toppuntaje extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toppuntaje);

        db = FirebaseFirestore.getInstance();
        ImageView back = findViewById(R.id.backtop);
        cargarPuntajes();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), home.class);
                startActivity(intent);
            }
        });
    }

    private void cargarPuntajes() {
        db.collection("scores").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> topScores = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String username = document.getString("nombreUsuario");
                    int score = document.getLong("score").intValue();
                    topScores.add(username + ": " + score);
                }
                mostrarPuntajes(topScores);
            } else {
                Toast.makeText(this, "Error al obtener puntajes: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarPuntajes(List<String> scores) {
        TextView[] userViews = {
                findViewById(R.id.user1),
                findViewById(R.id.user2),
                findViewById(R.id.user3),
                findViewById(R.id.user4),
                findViewById(R.id.user5),
                findViewById(R.id.user6),
                findViewById(R.id.user7),
                findViewById(R.id.user8),
                findViewById(R.id.user9),
                findViewById(R.id.user10)
        };

        TextView[] scoreViews = {
                findViewById(R.id.puntaje1),
                findViewById(R.id.puntaje2),
                findViewById(R.id.puntaje3),
                findViewById(R.id.puntaje4),
                findViewById(R.id.puntaje5),
                findViewById(R.id.puntaje6),
                findViewById(R.id.puntaje7),
                findViewById(R.id.puntaje8),
                findViewById(R.id.puntaje9),
                findViewById(R.id.puntaje10)
        };

        for (int i = 0; i < scores.size() && i < userViews.length; i++) {
            String[] parts = scores.get(i).split(": ");
            userViews[i].setText(parts[0]); // Asigna el nombre de usuario
            scoreViews[i].setText(parts[1]); // Asigna el puntaje
        }
    }
}
