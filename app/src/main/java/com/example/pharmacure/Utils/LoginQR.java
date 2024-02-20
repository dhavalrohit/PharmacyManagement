package com.example.pharmacure.Utils;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.pharmacure.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import io.grpc.netty.shaded.io.netty.internal.tcnative.AsyncTask;

public class LoginQR extends AppCompatActivity {
    Socket clientSocket;
    Button qrbtn;
    TextView servertxt;
    Socket ss= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityloginscan);
        qrbtn=findViewById(R.id.qrloginbtn);
        servertxt=findViewById(R.id.servertext);

        Handler handler=new Handler();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader br=new BufferedReader(new InputStreamReader(ss.getInputStream()));
                    String serverid=br.readLine();
                    servertxt.setText("Data From Server:"+serverid);
                    ss.close();
                    finish();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        qrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip="192.168.1.9";
                int port=9999;

                try {
                    ss = new Socket(ip,port);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                OutputStreamWriter os= null;
                try {
                    os = new OutputStreamWriter(ss.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String str="firebaseID q77qq1qqq22g1q";

                try {
                    PrintWriter outpr=new PrintWriter(os);
                    outpr.write(str);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    os.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                handler.postDelayed(runnable,500);

            }
        });


    }
}