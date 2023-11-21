package com.gadalos.planificacion_turismo_ia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
                customDropdown.showAsDropDown(btnDesplegable);
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
        // Recuperar datos del Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String nombre = bundle.getString("nombre");
            String correo = bundle.getString("correo");
            String cel    = bundle.getString("celular");
            String fotoUrl = bundle.getString("foto");

            // Aquí puedes usar los datos según sea necesario
            // Por ejemplo, para mostrar la imagen en btnDesplegable usando Picasso
            if (fotoUrl != null) {
                Picasso.get().load(fotoUrl).into(btnDesplegable);
            }else {
                btnDesplegable.setImageResource(R.drawable.perfil_de_usuario);
            }
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
