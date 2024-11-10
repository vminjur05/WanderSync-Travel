package com.example.sprintproject.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHelper {
    private static FirebaseDatabaseHelper instance;
    private final DatabaseReference databaseReference;

    private FirebaseDatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    public DatabaseReference getDestinationsReference() {
        return databaseReference.child("destinations");
    }

    public DatabaseReference getTravelLogReference() {
        return databaseReference.child("travelLog");
    }

    public DatabaseReference getAccommodationsReference() {
        return databaseReference.child("accommodations");
    }
}
