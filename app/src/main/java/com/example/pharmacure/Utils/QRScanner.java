package com.example.pharmacure.Utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.pharmacure.R;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.WebSocket;

public class QRScanner extends AppCompatActivity {
    Button scanBtn;
    TextView messageText, messageFormat;
    Socket ss= null;
    Handler handler;
    Runnable runnable;

    WebSocketClient_example webSocketClientExample = new WebSocketClient_example();

    private WebSocketClient_example mWebSocketClientExample;


    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private static final String WS_URL = "ws://192.168.1.8:9999";

    private WebSocketClient webSocketClient;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner_main);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);

        handler=new Handler();

        runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader br=new BufferedReader(new InputStreamReader(ss.getInputStream()));
                    String serverid=br.readLine();
                    //servertxt.setText("Data From Server:"+serverid);
                    ss.close();
                    finish();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        // adding listener to the button
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need to create the object
                // of IntentIntegrator class
                // which is the class of QR library
                IntentIntegrator intentIntegrator = new IntentIntegrator(QRScanner.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
            }
        });
    }


    public void onClick(View v) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {

                if (validateString(intentResult.getContents())){

                    Runnable r1=new Runnable() {
                        @Override
                        public void run()
                        {
                            connectWebSocket();
                        }
                    };
                    Thread t1=new Thread(r1);
                    t1.start();
                }
                else {
                    Toast.makeText(getApplicationContext(),"InValid QrCode",Toast.LENGTH_SHORT).show();
                    System.out.println("Invalid Qr Code");
                }




            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        webSocketClientExample.setMessageListener(new WebSocketClient_example.MessageListener() {
            @Override
            public void onMessageReceived(String message) {
                Log.d("MainActivity", "Received message from server: " + message);
                System.out.println("MainActivity"+"Received message from server: " + message);
            }
        });
    }

    private void connectWebSocket() {
        URI uri;
        try {
            //192.168.0.115
           //uri = new URI("ws://192.168.0.115:9999");
            uri = new URI("wss://nodejsapp-testing.up.railway.app");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
      webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {

                SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                        Context.MODE_PRIVATE);

                // Get value

                UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
                PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String fbUseriD=currentFirebaseUser.getUid();

                Log.i("WebSocket", "Opened");
                webSocketClient.send("Hello From Android");
                webSocketClient.send("UID:"+fbUseriD);
                webSocketClient.send("Username:"+UnameValue);
                webSocketClient.send("Password:"+PasswordValue);
            }
            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Handle the received message from the server
                        Log.i("WebSocket", "Received message: " + message);
                    }
                });
            }
            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("WebSocket", "Closed " + s);
            }
            @Override
            public void onError(Exception e) {
                Log.i("WebSocket", "Error " + e.getMessage());
            }
        };
      webSocketClient.connect();
    }

    public static boolean validateString(String input) {

        if (input.length() != 16) {
            return false;
        }

        int uppercaseCount = 0;
        int lowercaseCount = 0;
        int digitCount = 0;


        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                uppercaseCount++;
            } else if (Character.isLowerCase(c)) {
                lowercaseCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {

                return false;
            }
        }


        if (uppercaseCount != 4 || lowercaseCount != 4 || digitCount != 8) {
            return false;
        }


        return true;
    }


}