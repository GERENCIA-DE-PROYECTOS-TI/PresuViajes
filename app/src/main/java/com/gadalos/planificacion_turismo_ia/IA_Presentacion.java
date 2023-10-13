package com.gadalos.planificacion_turismo_ia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class IA_Presentacion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ia_presentacion);

        Button btnContinuar = findViewById(R.id.btnContinuar);
        ImageButton btnRegreso = findViewById(R.id.btnRegreso);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para lo que sucede al hacer clic en "Continuar".
                // Para ir a la ChatActivity, inicia una nueva actividad.
                Intent intent = new Intent(IA_Presentacion.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        btnRegreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para lo que sucede al hacer clic en "Retroceder".
                // Para volver a la MainActivity, inicia una nueva actividad.
                Intent intent = new Intent(IA_Presentacion.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}