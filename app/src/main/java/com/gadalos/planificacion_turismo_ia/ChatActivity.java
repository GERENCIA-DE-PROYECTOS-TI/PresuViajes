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

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements MessageCallback {
    private RecyclerView recyclerView;
    private EditText edtEscribirMensaje;
    private ImageButton btnEnviar,buttonBack, emojiButton;
    private ChatAdapter chatAdapter;
    private ChatService chatService;
    private LinearLayout messageLayout;
    public FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Conexion con el Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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
        initialMessages.add(new Message("Vichayito es un destino encantador en la costa norte de Perú. Ofrecemos un paquete de 3 días y 2 noches que incluye alojamiento en una carpa estándar en segunda fila, desayunos y almuerzos, traslados al aeropuerto y tours de medio día y día completo. El check-in es a las 3:00 pm y el check-out es a las 12:00 pm. Si deseas más información o realizar una reserva, puedes encontrarlo en nuestra app. ¡Disfruta de tu viaje a Vichayito! ", MessageRole.SYSTEM));
        initialMessages.add(new Message("Cusco es un destino increíble en los Andes peruanos. Ofrecemos un paquete de 5 días y 4 noches que incluye traslados a los hoteles y aeropuertos, traslado a la estación de tren, entradas a los atractivos, boleto de tren de ida y vuelta a Machu Picchu, visitas guiadas en español e inglés, almuerzos y alojamiento en hoteles seleccionados. También se incluyen desayunos diarios. ¡Esperamos que disfrutes de tu viaje a Cusco y Machu Picchu!", MessageRole.SYSTEM));
        initialMessages.add(new Message("Iquitos es un destino fascinante en la selva amazónica del norte de Perú. Ofrecemos un paquete de 3 días y 2 noches que incluye alojamiento en el Amazonas Sinchicuy Lodge, refrescos de bienvenida y comidas durante tu estadía, traslados desde el aeropuerto/hotel, excursiones ecoturísticas y avistamiento de animales, y encuentros culturales con comunidades locales. El programa incluye visitas a la comunidad Yagua, la Laguna Huashalado, excursiones por el río Yanayacu y al proyecto de fauna silvestre en el Fundo Neiser. Si deseas obtener más información puedes buscar en nuestra app. ¡Disfruta tu aventura en la selva amazónica de Iquitos!", MessageRole.SYSTEM));
        initialMessages.add(new Message("Tarapoto es un destino maravilloso en la selva peruana. Ofrecemos un paquete de 4 días y 3 noches que incluye alojamiento en Sumaj Casa Hotel Tarapoto, desayunos, almuerzos, traslados al aeropuerto y tours de medio día y día completo. El día 1 incluye canotaje en el río Mayo, el día 2 un tour a la Laguna Azul, el día 3 una visita al Lodge Pumarinri y las cascadas de Pucayaquillo, y el día 4 una exploración de las cataratas de Ahuashiyacu. Si deseas más información, puedes encontrarlo nuestra app. ¡Disfruta de tu viaje a Tarapoto!", MessageRole.SYSTEM));
        initialMessages.add(new Message("Al finalizar te puedes despedir y desearle un buen viaje", MessageRole.SYSTEM));
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
        // Dentro de onCreate o donde obtienes el usuario actual
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userPhotoUrl = currentUser.getPhotoUrl().toString();

        btnEnviar.setOnClickListener(v -> {
            String messageText = edtEscribirMensaje.getText().toString().trim();
            if (!messageText.isEmpty()) {
                Message userMessage = new Message(messageText, MessageRole.USER);
                userMessage.setUserPhotoUrl(userPhotoUrl);
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
            chatAdapter.removeLoadingMessage();
            chatAdapter.addMessage(errorMessage);
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        });
    }
}
