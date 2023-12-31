package com.gadalos.planificacion_turismo_ia;

public class Message {
    private String message;
    private MessageRole role;
    private String userPhotoUrl;

    public Message() {
    }

    public Message(String message, MessageRole role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role.getApiValue();
    }

    // Agrega un método para verificar si es un mensaje de carga sin texto
    public boolean isLoadingMessage() {
        return role == MessageRole.SYSTEM_LOADING;
    }
    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

}