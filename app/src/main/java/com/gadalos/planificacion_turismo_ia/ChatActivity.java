package com.gadalos.planificacion_turismo_ia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Encuentra el botón de retroceso por su ID
        ImageButton btnRegreso = findViewById(R.id.btnRegreso);

        // Configura un OnClickListener para el botón de retroceso
        btnRegreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un Intent para iniciar la actividad de inicio (HomeActivity)
                Intent intent = new Intent(ChatActivity.this, IA_Presentacion.class);

                // Inicia la actividad de inicio (HomeActivity)
                startActivity(intent);
            }
        });
    }
}
