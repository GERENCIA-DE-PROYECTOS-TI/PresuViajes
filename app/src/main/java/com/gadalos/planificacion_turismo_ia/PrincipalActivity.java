package com.gadalos.planificacion_turismo_ia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrincipalActivity extends AppCompatActivity {
    private TextView tvUsuario;
    private Button btnCerrarSesion;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }
    public void cerrarSesion(){
        mAuth.signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Sesión Cerrada con Exito", Toast.LENGTH_SHORT).show();
    }
}