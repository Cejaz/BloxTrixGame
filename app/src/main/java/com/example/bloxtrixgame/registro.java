package com.example.bloxtrixgame;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthResult;

import java.util.HashMap;

public class registro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailEditText, passwordEditText, nameEditText, lastnameEditText, usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicialización de Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Enlazar vistas
        emailEditText = findViewById(R.id.emailregistro);
        passwordEditText = findViewById(R.id.passwordregistro);
        nameEditText = findViewById(R.id.editTextText);
        lastnameEditText = findViewById(R.id.editTextText2);
        usernameEditText = findViewById(R.id.editTextText3);
        Button botonRegistro = findViewById(R.id.buttonregistrar);

        // Listener para el botón de registro
        botonRegistro.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String nombre = nameEditText.getText().toString();
        String apellido = lastnameEditText.getText().toString();
        String nombreUsuario = usernameEditText.getText().toString();

        // Validación de campos
        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || nombreUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registro en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            guardarDatosUsuario(user.getUid(), nombre, apellido, nombreUsuario);
                        }
                    } else {
                        Toast.makeText(registro.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarDatosUsuario(String userId, String nombre, String apellido, String nombreUsuario) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("apellido", apellido);
        userData.put("nombreUsuario", nombreUsuario);

        // Guardar en Firestore
        db.collection("users").document(userId).set(userData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Usuario guardado en Firestore", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar usuario en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
