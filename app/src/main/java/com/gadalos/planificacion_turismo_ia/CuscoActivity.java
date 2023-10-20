package com.gadalos.planificacion_turismo_ia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CuscoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cusco);

        ImageButton infoButton = findViewById(R.id.imageButtonInfo);
        ImageButton retrocederButton = findViewById(R.id.imageButtonRetroceder);

        retrocederButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad principal (MainActivity)
                Intent intent = new Intent(CuscoActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a un enlace web (https://tripgo.com.pe/)
                Uri uri = Uri.parse("https://tripgo.com.pe/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}