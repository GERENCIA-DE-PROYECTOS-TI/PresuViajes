package com.gadalos.planificacion_turismo_ia;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class DrawerHeader extends RelativeLayout {
    public DrawerHeader(Context context) {
        super(context);
        initializeViews(context);
    }

    public DrawerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public DrawerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.drawer_header, this);

        // Obtén referencias a los botones
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSe);
        ImageButton btnEditar = findViewById(R.id.btnEditar);

        // Configura OnClickListener para el botón de "Cerrar Sesión"
        btnCerrarSesion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar el código para redirigir a LoginActivity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
            }
        });

        // Configura OnClickListener para el botón de "Editar"
        btnEditar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar el código para redirigir a UsuarioActivity
                Intent intent = new Intent(getContext(), UsuarioActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}
