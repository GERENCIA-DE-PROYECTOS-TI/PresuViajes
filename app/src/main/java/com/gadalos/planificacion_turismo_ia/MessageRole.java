package com.gadalos.planificacion_turismo_ia;

public enum MessageRole {
    SYSTEM("system"), USER("user");

    private final String apiValue;

    MessageRole(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }
}