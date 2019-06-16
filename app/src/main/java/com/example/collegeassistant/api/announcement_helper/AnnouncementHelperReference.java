package com.example.collegeassistant.api.announcement_helper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnnouncementHelperReference {

    private static final String COLLECTION_NAME = "Announcement";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getAnnouncementCollection(){
        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME);
    }
}
