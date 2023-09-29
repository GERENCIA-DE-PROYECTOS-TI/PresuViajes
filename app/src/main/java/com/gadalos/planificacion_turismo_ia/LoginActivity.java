package com.gadalos.planificacion_turismo_ia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsuario, edtContrasena;
    private TextView tvRegistrese, tvOlvidoContrasena;
    private Button btnIniciarSesion;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtContrasena = (EditText) findViewById(R.id.edtContrasena);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciar);
        tvRegistrese = (TextView) findViewById(R.id.tvRegistrese);
        tvOlvidoContrasena = (TextView) findViewById(R.id.tvOlvidoContrasena);

        mAuth = FirebaseAuth.getInstance();
        tvRegistrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceder al Registro
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvOlvidoContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceder a Recuperar Contraseña
                Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    public void iniciarSesion(){
        //Convertimos a String debido a que debedemos comprobar si estan vacios los campos
        String username = edtUsuario.getText().toString().trim();
        String password = edtContrasena.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            //Mostramos un mensaje para ver que se llenen los campos
            Toast.makeText(LoginActivity.this, "Complete los campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            iniciarUsuario(username, password);
//            Toast.makeText(LoginActivity.this, "Prueba nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarUsuario(String username, String password) {

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    Toast.makeText(LoginActivity.this, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
                }else {
//                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al Ingresar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Si la cuenta ya esta iniciada pasar sin iniciar sesion
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
}