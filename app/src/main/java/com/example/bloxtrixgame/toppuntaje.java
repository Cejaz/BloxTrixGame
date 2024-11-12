package com.example.bloxtrixgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), home.class);
                startActivity(intent);
            }
        });

        loadTopScores();
    }

    private void loadTopScores() {
        db.collection("scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<UserScore> scoreList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getId();
                            Integer score = document.getLong("score").intValue();
                            scoreList.add(new UserScore(userId, score));
                        }
                        displayScores(scoreList);
                    } else {
                        Toast.makeText(this, "Error al obtener puntajes: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayScores(List<UserScore> scoreList) {
        // Implementación para mostrar los puntajes en la interfaz.
    }

    // Clase interna para almacenar el nombre de usuario y puntaje
    private static class UserScore {
        private String userId;
        private int score;

        public UserScore(String userId, int score) {
            this.userId = userId;
            this.score = score;
        }

        public String getUserId() {
            return userId;
        }

        public int getScore() {
            return score;
        }
    }
}

