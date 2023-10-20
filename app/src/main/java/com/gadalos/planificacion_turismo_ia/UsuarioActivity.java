package com.gadalos.planificacion_turismo_ia;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsuarioActivity extends AppCompatActivity {
    private TextView txtUsuario, textNomComp, textCorreo, textCel;
    private ImageView imageView;
    public FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ImageButton btnEditar, imageButtonRetroceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        imageView = (ImageView) findViewById(R.id.imageView);

        txtUsuario = (TextView) findViewById(R.id.textNombre);
        textNomComp = (TextView) findViewById(R.id.textNomComp);
        textCorreo = (TextView) findViewById(R.id.textCorreo);
        textCel = (TextView) findViewById(R.id.textCel);

        btnEditar = (ImageButton) findViewById(R.id.btnEditar);
        imageButtonRetroceder = (ImageButton) findViewById(R.id.imageButtonRetroceder);

        //Conexion con el Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();



    }
}