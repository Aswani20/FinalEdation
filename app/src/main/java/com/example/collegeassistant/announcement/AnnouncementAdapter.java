package com.example.collegeassistant.announcement;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.collegeassistant.R;
import com.example.collegeassistant.chat.MessageHolder;
import com.example.collegeassistant.models.Announcement;
import com.example.collegeassistant.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AnnouncementAdapter extends FirestoreRecyclerAdapter<Announcement, AnnouncementHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnnouncementAdapter(@NonNull FirestoreRecyclerOptions<Announcement> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementHolder holder, int position, @NonNull Announcement model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public AnnouncementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_announcement, viewGroup, false);
        return new AnnouncementHolder(view);
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
