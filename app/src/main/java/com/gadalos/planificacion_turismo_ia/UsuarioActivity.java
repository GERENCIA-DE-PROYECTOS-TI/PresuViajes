package com.gadalos.planificacion_turismo_ia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;

public class UsuarioActivity extends AppCompatActivity {
    private EditText textNombreEditar, textCorreoEditar, textCelEditar;
    private ImageView ivFotoEditar, imageView8, imageButtonEditarFoto;
    public FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ImageButton btnEditar, imageButtonRetroceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        ivFotoEditar = (ImageView) findViewById(R.id.ivFotoEditar);
        imageView8 = (ImageView) findViewById(R.id.imageView8);

        textNombreEditar = (EditText) findViewById(R.id.textNombreEditar);
        textCorreoEditar = (EditText) findViewById(R.id.textCorreoEditar);
        textCelEditar = (EditText) findViewById(R.id.textCelEditar);

        btnEditar = (ImageButton) findViewById(R.id.imageButtomGuardar);
        imageButtonRetroceder = (ImageButton) findViewById(R.id.imageButtonRetroceder);
        imageButtonEditarFoto = (ImageButton) findViewById(R.id.imageButtonEditarFoto);

        //Conexion con el Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        obtenerDatos();
        guardarDatos();
        cerrarSesion();

    }

    private void cerrarSesion() {
        imageButtonRetroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad principal (MainActivity)
                Intent intent = new Intent(UsuarioActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void obtenerDatos() {
        // Recuperar los datos del usuario actual de Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            // Inicio de sesión con correo y contraseña
            String userUid = currentUser.getUid();

            mFirestore.collection("usuario").document(userUid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String nombre = documentSnapshot.getString("nombre");
                                String correo = documentSnapshot.getString("correo");
                                String celular = documentSnapshot.getString("celular");
                                String foto = documentSnapshot.getString("foto");

                                Picasso.get().load(currentUser.getPhotoUrl()).placeholder(R.drawable.perfil_de_usuario).into(ivFotoEditar);

                                // Muestra los datos en los campos de texto
                                textNombreEditar.setText(nombre);
                                textCorreoEditar.setText(correo);
                                textCelEditar.setText(celular);
                            } else {
                                // No se encontraron datos
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error al obtener los datos
                            Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    });

            } else {
            // Verificar si el usuario inició sesión con Google
            if (currentUser.getProviderData().size() > 1) {
                // Inicio de sesión con Google
                String name = currentUser.getDisplayName();
                String email = currentUser.getEmail();
                String cel = currentUser.getPhoneNumber();

                textNombreEditar.setText(name);
                textCorreoEditar.setText(email);
                textCelEditar.setText(cel);

                Picasso.get().load(currentUser.getPhotoUrl()).placeholder(R.drawable.perfil).into(ivFotoEditar);
            }
        }
    }




    private void guardarDatos() {
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obten los nuevos valores de los campos de texto
                String nuevoNombre = textNombreEditar.getText().toString();
                String nuevoCorreo = textCorreoEditar.getText().toString();
                String nuevoTelefono = textCelEditar.getText().toString();

                // Valida que los campos no estén vacíos y que los cambios sean válidos antes de la actualización
                if (!nuevoNombre.isEmpty() && !nuevoCorreo.isEmpty() && !nuevoTelefono.isEmpty()) {
                    // Actualiza los datos del usuario en Firebase
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String userUid = currentUser.getUid();

                        // Crea un mapa con los nuevos datos
                        Map<String, Object> nuevosDatos = new HashMap<>();
                        nuevosDatos.put("nombre", nuevoNombre);
                        nuevosDatos.put("correo", nuevoCorreo);
                        nuevosDatos.put("celular", nuevoTelefono);

                        // Realiza la actualización en Firestore
                        mFirestore.collection("usuario").document(userUid)
                                .update(nuevosDatos)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UsuarioActivity.this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
                                        // Puedes redirigir al usuario a otra actividad o realizar otras acciones aquí.
                                        startActivity(new Intent(UsuarioActivity.this, HomeActivity.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // Mostrar un mensaje de error si algún campo está vacío
                    Toast.makeText(getApplicationContext(), "Completa todos los campos antes de actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}