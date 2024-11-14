package com.example.bloxtrixgame.modelos;

import com.example.bloxtrixgame.presentacion.PresenterCompletableObserver;
import com.example.bloxtrixgame.presentacion.PresenterObserver;
import com.example.bloxtrixgame.presentacion.Puntos;
import com.example.bloxtrixgame.presentacion.TurnoJuego;
import com.example.bloxtrixgame.presentacion.modeloJuego;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import android.util.Log;

public class BlockNineModelo implements modeloJuego {
    private FirebaseFirestore db;
    private int score;

    public BlockNineModelo() {
        db = FirebaseFirestore.getInstance();
    }

    public void guardarPuntajeFinal(int score) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            HashMap<String, Object> scoreData = new HashMap<>();
            scoreData.put("score", score);
            scoreData.put("nombreUsuario", user.getDisplayName());

            db.collection("scores").document(userId).set(scoreData)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Puntaje guardado exitosamente"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar puntaje", e));
        } else {
            Log.e("Firestore", "Usuario no autenticado");
        }
    }

    @Override
    public int getScore() {
        return score;
    }

    // Implementación de otros métodos
    @Override
    public void init() {}

    @Override
    public int getGameSize() {
        return 0;
    }

    @Override
    public void newGame() {
        score = 0;
    }

    @Override
    public void startGame(PresenterObserver<Puntos[][]> onGameDrawnListener) {}

    @Override
    public void pauseGame() {}

    @Override
    public void turn(TurnoJuego turn) {}

    @Override
    public void setGameOverListener(PresenterCompletableObserver onGameOverListener) {
        onGameOverListener.onComplete();
    }

    @Override
    public void setScoreUpdatedListener(PresenterObserver<Integer> onScoreUpdatedListener) {}
}
