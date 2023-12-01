package com.gadalos.planificacion_turismo_ia;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VichayitoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vichayito);

        ImageButton retrocederButton = findViewById(R.id.imageButtonRetroceder);
        ImageButton infoButton = findViewById(R.id.imageButtonInfo);
        String enlace = "https://tripgo.com.pe/vichayito-bungalows-carpas-by-aranwa/";

        retrocederButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad principal (MainActivity)
                finish();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el cuadro de diálogo emergente con WebView
                showWebViewDialog(enlace);
            }
        });
    }

    private void showWebViewDialog(String url) {
        // Crear el cuadro de diálogo emergente
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_webview);

        // Configurar el botón de cierre
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        // Configurar la barra de progreso
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.setMax(100); // El rango de progreso es de 0 a 100

        // Configurar el botón de recarga
        ImageView btnReload = dialog.findViewById(R.id.btnReload);
        btnReload.setOnClickListener(view -> {
            // Recargar la página web
            WebView webView = dialog.findViewById(R.id.webView);
            webView.reload();
        });

        // Configurar el WebView en el cuadro de diálogo
        WebView webView = dialog.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Habilitar el zoom
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); // Ocultar los controles de zoom predeterminados

        webView.setWebViewClient(new WebViewClient() {
            // Este método se llama cuando la página comienza a cargarse
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Hacer visible la barra de progreso al comenzar la carga
                progressBar.setVisibility(View.VISIBLE);
            }

            // Este método se llama cuando la página ha terminado de cargarse
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hacer invisible la barra de progreso al finalizar la carga
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        // Configurar la barra de progreso según el progreso de carga de la página
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        // Cargar la URL en el WebView
        webView.loadUrl(url);

        // Mostrar el cuadro de diálogo
        dialog.show();
    }
}