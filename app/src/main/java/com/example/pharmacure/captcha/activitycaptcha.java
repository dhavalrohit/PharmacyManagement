package com.example.pharmacure.captcha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pharmacure.R;

public class activitycaptcha extends AppCompatActivity {


    ImageView im;
    Button btn;
    TextView ans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captchamain);
        im = (ImageView)findViewById(R.id.imageView1);
        btn = (Button)findViewById(R.id.button1);
        ans = (TextView)findViewById(R.id.textView1);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                captchamodel c = new com.example.pharmacure.captcha.MathCaptcha(300, 100, com.rogerlemmonapps.captcha.MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                //Captcha c = new TextCaptcha(300, 100, 5, TextOptions.NUMBERS_AND_LETTERS);
                im.setImageBitmap(c.image);
                //im.setLayoutParams(new LinearLayout.LayoutParams(c.width *2, c.height *2));
                ans.setText(c.answer);
            }
        });
    }
}