package com.gadalos.planificacion_turismo_ia;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements MessageCallback {
    private RecyclerView recyclerView;
    private EditText edtEscribirMensaje;
    private ImageButton btnEnviar;
    private ChatAdapter chatAdapter;
    private ChatService chatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        edtEscribirMensaje = findViewById(R.id.escribirMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        ImageButton buttonBack = findViewById(R.id.btnRegreso);

        buttonBack.setOnClickListener(v -> {
            finish();
        });


        chatAdapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Message> initialMessages = new ArrayList<>();

        initialMessages.add(new Message("Hola, eres un asistente virtual de viajes", MessageRole.SYSTEM));
        initialMessages.add(new Message("Las respuestas deben ser concretas y cortas", MessageRole.SYSTEM));

        chatService = new ChatService(initialMessages, this);

        chatAdapter.addMessage(new Message("Hola, soy tu asistente virtual de viajes", MessageRole.SYSTEM));
        chatAdapter.addMessage(new Message("¿En qué te puedo ayudar?", MessageRole.SYSTEM));

        btnEnviar.setOnClickListener(v -> {
            String messageText = edtEscribirMensaje.getText().toString().trim();
            if (!messageText.isEmpty()) {
                Message userMessage = new Message(messageText, MessageRole.USER);
                chatService.sendMessage(userMessage);
                chatAdapter.addMessage(userMessage);
                chatAdapter.addLoadingMessage();
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
