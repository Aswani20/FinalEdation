package com.example.collegeassistant.chat;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.collegeassistant.models.Message;
import com.example.collegeassistant.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageHolder extends RecyclerView.ViewHolder {

    //Received items view binder
    @BindView(R.id.profile_image) ImageView profile;
    @BindView(R.id.user_name) TextView sender;
    @BindView(R.id.message_text) TextView message;
    @BindView(R.id.message_image) ImageView messageImage;
    @BindView(R.id.message_time) TextView time;

    //layout root
    @BindView(R.id.display_set) RelativeLayout display_set;
    @BindView(R.id.profile_container) LinearLayout profileContainer;
    @BindView(R.id.message_body) LinearLayout messageBody;



    //image manager view binder
    private final RequestManager glide;

    //user customization
    //FOR DATA
    private final int colorCurrentUser;
    private final int colorRemoteUser;
    private final int colorProfessor;

    private boolean isCurrentUser ,isProfessor;


    public MessageHolder(@NonNull View itemView, RequestManager glide) {
        super(itemView);
        this.glide = glide;
        ButterKnife.bind(this, itemView);
        colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark);
        colorRemoteUser = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
        colorProfessor = ContextCompat.getColor(itemView.getContext(), R.color.gradStart);
    }

    //Received items
    public void bind(Message message, String uid){
        if(message.getUserSender()!=null){
            isCurrentUser = message.getUserSender().getUid().equals(uid);
            isProfessor = message.getUserSender().getIsProfessor();
        }
        //sets the message
        this.message.setText(message.getMessage());
        this.message.setTextAlignment(isCurrentUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

        //setting user
        if(message.getUserSender()!=null) {
            this.sender.setText(message.getUserSender().getUsername());

            //set image to view
            if(message.getUserSender().getUrlPicture()!=null){//profile
                glide.load(message.getUserSender().getUrlPicture()).into(profile);
            }
        }
        if(isCurrentUser){
            profile.setVisibility(View.GONE);
        }else{
            profile.setVisibility(View.VISIBLE);

        }


        if(message.getUrlImage()!=null){//message content
            messageImage.setVisibility(View.VISIBLE);
            glide.load(message.getUrlImage()).into(messageImage);
        }else{
            messageImage.setVisibility(View.GONE);
        }

        //set time
        if(message.getDateCreated() != null) {
            Calendar cal = Calendar. getInstance();
            Date date=cal. getTime();
            if(date.compareTo(message.getDateCreated())>0){
                time.setText(this.convertDateToYears(message.getDateCreated()));
            }else{
                time.setText(this.convertDateToHour(message.getDateCreated()));
            }
        }

        if(message.getUserSender()!=null) {
            updateUI();
        }
    }

    private void updateUI(){

        //Update Message Bubble Color Background
        if(isProfessor){
            ((GradientDrawable) messageBody.getBackground()).setColor(colorProfessor);
        }else {
            ((GradientDrawable) messageBody.getBackground()).setColor(isCurrentUser ? colorCurrentUser : colorRemoteUser);
        }
        // PROFILE CONTAINER
        RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutHeader.addRule(isCurrentUser ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        this.profileContainer.setLayoutParams(paramsLayoutHeader);

        // MESSAGE CONTAINER
        RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutContent.addRule(isCurrentUser ? RelativeLayout.BELOW : RelativeLayout.BELOW, R.id.profile_container);
        paramsLayoutContent.addRule(isCurrentUser ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        if(isCurrentUser){
            paramsLayoutContent.setMargins(0,4,32,0);
        }else{
            paramsLayoutContent.setMargins(32,4,0,0);
        }
        this.messageBody.setLayoutParams(paramsLayoutContent);

        //TimeStamp View
        RelativeLayout.LayoutParams paramsLayoutTimeStamp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutTimeStamp.addRule(isCurrentUser ? RelativeLayout.BELOW : RelativeLayout.BELOW, R.id.message_body);
        paramsLayoutTimeStamp.addRule(isCurrentUser ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.message_body);
        this.time.setLayoutParams(paramsLayoutTimeStamp);

        this.display_set.requestLayout();
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
