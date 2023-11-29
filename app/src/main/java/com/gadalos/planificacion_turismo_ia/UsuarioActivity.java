package com.gadalos.planificacion_turismo_ia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsuarioActivity extends AppCompatActivity {
    private EditText textNombreEditar, textCorreoEditar, textCelEditar;
    private ImageView ivFotoEditar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ImageButton btnEditar, imageButtonRetroceder, imageButtonEditarFoto;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        ivFotoEditar = findViewById(R.id.ivFotoEditar);
        textNombreEditar = findViewById(R.id.textNombreEditar);
        textCorreoEditar = findViewById(R.id.textCorreoEditar);
        textCelEditar = findViewById(R.id.textCelEditar);
        btnEditar = findViewById(R.id.imageButtomGuardar);
        imageButtonRetroceder = findViewById(R.id.imageButtonRetroceder);
        imageButtonEditarFoto = findViewById(R.id.imageButtonEditarFoto);

        // Conexión con Firestore y obtención de datos
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        obtenerDatos();
        cargarImagen();
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
                                String celular = documentSnapshot.getString("celular");
                                String foto = documentSnapshot.getString("foto");

                                Uri fotoUri = imageUri != null ? imageUri : (foto != null ? Uri.parse(foto) : null);

                                Picasso.get().load(fotoUri).placeholder(R.drawable.perfil_de_usuario).into(ivFotoEditar);
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
                            Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Inicio de sesión con Google
            if (currentUser.getProviderData().size() > 1) {
                String name = currentUser.getDisplayName();
                String email = currentUser.getEmail();
                String cel = currentUser.getPhoneNumber();

                textNombreEditar.setText(name);
                textCorreoEditar.setText(email);
                textCelEditar.setText(cel);

                Picasso.get().load(currentUser.getPhotoUrl()).placeholder(R.drawable.perfil_de_usuario).into(ivFotoEditar);
            }
        }
    }

    private void cargarImagen() {
        imageButtonEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Mostrar la imagen seleccionada en el ImageView
                Picasso.get().load(imageUri).into(ivFotoEditar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validarDatos(String nombre, String correo, String celular) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        String celularPattern = "^[0-9]{9}$";

        if (nombre.isEmpty() || correo.isEmpty() || celular.isEmpty()) {
            Toast.makeText(UsuarioActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!correo.matches(emailPattern)) {
            Toast.makeText(UsuarioActivity.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!celular.matches(celularPattern)) {
            Toast.makeText(UsuarioActivity.this, "Se necesita solo 9 dígitos numéricos", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void guardarDatos() {
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = textNombreEditar.getText().toString();
                String nuevoCorreo = textCorreoEditar.getText().toString();
                String nuevoCelular = textCelEditar.getText().toString();

                // Valida los datos antes de intentar la actualización
                if (validarDatos(nuevoNombre, nuevoCorreo, nuevoCelular)) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String userUid = currentUser.getUid();

                        // Crea un mapa con los nuevos datos
                        Map<String, Object> nuevosDatos = new HashMap<>();
                        nuevosDatos.put("nombre", nuevoNombre);
                        nuevosDatos.put("correo", nuevoCorreo);
                        nuevosDatos.put("celular", nuevoCelular);

                        // Realiza la actualización en Firestore
                        mFirestore.collection("usuario").document(userUid)
                                .update(nuevosDatos)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Subir la nueva imagen a Firebase Storage
                                        subirImagenAFirebaseStorage(userUid);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    private void subirImagenAFirebaseStorage(String userUid) {
        if (imageUri != null) {
            // Obtener una referencia al almacenamiento de Firebase en la ubicación específica del usuario
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("fotos_perfil").child(userUid);

            // Subir la imagen al almacenamiento de Firebase
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtener la URL de la imagen almacenada en Firebase Storage
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String nuevaFotoUrl = uri.toString();

                                    // Actualizar la URL de la foto en Firestore
                                    mFirestore.collection("usuario").document(userUid)
                                            .update("foto", nuevaFotoUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(UsuarioActivity.this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
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
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Si no se selecciona una nueva imagen, simplemente actualizar los datos en Firestore
            startActivity(new Intent(UsuarioActivity.this, HomeActivity.class));
        }
    }
}
