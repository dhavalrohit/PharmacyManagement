package com.example.pharmacure.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pharmacure.HomePageFragment;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_info);

        Button regbtn=findViewById(R.id.btn_signup);

        DatabaseReference db=Firebase_factory.getfbDatabase_instance().getReference().child("Users");
        db.keepSynced(true);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdatabase();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finish();
                starthomepageactivity();

            }
        });




    }

    public void createdatabase(){

        EditText ownernametxt=findViewById(R.id.input_name);
        EditText storenametxt=findViewById(R.id.input_store_name);
        EditText tinnumbertxt=findViewById(R.id.input_tin);
        EditText gstnumbertxt=findViewById(R.id.input_gst);
        EditText storeaddtxt=findViewById(R.id.input_addr);
        EditText mobbotxt=findViewById(R.id.input_ph1_mobile);
        EditText whatsapptxt=findViewById(R.id.input_ph2_whatsapp);
        EditText moreinfotxt=findViewById(R.id.input_more);





        String uid = Firebase_factory.getFirebaseAuth_Instance().getCurrentUser().getUid();
        DatabaseReference du= Firebase_factory.getfbDatabase_instance().getReference().child("Users");
        du.keepSynced(true);
        du.child(uid).child("info").child("name").setValue(ownernametxt.getText().toString());
        du.child(uid).child("info").child("storename").setValue(storenametxt.getText().toString());
        du.child(uid).child("info").child("tin").setValue(tinnumbertxt.getText().toString());
        du.child(uid).child("info").child("addr").setValue(storeaddtxt.getText().toString());
        du.child(uid).child("info").child("ph1").setValue(mobbotxt.getText().toString());
        du.child(uid).child("info").child("ph2").setValue(whatsapptxt.getText().toString());
        du.child(uid).child("info").child("more").setValue(moreinfotxt.getText().toString());
        du.child(uid).child("info").child("gst").setValue(gstnumbertxt.getText().toString());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        du.child(uid).child("Log").child("log").setValue(
                format.format(calendar.getTime()) + "\n[" + fm.format(calendar.getTime()) + "] Account Created");

        Toast.makeText(getBaseContext(), "Database Created",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), "Login to continue",
                Toast.LENGTH_SHORT).show();




    }

    public void starthomepageactivity(){


        Intent i=new Intent(RegistrationActivity.this, HomePageFragment.class);
        startActivity(i);
    }

}
