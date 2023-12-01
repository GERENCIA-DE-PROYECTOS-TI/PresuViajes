package com.gadalos.planificacion_turismo_ia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DrawerHeader extends RelativeLayout {
    private TextView textNombrePerfil, textCorreoPerfil, textCelPerfil;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ImageView ivFotoPerfil;

    public DrawerHeader(Context context) {
        super(context);
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore
        initializeViews(context);
        // Llamar a obtenerDatos() cuando se inicialice la vista
        //obtenerDatos(context);
    }

    public DrawerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore
        initializeViews(context);
        // Llamar a obtenerDatos() cuando se inicialice la vista
        //obtenerDatos(context);
    }

    public DrawerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore
        initializeViews(context);
        // Llamar a obtenerDatos() cuando se inicialice la vista
        //obtenerDatos(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.drawer_header, this);

        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        textNombrePerfil = findViewById(R.id.textNombrePerfil);
        textCorreoPerfil = findViewById(R.id.textCorreoPerfil);
        textCelPerfil = findViewById(R.id.textCelPerfil);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSe);
        ImageButton btnEditar = findViewById(R.id.btnEditar);

        // Configurar OnClickListener para el botón de "Cerrar Sesión"
        btnCerrarSesion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar la sesión
                mAuth.signOut();

                // Agregar el código para redirigir a LoginActivity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);


            }
        });

        // Configurar OnClickListener para el botón de "Editar"
        btnEditar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar el código para redirigir a UsuarioActivity
                Intent intent = new Intent(getContext(), UsuarioActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
    /*private void obtenerDatos(Context context) {
        // Obtener el Intent asociado con la Activity actual
        Intent intent = ((Activity) context).getIntent();

        // Verificar si el Intent tiene datos adicionales
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();

            // Obtener los datos del Bundle
            String nombre = bundle.getString("nombre", "");
            String correo = bundle.getString("correo", "");
            String photoUrl = bundle.getString("photoUrl", "");

            // Hacer lo que necesites con estos datos, como mostrarlos en TextViews
            textNombrePerfil.setText(nombre);
            textCorreoPerfil.setText(correo);

            // Aquí puedes usar una biblioteca como Picasso para cargar la imagen desde la URL
            // (esto es solo un ejemplo, ajusta según tu implementación)
            if (!photoUrl.isEmpty()) {
                Picasso.get().load(photoUrl).into(ivFotoPerfil);
            }
        }
    }*/

}
