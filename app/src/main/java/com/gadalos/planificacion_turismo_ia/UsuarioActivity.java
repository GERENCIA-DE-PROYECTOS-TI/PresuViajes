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
    private EditText textNombreEditar, textCorreoEditar, textCelEditar;
    private ImageView ivFotoEditar;
    public FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ImageButton imageButtonRetroceder, imageButtonEditarFoto, imageButtomGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        ivFotoEditar = (ImageView) findViewById(R.id.ivFotoEditar);

        textNombreEditar = (EditText) findViewById(R.id.textNombreEditar);
        textCorreoEditar = (EditText) findViewById(R.id.textCorreoEditar);
        textCelEditar = (EditText) findViewById(R.id.textCelEditar);

        imageButtomGuardar = (ImageButton) findViewById(R.id.imageButtomGuardar);
        imageButtonEditarFoto = (ImageButton) findViewById(R.id.imageButtonEditarFoto);
        imageButtonRetroceder = (ImageButton) findViewById(R.id.imageButtonRetroceder);

        //Conexion con el Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();



    }
}