package com.example.pharmacure.Utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.annotation.NonNull;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class cloudvision_test {
   public static FirebaseFunctions mFunctions;
    public static void main(String[] args) {
        InputStream in;
        cloudvision_test cv=new cloudvision_test();


         mFunctions=FirebaseFunctions.getInstance();
        try {
            File file=new File("D:\test images\testimage3.jpg");
             in=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        bitmap=cv.scaleBitmapDown(bitmap,1024);

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

        cv.annotateImage(request.toString())
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
