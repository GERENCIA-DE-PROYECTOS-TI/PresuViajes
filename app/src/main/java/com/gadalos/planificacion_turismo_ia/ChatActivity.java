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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements MessageCallback {
    private RecyclerView recyclerView;
    private EditText edtEscribirMensaje;
    private ImageButton btnEnviar, buttonBack, emojiButton;
    private ChatAdapter chatAdapter;
    private ChatService chatService;
    private LinearLayout messageLayout;
    public FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Conexion con el Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        edtEscribirMensaje = findViewById(R.id.escribirMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        emojiButton = findViewById(R.id.emojiButton);
        messageLayout = findViewById(R.id.messageLayout);
        buttonBack = findViewById(R.id.btnRegreso);

        buttonBack.setOnClickListener(v -> finish());

        chatAdapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Message> initialMessages = new ArrayList<>();
        initialMessages.add(new Message("Hola, eres un asistente virtual de viajes llamado Travis", MessageRole.SYSTEM));
        initialMessages.add(new Message("Posees solo 4 tipos de viajes en Perú: Vichayito, Tarapoto, Iquitos y Cusco", MessageRole.SYSTEM));
        initialMessages.add(new Message("Las respuestas que brindes deben ser concretas y cortas", MessageRole.SYSTEM));

        // Añadir mensajes específicos para cada destino
        initialMessages.addAll(readMessagesForDestination("vichayito"));
        initialMessages.addAll(readMessagesForDestination("cusco"));
        initialMessages.addAll(readMessagesForDestination("iquitos"));
        initialMessages.addAll(readMessagesForDestination("tarapoto"));

        chatService = new ChatService(initialMessages, this);

        // Agregar el primer mensaje después de 1 segundos
        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            chatAdapter.addMessage(new Message("Hola, soy Travis tu asistente virtual de viajes", MessageRole.SYSTEM));
        }, 1000);

        // Agregar el segundo mensaje después de 2 segundos desde el primer mensaje
        Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            chatAdapter.addMessage(new Message("¿A dónde quieres viajar?", MessageRole.SYSTEM));
        }, 2000);

        EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.messageLayout)).build(edtEscribirMensaje);
        emojiButton.setOnClickListener(view -> emojiPopup.toggle());

        btnEnviar.setOnClickListener(v -> {
            String messageText = edtEscribirMensaje.getText().toString().trim();
            if (!messageText.isEmpty()) {
                // Antes de enviar el mensaje, obtener la URL de la imagen desde Firestore
                obtenerUrlImagenFirestore(messageText);
            }
        });
    }

    private void obtenerUrlImagenFirestore(String messageText) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();

            mFirestore.collection("usuario").document(userUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fotoUrl = documentSnapshot.getString("foto");

                            // Crea el mensaje y asigna la URL de la imagen
                            Message userMessage = new Message(messageText, MessageRole.USER);
                            userMessage.setUserPhotoUrl(fotoUrl);

                            // Envía el mensaje al servicio de chat
                            chatService.sendMessage(userMessage);

                            // Actualiza la interfaz de usuario
                            chatAdapter.addMessage(userMessage);
                            chatAdapter.addLoadingMessage();
                            EmojiTextView emojiTextView = (EmojiTextView) LayoutInflater
                                    .from(getApplicationContext())
                                    .inflate(R.layout.emoji_text_view, messageLayout, false);
                            messageLayout.addView(emojiTextView);
                            edtEscribirMensaje.setText("");
                            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Maneja el error al obtener la URL de la imagen
                        // Puedes mostrar un mensaje de error o tomar alguna acción apropiada
                    });
        }
    }

    private List<Message> readMessagesForDestination(String destination) {
        int resourceId = getResources().getIdentifier(destination, "raw", getPackageName());
        return readMessagesFromRaw(resourceId);
    }

    private List<Message> readMessagesFromRaw(int rawResourceId) {
        List<Message> messages = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(rawResourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            String fileContents = stringBuilder.toString();
            String[] messagesArray = fileContents.split("---"); // Separador entre mensajes

            for (String messageText : messagesArray) {
                messages.add(new Message(messageText.trim(), MessageRole.SYSTEM));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
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
            chatAdapter.removeLoadingMessage();
            chatAdapter.addMessage(errorMessage);
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        });
    }
}
