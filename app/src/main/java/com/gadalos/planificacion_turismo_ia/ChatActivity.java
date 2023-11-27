package com.gadalos.planificacion_turismo_ia;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements MessageCallback {
    private RecyclerView recyclerView;
    private EditText edtEscribirMensaje;
    private ImageButton btnEnviar,buttonBack, emojiButton;
    private ChatAdapter chatAdapter;
    private ChatService chatService;
    private LinearLayout messageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        edtEscribirMensaje = findViewById(R.id.escribirMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        emojiButton = (ImageButton) findViewById(R.id.emojiButton);
        messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
        buttonBack = findViewById(R.id.btnRegreso);

        buttonBack.setOnClickListener(v -> {
            finish();
        });


        chatAdapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Message> initialMessages = new ArrayList<>();

        initialMessages.add(new Message("Hola, eres un asistente virtual de viajes para  llamado travis", MessageRole.SYSTEM));
        initialMessages.add(new Message("Posees solo 4 tipos de viajes en Peru y son Vichayito, Tarapoto, Iquitos y Cusco", MessageRole.SYSTEM));
        initialMessages.add(new Message("Las respuestas que brindes deben ser concretas y cortas", MessageRole.SYSTEM));

        chatService = new ChatService(initialMessages, this);

        // Agregar el primer mensaje después de 1 segundos
        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            chatAdapter.addMessage(new Message("Hola, soy Travis tu asistente virtual de viajes", MessageRole.SYSTEM));
        }, 1000);

        // Agregar el segundo mensaje después de 2 segundos desde el primer mensaje
        Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            chatAdapter.addMessage(new Message("¿A donde quieres viajar?", MessageRole.SYSTEM));
        }, 2000);

        EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.messageLayout)).build(edtEscribirMensaje);
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });

        btnEnviar.setOnClickListener(v -> {
            String messageText = edtEscribirMensaje.getText().toString().trim();
            if (!messageText.isEmpty()) {
                Message userMessage = new Message(messageText, MessageRole.USER);
                chatService.sendMessage(userMessage);
                chatAdapter.addMessage(userMessage);
                chatAdapter.addLoadingMessage();
                EmojiTextView emojiTextView = (EmojiTextView) LayoutInflater
                        .from(v.getContext())
                        .inflate(R.layout.emoji_text_view, messageLayout, false);
                messageLayout.addView(emojiTextView);
                edtEscribirMensaje.setText("");
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }

    @Override
    public void onMessageReceived(Message message) {
        runOnUiThread(() -> {
            chatAdapter.removeLoadingMessage();
            chatAdapter.addMessage(message);
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        });
    }

    @Override
    public void onMessageError(String error) {
        runOnUiThread(() -> {
            Message errorMessage = new Message("Error: " + error, MessageRole.SYSTEM);
            chatAdapter.addMessage(errorMessage);
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        });
    }
}
