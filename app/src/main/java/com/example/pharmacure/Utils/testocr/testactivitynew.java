package com.example.pharmacure.Utils.testocr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.R;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.ImageContext;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class testactivitynew extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 100;
    private ImageView selectedImageView;
    TextView restext;
    String finaltext="";
    private static final int GALLERY_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testactivitynew);

        selectedImageView = findViewById(R.id.selected_image_view);
        restext=findViewById(R.id.resulttext_ocr);

        // Start intent to pick an image from the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri);

            // Perform text detection
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                detectHandwrittenText(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void detectHandwrittenText(InputStream inputStream) throws IOException {

        InputStream is=getApplicationContext().getAssets().open("application_default_credentials.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(is);
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
            Image image = Image.newBuilder().setContent(com.google.protobuf.ByteString.readFrom(inputStream)).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION))
                    .setImage(image)
                    .setImageContext(ImageContext.newBuilder().addLanguageHints("en"))
                    .build();

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(ImmutableList.of(request));
            for (AnnotateImageResponse res : response.getResponsesList()) {
                if (res.hasError()) {
                    Toast.makeText(this, "Error: " + res.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    // Extract detected text and display or process it as needed
                    String detectedText = annotation.getDescription();

                    finaltext=finaltext.concat(detectedText);
                    restext.setText(finaltext);

                    Toast.makeText(this, "Detected text: " + detectedText, Toast.LENGTH_LONG).show();
                    Log.d("detected text:",detectedText);

                }
            }
        }
    }
}