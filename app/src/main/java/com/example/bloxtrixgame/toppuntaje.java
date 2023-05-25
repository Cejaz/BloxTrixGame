package com.example.bloxtrixgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class toppuntaje extends AppCompatActivity {
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toppuntaje);
        ImageView back = findViewById(R.id.backtop);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), home.class);
                startActivity(intent);
            }
        });

        // Obtén la referencia a la colección de usuarios en Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Consultar los datos de usuarios y mostrarlos en la tabla
        loadUserData();
    }

    private void loadUserData() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String name = userSnapshot.child("nombre").getValue(String.class);
                    int score = 0;

                    DataSnapshot scoresSnapshot = userSnapshot.child("scores");
                    if (scoresSnapshot.exists()) {
                        for (DataSnapshot scoreSnapshot : scoresSnapshot.getChildren()) {
                            Integer value = scoreSnapshot.getValue(Integer.class);
                            if (value != null) {
                                score += value;
                            }
                        }
                    }

                    if (name != null) {
                        userList.add(new User(name, score));
                    }
                }

                // Ordenar la lista de usuarios por puntaje de forma descendente
                Collections.sort(userList, (user1, user2) -> Integer.compare(user2.getScore(), user1.getScore()));

                // Mostrar los datos en la tabla
                displayUserData(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error de consulta de Firebase
            }
        });
    }

    private void displayUserData(List<User> userList) {
        // Obtener las referencias a los TextView de la vista
        TextView[] userViews = new TextView[10];
        TextView[] scoreViews = new TextView[10];

        userViews[0] = findViewById(R.id.user1);
        userViews[1] = findViewById(R.id.user2);
        userViews[2] = findViewById(R.id.user3);
        userViews[3] = findViewById(R.id.user4);
        userViews[4] = findViewById(R.id.user5);
        userViews[5] = findViewById(R.id.user6);
        userViews[6] = findViewById(R.id.user7);
        userViews[7] = findViewById(R.id.user8);
        userViews[8] = findViewById(R.id.user9);
        userViews[9] = findViewById(R.id.user10);

        scoreViews[0] = findViewById(R.id.puntaje1);
        scoreViews[1] = findViewById(R.id.puntaje2);
        scoreViews[2] = findViewById(R.id.puntaje3);
        scoreViews[3] = findViewById(R.id.puntaje4);
        scoreViews[4] = findViewById(R.id.puntaje5);
        scoreViews[5] = findViewById(R.id.puntaje6);
        scoreViews[6] = findViewById(R.id.puntaje7);
        scoreViews[7] = findViewById(R.id.puntaje8);
        scoreViews[8] = findViewById(R.id.puntaje9);
        scoreViews[9] = findViewById(R.id.puntaje10);

        // Mostrar los nombres de usuario y puntajes en los TextView correspondientes
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            userViews[i].setText(user.getName());
            scoreViews[i].setText(String.valueOf(user.getScore()));
        }
    }

    // Clase de modelo de usuario
    private static class User {
        private String name;
        private int score;

        public User(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
