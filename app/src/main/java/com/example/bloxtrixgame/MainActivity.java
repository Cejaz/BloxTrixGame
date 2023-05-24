package com.example.bloxtrixgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloxtrixgame.home;
import com.example.bloxtrixgame.registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.bloxtrixgame.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button ingresar = findViewById(R.id.ingresar);
        Button registrar = findViewById(R.id.registrarse);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí realizas la autenticación con Firebase
                signIn();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registro.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Ingrese un email y una contraseña válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // La autenticación fue exitosa, redirige al usuario a la siguiente actividad
                            Intent intent = new Intent(getApplicationContext(), home.class);
                            startActivity(intent);
                        } else {
                            // La autenticación falló, muestra un mensaje de error
                            Toast.makeText(MainActivity.this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
