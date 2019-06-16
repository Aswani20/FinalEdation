package com.example.collegeassistant.api.announcement_helper;

import com.example.collegeassistant.models.Announcement;
import com.example.collegeassistant.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class AnnouncementHelper {


    // --- GET ---
    public static Query getAllAnnouncement(String year, String department){
        return AnnouncementHelperReference.getAnnouncementCollection()
                .document(year)
                .collection(department)
                .orderBy("dateCreated")
                .limit(50);
    }

    //---Save to cloud---
    public static Task<DocumentReference> createAnnouncement(User userSender, String textMessage, String year, String department){

        // 1 - Create the Message object
        Announcement message = new Announcement(userSender, textMessage, year, department);

        // 2 - Store Message to Firestore
        return AnnouncementHelperReference.getAnnouncementCollection()
                .document(year)
                .collection(department)
                .add(message);
    }
}
