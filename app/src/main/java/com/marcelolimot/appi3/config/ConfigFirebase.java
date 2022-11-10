package com.marcelolimot.appi3.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfigFirebase {
    private static FirebaseFirestore database;
    private static FirebaseAuth mAuth;

    public static FirebaseFirestore getFirebaseFirestore(){
        if(database== null){
            database= FirebaseFirestore.getInstance();
        }
        return database;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(mAuth == null){
            mAuth =FirebaseAuth.getInstance();
        }
        return mAuth;
    }
}
