package com.example.bloxtrixgame;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;



import java.util.HashMap;

public class registro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Inicializa Firestore

        // Código para registrar el usuario
        registrarUsuario();
    }

    private void registrarUsuario() {
        String email = emailRegistroEditText.getText().toString();
        String password = passwordRegistroEditText.getText().toString();
        String nombre = nameEditText.getText().toString();
        String apellido = lastnameEditText.getText().toString();
        String nombreUsuario = usernameEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosUsuario(user.getUid(), nombre, apellido, nombreUsuario);
                    } else {
                        Toast.makeText(registro.this, "Error en el registro: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarDatosUsuario(String userId, String nombre, String apellido, String nombreUsuario) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("apellido", apellido);
        userData.put("nombreUsuario", nombreUsuario);

        db.collection("users").document(userId).set(userData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Usuario guardado en Firestore", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
