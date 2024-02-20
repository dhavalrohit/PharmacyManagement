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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.R;
import com.example.pharmacure.captcha.MathCaptcha;
import com.example.pharmacure.captcha.captchamodel;

import com.example.pharmacure.captcha.captchamodel;
import com.factory.Firebase_factory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Loginactivity_new extends AppCompatActivity {

    public String uid;
    String captchaans="";
    EditText enterdcaptcha;
    Button changecaptchantn;

    EditText emailtext,passwordtext;

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
        setContentView(R.layout.loginnew);

        emailtext=findViewById(R.id.email);
        passwordtext=findViewById(R.id.password);
        Button loginbtn=findViewById(R.id.loginButton);
        Button signupbutton=findViewById(R.id.goToRegisterButton_loginnew);
        ImageView img1=findViewById(R.id.imageView1captcha);
        enterdcaptcha=findViewById(R.id.captchatext);
        changecaptchantn=findViewById(R.id.buttoncaptchachange);

        captchamodel ch=new MathCaptcha(300,100, com.rogerlemmonapps.captcha.MathCaptcha.MathOptions.PLUS_MINUS);
        //Captcha c = new TextCaptcha(300, 100, 5, TextOptions.NUMBERS_AND_LETTERS);
        img1.setImageBitmap(ch.image);
        img1.setLayoutParams(new LinearLayout.LayoutParams(ch.width *2, ch.height *2));
        captchaans=ch.answer;

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()){

                    String enteredcaptchatext=enterdcaptcha.getText().toString();


                    if (checkcaptcha(enteredcaptchatext)){

                        firebaselogin();

                    }else {
                        Toast.makeText(getBaseContext(), "Invalid Captcha",
                                Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(getBaseContext(), "Internet Connection Not Avaliable",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii=new Intent(Loginactivity_new.this, Sign_up_Activity.class);
                startActivity(ii);
            }
        });

        changecaptchantn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captchamodel ch=new MathCaptcha(300,100, com.rogerlemmonapps.captcha.MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                //Captcha c = new TextCaptcha(300, 100, 5, TextOptions.NUMBERS_AND_LETTERS);
                img1.setImageBitmap(ch.image);
                img1.setLayoutParams(new LinearLayout.LayoutParams(ch.width *2, ch.height *2));
                captchaans=ch.answer;
                Toast.makeText(getApplicationContext(),"Captcha Changed",Toast.LENGTH_SHORT).show();

            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadPreferences();
    }


    public void starthomepageactivity(){


        Intent i=new Intent(Loginactivity_new.this, MainActivity.class);
        startActivity(i);
    }

    public void firebaselogin(){
        // ...
        // Initialize Firebase Auth


        String email=emailtext.getText().toString();
        String password=passwordtext.getText().toString();

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

                            emailtext.setText("");
                            passwordtext.setText("");

                            starthomepageactivity();



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("My App", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        savePreferences();


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean checkcaptcha(String text){
        if (text.equalsIgnoreCase(captchaans)){
            return true;
        }else {
            return false;
        }
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String email=emailtext.getText().toString();
        String password=passwordtext.getText().toString();

        UnameValue = email;
        PasswordValue = password;
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        emailtext.setText(UnameValue);
        passwordtext.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }



}