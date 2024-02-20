package com.example.pharmacure.Utils.qrlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pharmacure.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.zxing.Result;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qractivitymain extends AppCompatActivity  implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private EditText usernameET ;
    private Button signInButton;

    private LinearLayout chatLayout;
    private EditText messageET;

    private LinearLayout qrScanneLayout;

    private ListView chatList ;

    public  FirebaseFunctions mFunctions;
    InputStream in;

    private MessageBroadcastReceiver mReceiver = new MessageBroadcastReceiver();
    private ChatListAdapter adapter = new ChatListAdapter(this);

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        usernameET = (EditText) findViewById(R.id.usernameET);
        signInButton= (Button) findViewById(R.id.signinButton);

        chatLayout= (LinearLayout) findViewById(R.id.chatLayout);
        messageET= (EditText) findViewById(R.id.messageET);

        qrScanneLayout = (LinearLayout) findViewById(R.id.qrScannerLayout);
        chatList = (ListView) findViewById(R.id.chatLListView);
        chatList.setAdapter(adapter);

        mFunctions=FirebaseFunctions.getInstance();

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.testimage3);
        bitmap=scaleBitmapDown(bitmap,1024);

        // Convert bitmap to base64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        // Create json request to cloud vision
        JsonObject request = new JsonObject();
// Add image to request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);
//Add features to the request
        JsonObject feature = new JsonObject();
        feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
// Alternatively, for DOCUMENT_TEXT_DETECTION:
//feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
        JsonArray features = new JsonArray();
        features.add(feature);
        request.add("features", features);

        JsonObject imageContext = new JsonObject();
        JsonArray languageHints = new JsonArray();
        languageHints.add("en");
        imageContext.add("languageHints", languageHints);
        request.add("imageContext", imageContext);

        annotateImage(request.toString())
                .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
                    @Override
                    public void onComplete(@NonNull Task<JsonElement> task) {
                        if (!task.isSuccessful()) {
                            // Task failed with an exception
                            // ...
                            System.out.println("failed");
                        } else {
                            // Task completed successfully

                            // ...
                            JsonObject annotation = task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
                            System.out.format("%nComplete annotation:%n");
                            System.out.format("%s%n", annotation.get("text").getAsString());
                        }
                    }
                });






    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter("RECEIVEMESSAGE"));
    }

    /**
     * Used only for locking in a username for the user.
     *
     * @param view
     */
    public void signIn(View view) {
        if (usernameET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Must have username defined", Toast.LENGTH_SHORT).show();
            return ;
        }

        // If a username is defined, then lock interface and update
        usernameET.setEnabled(false);
        signInButton.setEnabled(false);
        chatLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Used for sending messages to the chat room server
     *
     * @param v
     */
    public void sendMessage(View v) {
        if (!messageET.getText().toString().isEmpty()) {
            try {
                ServerFacade.sendMessage(
                        this,
                        usernameET.getText().toString(),
                        messageET.getText().toString()
                );
                messageET.setText("");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(this, "Problem sending message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * @param view
     */
    public void performQrScanner(View view) {

        qrScanneLayout.setVisibility(View.VISIBLE);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />

        qrScanneLayout.addView(
                mScannerView
        );

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();   // Stop camera on pause
        }
        unregisterReceiver(mReceiver);
    }

    /**
     * Process the result of the QRCode read.
     *
     * @param rawResult
     */
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.

        mScannerView.stopCamera();

        try {
            ServerFacade.sendRegisterUserToQRCode(
                    this,
                    usernameET.getText().toString(),
                    rawResult.getText()
            );

            qrScanneLayout.removeView(
                    mScannerView
            );
            qrScanneLayout.setVisibility(View.GONE);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("Main Activity", e.getMessage(), e);
            qrScanneLayout.removeView(
                    mScannerView
            );
            qrScanneLayout.setVisibility(View.GONE);
        }

    }

    class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String rawData = intent.getStringExtra("rawdata");
            String user = rawData.split(":")[0].trim();
            String message = rawData.split(":")[1].trim();
            adapter.addMessage(
                    user, message,
                    (user.trim().equals(usernameET.getText().toString()))
            );
            Log.d("RAWDATA", "Received RawData : " + rawData);
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                    @Override
                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }
}