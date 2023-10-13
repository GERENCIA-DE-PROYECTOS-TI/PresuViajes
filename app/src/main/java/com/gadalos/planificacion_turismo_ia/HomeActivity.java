package com.gadalos.planificacion_turismo_ia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    private TextView tvFrase;
    private ImageButton destino1Button;
    private ImageButton destino2Button;
    private ImageButton destino3Button;
    private ImageButton destino4Button;
    private ImageButton usuarioButton;
    private FloatingActionButton fabButton;

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
        usuarioButton = findViewById(R.id.ivPerfilUsuario);
        fabButton = findViewById(R.id.fabButton);

        // Puedes agregar acciones a tus elementos aquÃ­


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir MainActivity2
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);

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
        usuarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una Intent para abrir MainActivity2
                Intent intent = new Intent(HomeActivity.this, UsuarioActivity.class);

                // Iniciar la actividad
                startActivity(intent);
            }
        });
    }
}

