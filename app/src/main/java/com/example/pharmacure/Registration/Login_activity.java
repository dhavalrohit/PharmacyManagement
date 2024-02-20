package com.example.pharmacure.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;
import com.example.pharmacure.captcha.captchamodel;
import com.example.pharmacure.captcha.captchamodel;
import com.factory.Firebase_factory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Login_activity extends AppCompatActivity {

    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button loginbtn=findViewById(R.id.loginButton);




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()){
                    firebaselogin();

                }else {
                    Toast.makeText(getBaseContext(), "Internet Connection Not Avaliable",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void starthomepageactivity(){


        Intent i=new Intent(Login_activity.this, MainActivity.class);
        startActivity(i);
    }

    public void firebaselogin(){
        // ...
        // Initialize Firebase Auth


        EditText emailtxt=findViewById(R.id.email);
        EditText passwordtxt=findViewById(R.id.password);
        String email=emailtxt.getText().toString();
        String password=passwordtxt.getText().toString();

        Firebase_factory.getFirebaseAuth_Instance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("My App", "signInWithEmail:success");
                            FirebaseUser user = Firebase_factory.getFirebaseAuth_Instance().getCurrentUser();

                            uid=user.getUid();
                            Toast.makeText(getBaseContext(), uid+"",
                                    Toast.LENGTH_SHORT).show();

                            emailtxt.setText("");
                            passwordtxt.setText("");

                            starthomepageactivity();



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("My App", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}