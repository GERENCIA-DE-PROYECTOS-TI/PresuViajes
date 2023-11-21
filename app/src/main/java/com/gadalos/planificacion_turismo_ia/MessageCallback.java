package com.gadalos.planificacion_turismo_ia;

public interface MessageCallback {
    void onMessageReceived(Message message);

    void onMessageError(String error);
}