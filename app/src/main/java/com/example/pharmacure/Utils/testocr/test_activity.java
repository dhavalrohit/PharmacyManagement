package com.example.pharmacure.Utils.testocr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class test_activity extends AppCompatActivity {


    private TextView mImageDetails;
    private ImageView mMainImage;

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final String FILE_NAME = "temp.jpg";

    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = test_activity.class.getSimpleName();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testocr);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(test_activity.this);
            builder
                    .setMessage(R.string.dialog_select_prompt)
                    .setPositiveButton(R.string.dialog_select_gallery, (dialog, which) -> startGalleryChooser())
                    .setNegativeButton(R.string.dialog_select_camera, (dialog, which) -> startCamera());
            builder.create().show();
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

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);


               String path= Utility.getRealPathFromURI(getApplicationContext(),uri);
                System.out.println(path);
                callCloudVision(path);
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

    private void callCloudVision(final String filepath) {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> detectionTask = new Detect(this,detectDocumentText(filepath) );
            detectionTask.execute();
        } catch (Exception e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
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

private static class Detect extends AsyncTask<Object,Void,String>{

    private final WeakReference<test_activity> mActivityWeakReference;
        String filename;

    public Detect(test_activity activity, String filename) {
        this.mActivityWeakReference = new WeakReference<>(activity);
        this.filename = filename;
    }

    @Override
    protected String doInBackground(Object... objects) {

        try {
            Log.d(TAG, "created Cloud Vision request object, sending request");
            String text="detectDocumentText(filename)";
            text=filename;
            System.out.println(text);
            return text;

        } catch (Exception e) {
            Log.d("failed ","failed");
        }
        return "Cloud Vision API request failed. Check logs for details.";

    }

    @Override
    protected void onPostExecute(String s) {
        test_activity activity = mActivityWeakReference.get();
        if (activity != null && !activity.isFinishing()) {
            TextView imageDetail = activity.findViewById(R.id.image_details);
            imageDetail.setText(s);
        }

    }
}
    public static String detectDocumentText(String filePath) throws IOException {
        String result="";
        List<AnnotateImageRequest> requests = new ArrayList<>();


        com.google.protobuf.ByteString imgbytes= com.google.protobuf.ByteString.readFrom(new FileInputStream(filePath));

        com.google.cloud.vision.v1.Image img= com.google.cloud.vision.v1.Image.newBuilder().setContent(imgbytes).build();

        com.google.cloud.vision.v1.Feature feature= com.google.cloud.vision.v1.Feature.newBuilder().setType(com.google.cloud.vision.v1.Feature.Type.DOCUMENT_TEXT_DETECTION).build();


        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.println("annotation error");
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return res.getError().getMessage();
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page : annotation.getPagesList()) {
                    String pageText = "";
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word : para.getWordsList()) {
                                String wordText = "";
                                for (Symbol symbol : word.getSymbolsList()) {
                                    wordText = wordText + symbol.getText();
                                    System.out.format(
                                            "Symbol text: %s (confidence: %f)%n",
                                            symbol.getText(), symbol.getConfidence());
                                }
                                System.out.format(
                                        "Word text: %s (confidence: %f)%n%n", wordText, word.getConfidence());
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            System.out.println("%nParagraph: %n" + paraText);
                            System.out.format("Paragraph Confidence: %f%n", para.getConfidence());
                            blockText = blockText + paraText;
                        }
                        pageText = pageText + blockText;
                    }
                }
                System.out.println("%nComplete annotation:");
                System.out.println(annotation.getText());
                result=annotation.getText();
            }
        }
        return result;
    }




}


