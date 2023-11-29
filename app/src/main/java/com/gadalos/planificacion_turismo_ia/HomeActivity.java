package com.gadalos.planificacion_turismo_ia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private TextView tvFrase;
    private ImageButton destino1Button;
    private ImageButton destino2Button;
    private ImageButton destino3Button;
    private ImageButton destino4Button;
    private FloatingActionButton fabButton;
    private CircleImageView btnDesplegable;
    private PopupWindow customDropdown;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Vincula los elementos de la interfaz de usuario con las variables Java
        tvFrase = findViewById(R.id.tvFrase);
        destino1Button = findViewById(R.id.imageButton1);
        destino2Button = findViewById(R.id.imageButton2);
        destino3Button = findViewById(R.id.imageButton3);
        destino4Button = findViewById(R.id.imageButton4);
        btnDesplegable = findViewById(R.id.btnDesplegable);
        fabButton = findViewById(R.id.fabButton);

        //Conexion con el Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Puedes agregar acciones a tus elementos aquí
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir MainActivity2
                Intent intent = new Intent(HomeActivity.this, IA_Presentacion.class);
                // Iniciar la actividad
                startActivity(intent);
            }
        });

        destino1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir TarapotoActivity
                Intent intent = new Intent(HomeActivity.this, TarapotoActivity.class);
                // Iniciar la actividad
                startActivity(intent);
            }
        });

        destino2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir IquitosActivity
                Intent intent = new Intent(HomeActivity.this, IquitosActivity.class);
                // Iniciar la actividad
                startActivity(intent);
            }
        });

        destino3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir VichayitosActivity
                Intent intent = new Intent(HomeActivity.this, VichayitoActivity.class);
                // Iniciar la actividad
                startActivity(intent);
            }
        });

        destino4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir CuscoActivity
                Intent intent = new Intent(HomeActivity.this, CuscoActivity.class);
                // Iniciar la actividad
                startActivity(intent);
            }
        });

        View customDropdownView = getLayoutInflater().inflate(R.layout.drawer_header, null);

        // Ajusta el ancho y alto de la ventana emergente según tus necesidades
        customDropdown = new PopupWindow(customDropdownView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        btnDesplegable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UsuarioActivity.class);
                startActivity(intent);
            }
        });

        customDropdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDropdown.dismiss();
            }
        });

        // Llamar a obtenerDatos() cuando se inicialice la vista
        obtenerDatos();
    }

    private void obtenerDatos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();

            mFirestore.collection("usuario").document(userUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String foto = documentSnapshot.getString("foto");
                            Uri fotoUri = foto != null ? Uri.parse(foto) : null;

                            // Mostrar la imagen en btnDesplegable usando Picasso
                            if (fotoUri != null) {
                                Picasso.get()
                                        .load(fotoUri)
                                        .resize(110, 110) // Redimensionar la imagen
                                        .centerCrop()
                                        .placeholder(R.drawable.perfil_de_usuario)
                                        .into(btnDesplegable);
                            }
                        } else {
                            // No se encontraron datos
                        }
                    }).addOnFailureListener(e -> {
                        // Handle errors
                    });
        }
    }

    public void cerrarSesion(View view) {
        // Agregar el código para redirigir a LoginActivity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void editar(View view) {
        // Agregar el código para redirigir a UsuarioActivity
        Intent intent = new Intent(HomeActivity.this, UsuarioActivity.class);
        startActivity(intent);
    }
}
