package com.gadalos.planificacion_turismo_ia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsuario, edtContrasena;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtContrasena = (EditText) findViewById(R.id.edtContrasena);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciar);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    public void iniciarSesion(){
        //Convertimos a String debido a que debedemos comprobar si estan vacios los campos
        String username = edtUsuario.getText().toString();
        String password = edtContrasena.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            //Mostramos un mensaje para ver que se llenen los campos
            Toast.makeText(LoginActivity.this, "Complete los campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            if (username.equals("dcatunta") && password.equals("987654")){
                Toast.makeText(LoginActivity.this, "Exito al Validar los Datos", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "Error al Iniciar Sesión", Toast.LENGTH_SHORT).show();
            }
        }
    }
}