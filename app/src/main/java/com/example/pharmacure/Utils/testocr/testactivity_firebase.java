package com.example.pharmacure.Utils.testocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
import java.io.IOException;
import java.lang.ref.WeakReference;

public class testactivity_firebase extends AppCompatActivity {

    private static TextView mImageDetails;
    private static ImageView mMainImage;
    private static FirebaseFunctions mFunctions;

    String result="";
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final String FILE_NAME = "temp.jpg";

    private static final int MAX_DIMENSION = 1200;
    public Uri fileuri;

   static testactivity_firebase test=new testactivity_firebase();

    private static final String TAG = testactivity_firebase.class.getSimpleName();




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testocr);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(testactivity_firebase.this);
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, (dialog, which) -> startGalleryChooser())
                        .setNegativeButton(R.string.dialog_select_camera, (dialog, which) -> startCamera());
                builder.create().show();
            }
        });


        mImageDetails = findViewById(R.id.image_details);
        mMainImage = findViewById(R.id.main_image);


    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    public  void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Scale down bitmap size
                bitmap = scaleBitmapDown(bitmap, 640);
                // Convert bitmap to base64 encoded string
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);


                mFunctions = FirebaseFunctions.getInstance();


                mFunctions.useEmulator("127.0.0.1", 4500);

                // Create json request to cloud vision
                JsonObject request = new JsonObject();
// Add image to request
                JsonObject image = new JsonObject();
                image.add("content", new JsonPrimitive(base64encoded));
                request.add("image", image);
//Add features to the request
                JsonObject feature = new JsonObject();
                feature.add("type", new JsonPrimitive("TEXT_DETECTION"));
// Alternatively, for DOCUMENT_TEXT_DETECTION:
//feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
                JsonArray features = new JsonArray();
                features.add(feature);
                request.add("features", features);


                annotateImage(request.toString())
                        .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
                            @Override
                            public void onComplete(@NonNull Task<JsonElement> task) {
                                if (!task.isSuccessful()) {
                                    // Task failed with an exception
                                    task.getException().printStackTrace();
                                    System.out.println("failes");
                                    // ...
                                } else {
                                    // Task completed successfully
                                    // ...
                                    JsonObject annotation = task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
                                    System.out.format("%nComplete annotation:%n");
                                    System.out.format("%s%n", annotation.get("text").getAsString());
                                    result=annotation.get("text").getAsString();
                                    mImageDetails.setText(annotation.get("text").getAsString());

                                }
                            }
                        });


                //String path= Utility.getRealPathFromURI(getApplicationContext(),uri);
                //System.out.println(path);
                Log.d("message","calling cloud vision");
                //callCloudVision(uri);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
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

    private void callCloudVision( Uri uri) {
        // Switch text to loading
        Log.d(TAG,"calling cloud vision");
        fileuri=uri;
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {

        } catch (Exception e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }



    public static class detect extends AsyncTask<Object,Void,String>{

        private final WeakReference<testactivity_firebase> mActivityWeakReference;
        Uri uriaddress;

        public detect(WeakReference<testactivity_firebase> mActivityWeakReference, Uri uriaddress) {
            this.mActivityWeakReference = mActivityWeakReference;
            this.uriaddress = uriaddress;
        }

        @Override
        protected String doInBackground(Object... objects) {
            test.uploadImage(uriaddress);


            return test.result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

