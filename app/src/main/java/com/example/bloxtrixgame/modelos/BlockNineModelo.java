package com.example.bloxtrixgame.modelos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import android.util.Log;

public class BlockNineModelo implements modeloJuego { // o extends modeloJuego si es una clase
    private FirebaseFirestore db;

    public BlockNineModelo() {
        db = FirebaseFirestore.getInstance(); // Inicializa Firestore
    }

    public void guardarPuntajeFinal(int score) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            HashMap<String, Object> scoreData = new HashMap<>();
            scoreData.put("score", score);

            db.collection("scores").document(userId).set(scoreData)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Puntaje guardado exitosamente"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar puntaje", e));
        }
    }
}
