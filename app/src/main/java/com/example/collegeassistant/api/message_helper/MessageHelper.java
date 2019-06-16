package com.example.collegeassistant.api.message_helper;

import com.example.collegeassistant.models.Message;
import com.example.collegeassistant.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;



public class MessageHelper {//-----organizes queries-------



    // --- GET ---
    public static Query getAllMessageForChat(String year, String department){
        return ChatHelper.getChatCollection()
                .document(year)
                .collection(department)
                .orderBy("dateCreated")
                .limit(50);
    }

   //---Save to cloud---
    public static Task<DocumentReference> createMessageForChat(User userSender,String textMessage, String year, String department){

        // 1 - Create the Message object
        Message message = new Message(textMessage, userSender);

        // 2 - Store Message to Firestore
        return ChatHelper.getChatCollection()
                .document(year)
                .collection(department)
                .add(message);
    }

}

