package com.example.bloxtrixgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailRegistroEditText;
    private EditText passwordRegistroEditText;
    private EditText nameEditText;
    private EditText lastnameEditText;
    private EditText usernameEditText;
    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ImageView back = findViewById(R.id.back);
        Button botonRegistro = findViewById(R.id.buttonregistrar);
        emailRegistroEditText = findViewById(R.id.emailregistro);
        passwordRegistroEditText = findViewById(R.id.passwordregistro);
        nameEditText = findViewById(R.id.editTextText);
        lastnameEditText = findViewById(R.id.editTextText2);
        usernameEditText = findViewById(R.id.editTextText3);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");


        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los campos de entrada
                String email = emailRegistroEditText.getText().toString();
                String password = passwordRegistroEditText.getText().toString();

                // Validar que los campos no estén vacíos
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(registro.this, "Ingresa un email y contraseña válidos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear el usuario en Firebase Auth
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(registro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Obtener los datos del formulario de registro
                                String email = emailRegistroEditText.getText().toString();
                                String password = passwordRegistroEditText.getText().toString();
                                String nombre = nameEditText.getText().toString();
                                String apellido = lastnameEditText.getText().toString();
                                String nombreUsuario = usernameEditText.getText().toString();

// Crear un objeto HashMap para los datos del usuario
                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("nombre", nombre);
                                userData.put("apellido", apellido);
                                userData.put("nombreUsuario", nombreUsuario);

// Obtener la identificación única del usuario actual
                                String userId = mAuth.getCurrentUser().getUid();

// Guardar los datos en la base de datos
                                usersRef.child(userId).setValue(userData);

                                if (task.isSuccessful()) {
                                    // El registro fue exitoso
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                    // Aquí puedes redirigir al usuario a la siguiente actividad
                                    // Por ejemplo:
                                    Intent intent = new Intent(registro.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // El registro falló
                                    Toast.makeText(registro.this, "Error en el registro: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}