package com.example.pharmacure.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.R;
import com.factory.Firebase_factory_doctor;
import com.google.android.material.textfield.TextInputLayout;

public class Doctor_Activity extends AppCompatActivity {

    TextInputLayout docname,docmobileno,docregno,doclocality,docspec,docid;
    EditText edocname,edocmobileno,edoclocality,edocid,edocspec,edocregno;
    Button clear,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        docname= (TextInputLayout) findViewById(R.id.doctornameLayout);
        docregno=(TextInputLayout) findViewById(R.id.docregnoLayout);
        docmobileno=(TextInputLayout) findViewById(R.id.docmobilenoLayout);
        doclocality=(TextInputLayout) findViewById(R.id.doclocalityLayout);
        docspec=(TextInputLayout) findViewById(R.id.docspecLayout);
        docid=(TextInputLayout) findViewById(R.id.docIDLayout);

        edocname=(EditText) findViewById(R.id.doctornameText);
        edoclocality=(EditText) findViewById(R.id.doclocalityText);
        edocmobileno=(EditText) findViewById(R.id.docmobilenoText);
        edocspec=(EditText) findViewById(R.id.docspeText);
        edocregno=(EditText) findViewById(R.id.docregnoText);
        edocid=(EditText)findViewById(R.id.docidText);

        clear=(Button) findViewById(R.id.adddoc_btn_clear);
        add=(Button) findViewById(R.id.adddoc_btn_add);

        String docid= Firebase_factory_doctor.getdoctorid();

        edocid.setText(docid);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddoc();
                finish();
            }
        });






    }





    public boolean validate() {
        int flag = 0;
        if (edocname.getText().toString().trim().isEmpty()) {
            flag = 1;
            docname.setError("Cannot be Empty");
        } else
            docname.setErrorEnabled(false);

        if (edocregno.getText().toString().trim().isEmpty()) {
            flag = 1;
            docregno.setError("Cannot be Empty");
        } else
            docregno.setErrorEnabled(false);

        if (edocmobileno.getText().toString().trim().isEmpty()) {
            flag = 1;
            docmobileno.setError("Cannot be Empty");
        } else
            docmobileno.setErrorEnabled(false);

        if (edoclocality.getText().toString().trim().isEmpty()) {
            flag = 1;
            doclocality.setError("Cannot be Empty");
        } else
            doclocality.setErrorEnabled(false);

        if (edocspec.getText().toString().trim().isEmpty()) {
            flag = 1;
            docspec.setError("Cannot be Empty");
        } else
            docspec.setErrorEnabled(false);

        return flag != 1;
    }

    public void adddoc(){
        String docname=edocname.getText().toString();
        String docspec=edocspec.getText().toString();
        String docregno=edocregno.getText().toString();
        String docloc=edoclocality.getText().toString();
        String docmob=edocmobileno.getText().toString();
        String did=edocid.getText().toString();

        if (validate()){
            Firebase_factory_doctor.addDoctor(docname,did,docloc,docmob,docregno,docspec);
            Toast.makeText(this, docname+" added", Toast.LENGTH_SHORT).show();
        }


    }

}