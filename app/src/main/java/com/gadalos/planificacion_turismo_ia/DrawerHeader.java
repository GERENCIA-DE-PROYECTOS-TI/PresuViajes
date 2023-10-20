package com.gadalos.planificacion_turismo_ia;

import android.content.Context;
import android.content.Intent;
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

public class DrawerHeader extends RelativeLayout {
    private TextView textNombrePerfil, textCorreoPerfil, textCelPerfil;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public DrawerHeader(Context context) {
        super(context);
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore
        initializeViews(context);
        // Llamar a obtenerDatos() cuando se inicialice la vista
        obtenerDatos(context);
    }

    public DrawerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore
        initializeViews(context);
        // Llamar a obtenerDatos() cuando se inicialice la vista
        obtenerDatos(context);
    }

    public DrawerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore
        initializeViews(context);
        // Llamar a obtenerDatos() cuando se inicialice la vista
        obtenerDatos(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.drawer_header, this);

        ImageView ivFotoPerfil;
        textNombrePerfil = findViewById(R.id.textNombrePerfil);
        textCorreoPerfil = findViewById(R.id.textCorreoPerfil);
        textCelPerfil = findViewById(R.id.textCelPerfil);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSe);
        ImageButton btnEditar = findViewById(R.id.btnEditar);

        // Configurar OnClickListener para el botón de "Cerrar Sesión"
        btnCerrarSesion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private void obtenerDatos(Context context) {
        // Recuperar los datos del usuario actual de Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();

            mFirestore.collection("usuario").document(userUid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String nombre = documentSnapshot.getString("nombre");
                                String correo = documentSnapshot.getString("correo");
                                String telefono = documentSnapshot.getString("telefono");

                                // Muestra los datos en los campos de texto
                                textNombrePerfil.setText(nombre);
                                textCorreoPerfil.setText(correo);
                                textCelPerfil.setText(telefono);
                            } else {
                                // No se encontraron datos
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error al obtener los datos
                            Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
