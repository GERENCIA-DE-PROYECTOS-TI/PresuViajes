package com.gadalos.planificacion_turismo_ia;


import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatService {
    private final List<Message> messageList;
    private final OkHttpClient httpClient;
    private final MessageCallback callback;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final String OPEN_IA_API_KEY = "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";
    private final String OPEN_IA_URL = "https://api.openai.com/v1/chat/completions";

    public ChatService(List<Message> initialMessages, MessageCallback callback) {
        this.messageList = new ArrayList<>(initialMessages);
        this.httpClient = new OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        this.callback = callback;
    }

    public void sendMessage(Message message) {
        messageList.add(message);
        this.callApi();
    }

    private void callApi() {

        try {
            JSONObject apiRequestBody = new JSONObject();

            apiRequestBody.put("model", "gpt-3.5-turbo");
            apiRequestBody.put("max_tokens", 300);

            JSONArray messagesRequest = new JSONArray();

            messageList.forEach(message -> {
                JSONObject messageRequest = new JSONObject();
                try {
                    messageRequest.put("content", message.getMessage());
                    messageRequest.put("role", message.getRole());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                messagesRequest.put(messageRequest);
            });

            apiRequestBody.put("messages", messagesRequest);

            RequestBody body = RequestBody.create(apiRequestBody.toString(), JSON);


            Request request = new Request.Builder()
                    .url(OPEN_IA_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + OPEN_IA_API_KEY)
                    .build();

            httpClient.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onMessageError(e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String rawResponse = response.body().string();
                        try {
                            JSONObject apiObjectResponse = new JSONObject(rawResponse);
                            JSONArray responseChoices = apiObjectResponse.getJSONArray("choices");
                            String message = responseChoices.getJSONObject(0).getJSONObject("message").getString("content");

                            callback.onMessageReceived(new Message(message, MessageRole.SYSTEM));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callback.onMessageError(response.message());
                    }
                }
            });
        } catch (Exception e) {
            callback.onMessageError(e.getMessage());
        }
    }
}