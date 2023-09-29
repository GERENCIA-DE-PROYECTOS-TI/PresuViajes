package com.gadalos.planificacion_turismo_ia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    private EditText edtCorreo2;
    private Button btnEnviarEnlace, btnCrearCuentaNueva, btnVolverInicioSesion;
    private ProgressDialog progress;
    private String s_correo;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        mAuth = FirebaseAuth.getInstance();
        edtCorreo2 = (EditText) findViewById(R.id.edtCorreo2);
        btnEnviarEnlace = (Button) findViewById(R.id.btnEnviarEnlace);
        btnCrearCuentaNueva = (Button) findViewById(R.id.btnCrearCuentaNueva);
        btnVolverInicioSesion = (Button) findViewById(R.id.btnVolverInicioSesion);
        progress = new ProgressDialog(this);

        enviarEnlace();

    }

    public void enviarEnlace() {

        btnEnviarEnlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_correo = edtCorreo2.getText().toString().trim();

                if (s_correo.isEmpty()){
                    Toast.makeText(RecuperarContrasenaActivity.this, "Ingrese su correo", Toast.LENGTH_SHORT).show();
                }else {
                    progress.setMessage("Espere un momento..");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    recuperarContraseña();
                }
            }
        });

    }

    private void recuperarContraseña() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(s_correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RecuperarContrasenaActivity.this, "Revise su correo, se ha enviado un enlace para restaurar su contraseña", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecuperarContrasenaActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(RecuperarContrasenaActivity.this, "El correo no se pudo enviar 😅", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}