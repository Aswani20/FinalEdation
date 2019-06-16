package com.example.collegeassistant.models;

import com.google.firebase.firestore.ServerTimestamp;


import java.util.Date;

public class Announcement {
    private User userSender;
    private String announcement;
    private String targetDepartment;
    private String targetYear;
    private Date dateCreated;

    public Announcement(){

    }

    public Announcement(User user, String announcement, String targetYear, String targetDepartment){
        this.userSender = user;
        this.targetYear = targetYear;
        this.targetDepartment = targetDepartment;
        this.announcement = announcement;
    }

    //--------------getters------------------------------
    public User getUserSender(){ return userSender; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }
    public String getAnnouncement(){return announcement;}
    public String getTargetDepartment(){return targetDepartment;}
    public String getTargetYear(){return targetYear;}


    //--------------setters------------------------------
    public void setUserSender(User user){
        userSender = user;
    }
    public void setDateCreated(Date date){
        dateCreated = date;
    }
    public void setAnnouncement(String announcement){
        this.announcement = announcement;
    }
    public void setTargetDepartment(String targetDepartment){
        this.targetDepartment = targetDepartment;
    }
    public void setTargetYear(String targetYear){
        this.targetYear = targetYear;
    }
}
