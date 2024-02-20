package com.example.pharmacure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.Customer.CustomerFragement;
import com.example.pharmacure.Doctor.Doctor_Activity;
import com.example.pharmacure.Inventory.ItemsFragement;
import com.example.pharmacure.Registration.Loginactivity_new;
import com.example.pharmacure.Registration.Updateinfo_activity;
import com.example.pharmacure.Summary.Summary_Fragement;
import com.example.pharmacure.Transactions.newTransaction.TransactionActivity;
import com.example.pharmacure.Transactions.transHistory.TransactionFragement;
import com.example.pharmacure.Utils.LoginQR;
import com.example.pharmacure.Utils.QRScanner;
import com.example.pharmacure.Utils.testocr.test_activity;
import com.example.pharmacure.Utils.testocr.testactivity_firebase;
import com.example.pharmacure.Utils.testocr.testactivitynew;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_Bill;
import com.factory.Firebase_factory_inventory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static FloatingActionButton floatingbtn;
    private DatabaseReference db;
   FragmentManager transfragmentManager;
   private String uid;

   public static int flag = 0;

   View Headerview;
   public static String billid;
   public static String productid;

    TextView navigationhead,navigationsub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        floatingbtn = (FloatingActionButton) findViewById(R.id.fab_main);
        floatingbtn.hide();
        //billid= Firebase_factory_Bill.getBillID();
        billid=Firebase_factory_Bill.getBillID();
        productid= Firebase_factory_inventory.getproductid();
        productid=Firebase_factory_inventory.getproductid();

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref= database.getReference("message");
        myref.setValue("connected");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Headerview =navigationView.getHeaderView(0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomePageFragment()).commit();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        FirebaseAuth mauth= Firebase_factory.getFirebaseAuth_Instance();
        uid=Firebase_factory.getfbUserId();


        db= Firebase_factory.getdatabaseRef().child("Users").child(uid);
        db.keepSynced(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
    db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String storename=snapshot.child("storename").getValue(String.class);
                    String ownername=snapshot.child("name").getValue(String.class);

                    navigationhead=(TextView) Headerview.findViewById(R.id.navhead);
                    navigationsub=(TextView) Headerview.findViewById(R.id.navsub);
                    navigationhead.setText(storename);
                    navigationsub.setText(ownername);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    /*@Override
    protected void onStop() {
        super.onStop();
        saveExitTime();

    }*/

   /* @Override
   protected void onRestart() {
        super.onRestart();
        if (checkTimeDifference()){

        }else {
            Toast.makeText(getBaseContext(), "Session Timeout,Login To Continue",
                    Toast.LENGTH_SHORT).show();

            Intent ii=new Intent(MainActivity.this, Login_activity.class);
            startActivity(ii);

        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (flag == 0) {
            // super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomePageFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.order_ename).setVisible(false);
        menu.findItem(R.id.order_qty).setVisible(false);
        menu.findItem(R.id.order_exp).setVisible(false);
        menu.findItem(R.id.order_due).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_updateinfo) {
            Intent i = new Intent(this, Updateinfo_activity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_logout) {
            finish();
            signout();
            startActivity(new Intent(MainActivity.this, Loginactivity_new.class));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_inventory) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ItemsFragement()).addToBackStack(null).commit();
        } else if (id == R.id.nav_home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomePageFragment()).commit();
        } /*else if (id == R.id.nav_transaction) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new TransactionFragement()).commit();
        } */else if (id == R.id.nav_customers) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CustomerFragement()).addToBackStack(null).commit();
        }else if (id == R.id.nav_summary) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Summary_Fragement()).addToBackStack(null).commit();
        }
        /* else if (id == R.id.nav_dev) {
            Intent i = new Intent(this, Developer.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(this, About.class);
            startActivity(i);}*/
        else if (id==R.id.nav_doctors) {
            /*FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,new doctorFragement()).addToBackStack(null).commit();*/
            Intent ii=new Intent(MainActivity.this, Doctor_Activity.class);
            startActivity(ii);

        } else if (id==R.id.nav_loginqr) {
            Intent ii=new Intent(MainActivity.this, testactivitynew.class);
            startActivity(ii);
        } else if (id==R.id.qrlogin) {
            Intent ii=new Intent(MainActivity.this, LoginQR.class);
            startActivity(ii);



        } else if (id==R.id.scanqr) {
            Intent ii=new Intent(MainActivity.this, QRScanner.class);
            startActivity(ii);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public  void homebtn(View view) {
        int id = view.getId();
        if (R.id.btntrans_homeicon == id) {
           Intent i = new Intent(this, TransactionActivity.class);
            startActivity(i);

            /*Intent ii=new Intent(this, captchaactivity.class);
            startActivity(ii);*/

        } else if (R.id.btncust_homeicon == id) {
            FragmentManager customerfragmentManager = getSupportFragmentManager();
            customerfragmentManager.beginTransaction().replace(R.id.content_frame, new CustomerFragement()).addToBackStack(null).commit();

        } else if (R.id.btntranshistory_homeicon == id) {
             transfragmentManager = getSupportFragmentManager();

            transfragmentManager.beginTransaction().replace(R.id.content_frame, new TransactionFragement(),"transfragment").addToBackStack(null).commit();


        } else if (R.id.btninventory_homeicon == id) {
            FragmentManager itemsfragmentManager = getSupportFragmentManager();
            itemsfragmentManager.beginTransaction().replace(R.id.content_frame, new ItemsFragement()).addToBackStack(null).commit();

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        saveExitTime();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checkTimeDifference()){

        }else {
            Toast.makeText(getApplicationContext(),"Session Timeout Login to Continue",Toast.LENGTH_SHORT).show();
            Intent ii=new Intent(MainActivity.this, Loginactivity_new.class);
            startActivity(ii);
        }
    }

    public void updatei(View view) {
        Intent i = new Intent(this, Updateinfo_activity.class);
        startActivity(i);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



    public void signout(){
        FirebaseAuth mauth;
        mauth=Firebase_factory.getFirebaseAuth_Instance();
        mauth.signOut();

    }

    protected  void saveExitTime() {
        long currentTime = System.currentTimeMillis();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("ExitTime", currentTime);
        editor.apply();
    }

    protected  boolean checkTimeDifference() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        long exitTime = sharedPreferences.getLong("ExitTime", 0);
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - exitTime;

        return exitTime != 0 && timeDifference <= 300000;
    }
}