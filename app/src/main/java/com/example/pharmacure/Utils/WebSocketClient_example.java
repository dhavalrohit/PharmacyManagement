package com.example.pharmacure.Utils;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient_example extends WebSocketListener {

    private static final String WS_SERVER_URL = "ws://localhost:9999"; // Replace with your server IP

    private WebSocket webSocket;

    // Add an interface to listen for message events
    public interface MessageListener {
        void onMessageReceived(String message);
    }

    private MessageListener messageListener;

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }


    public void connectWebSocket() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(WS_SERVER_URL)
                            .build();

                    webSocket = client.newWebSocket(request, WebSocketClient_example.this);

                    // Note: Don't forget to add this line to ensure the WebSocket connects before returning
                    client.dispatcher().executorService().shutdown();
                } catch (Exception e) {
                    Log.e("WebSocketClient", "Error connecting to WebSocket", e);
                }}
        }).start();





    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        // Connection opened, you can send messages here if needed
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        // Received a message from the server, handle it as needed

        // Received a message from the server, handle it as needed
        Log.d("WebSocket", "Received message: " + text);
        System.out.println("WebSocket"+"Received message: " + text);

        // Notify the listener about the received message
        if (messageListener != null) {
            messageListener.onMessageReceived(text);
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        // Connection closed
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        // Handle connection failure
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing from client");
        }
    }




}
