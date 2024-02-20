package com.example.pharmacure.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Sign_up_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_sign_up);

        checkuser_logged();

        Button login_btn=(Button) findViewById(R.id.goToLoginButton);
        Button signupbtn=findViewById(R.id.registerButton);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()){
                    firebase_signup();
                }else {
                    Toast.makeText(getBaseContext(), "Internet Connection Not Avaliable",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent=new Intent(Sign_up_Activity.this, Loginactivity_new.class);
                startActivity(loginintent);
            }
        });

    }
    /*    Intent loginintent=new Intent(MainActivity.this,Login_activity.class);
                startActivity(loginintent);
            */


    public void movetoregisterinfo(){
        Intent loginintent=new Intent(Sign_up_Activity.this, RegistrationActivity.class);
        startActivity(loginintent);
    }



    public void firebase_signup(){

        EditText emailtxt=findViewById(R.id.email);
        String email=emailtxt.getText().toString();

        EditText passwordtxt=findViewById(R.id.password);
        String password=passwordtxt.getText().toString();



        Firebase_factory.getFirebaseAuth_Instance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("My App", "createUserWithEmail:success");
                            FirebaseUser user = Firebase_factory.getFirebaseAuth_Instance().getCurrentUser();

                            Toast.makeText(getBaseContext(), "Authentication Success.New User Created",
                                    Toast.LENGTH_SHORT).show();


                            movetoregisterinfo();

                            finish();




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("My App", "createUserWithEmail:failure", task.getException());

                            Toast.makeText(getBaseContext(), "Authentication failed: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();



                        }
                    }
                });
    }
    public void checkuser_logged(){
        FirebaseAuth mauth;
        mauth=FirebaseAuth.getInstance();
        if (mauth.getCurrentUser()!=null){
            Toast.makeText(getBaseContext(), "User Logged In Automatically",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Sign_up_Activity.this, MainActivity.class));
            finish();
        }else {
            setContentView(R.layout.activity_sign_up);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    /*@Override//added on 20/12/23
    protected void onRestart() {
        super.onRestart();
        Intent ii=new Intent(Sign_up_Activity.this,Loginactivity_new.class);
        startActivity(ii);
    }*/





    protected  void saveExitTime() {
        long currentTime = System.currentTimeMillis();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("ExitTimeSignupActivity", currentTime);
        editor.apply();
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        if (checkTimeDifference()){

        }else {
            Toast.makeText(getBaseContext(), "Session Timeout,Login To Continue",
                    Toast.LENGTH_SHORT).show();

            Intent ii=new Intent(Sign_up_Activity.this, Login_activity.class);
            startActivity(ii);

        }
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        Intent ii=new Intent(Sign_up_Activity.this,Loginactivity_new.class);
        startActivity(ii);


    }*/



    protected  boolean checkTimeDifference() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        long exitTime = sharedPreferences.getLong("ExitTimeSignupActivity", 0);
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - exitTime;

        return exitTime != 0 && timeDifference <= 2000;
    }


}