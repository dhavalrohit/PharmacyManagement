package com.example.pharmacure.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                firebase_signup();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent=new Intent(Sign_up_Activity.this, Login_activity.class);
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



            startActivity(new Intent(Sign_up_Activity.this, MainActivity.class));
            finish();
        }else {
            setContentView(R.layout.activity_sign_up);
        }

    }
}
