package com.example.collegeassistant.chat;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.example.collegeassistant.models.Message;
import com.example.collegeassistant.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageHolder> {


    //FOR DATA
    private final RequestManager glide;
    private String uid;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     * @param glide
     */
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide, String uid) {
        super(options);
        this.glide = glide;
        this.uid=uid;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
       holder.bind(model, uid);
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_messages, parent, false);
        return new MessageHolder(view, glide);
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }


    @Override
    public void onError(FirebaseFirestoreException e) {
        // Called when there is an error getting a query snapshot. You may want to update
        // your UI to display an error message to the user.
        // ...
        Log.e("error", e.getMessage());
    }


}
