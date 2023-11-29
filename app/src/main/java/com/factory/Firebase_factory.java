package com.factory;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase_factory {



    public static FirebaseDatabase getfbDatabase_instance(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        return database;
    }

    public static DatabaseReference getdatabaseRef(){
        DatabaseReference databaseref=FirebaseDatabase.getInstance().getReference();
        databaseref.keepSynced(true);
        return databaseref;
    }

    public static FirebaseAuth getFirebaseAuth_Instance(){
        FirebaseAuth mauth=FirebaseAuth.getInstance();
        return mauth;
    }

    public static String getfbUserId(){
        FirebaseAuth mauth=FirebaseAuth.getInstance();
        String uid=mauth.getCurrentUser().getUid();
        return uid;
    }

}
