package com.example.collegeassistant.announcement;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.collegeassistant.R;
import com.example.collegeassistant.models.Announcement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.sender_name) TextView senderName;
    @BindView(R.id.status_indicator) ImageView statusIndicator;
    @BindView(R.id.announcement) TextView announcement;
    @BindView(R.id.created_date) TextView time;

    private final int colorUrgent;
    private final int colorDateChanged;
    private final int colorAnnouncement;

    public AnnouncementHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        colorUrgent = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark);
        colorDateChanged = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
        colorAnnouncement = ContextCompat.getColor(itemView.getContext(), R.color.gradStart);
    }

    public void bind(Announcement announcement){
        if(announcement.getUserSender()!=null){//sets user name
            String sender = "Professor: " +announcement.getUserSender().getUsername();
            senderName.setText(sender);
        }

        this.announcement.setText(announcement.getAnnouncement());



        //set time
        if(announcement.getDateCreated() != null) {
            Calendar cal = Calendar. getInstance();
            Date date=cal. getTime();
            if(date.compareTo(announcement.getDateCreated())>0){
                String dipText = "Sent:"+this.convertDateToYears(announcement.getDateCreated());
                time.setText(dipText);
            }else{
                String dipText = "Sent:"+this.convertDateToHour(announcement.getDateCreated());
                time.setText(dipText);
            }
        }

    }

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }

    private String convertDateToYears(Date date){
        DateFormat dfTime = new SimpleDateFormat("dd/MM/yy");
        return dfTime.format(date);
    }
}
