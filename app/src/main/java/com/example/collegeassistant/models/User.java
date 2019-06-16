package com.example.collegeassistant.models;


import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String username;
    private String urlPicture;
    private boolean isProfessor;

    //---Student Specific
    private String eduYear;//----corresponds to grade the year the student is assigned to.
    private String department;//---- the department the student assigned to.


    public User() { }



    //----Student Constructor
    public User(String uid, String username, String urlPicture, String year,String department, boolean isProfessor) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.eduYear = year;
        this.department = department;
        this.isProfessor = isProfessor;
    }

    //----Professor Constructor
    public User(String uid, String username, String urlPicture, boolean isProfessor) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.isProfessor = isProfessor;
    }



    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getDepartment() { return department; }
    public String getEduYear() { return eduYear; }
    public boolean getIsProfessor(){return isProfessor;}

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setDepartment(String department) { this.department = department; }
    public void setEduYear(String eduYear) { this.eduYear = eduYear; }
    public void setIsProfessor(boolean isProfessor){ this.isProfessor = isProfessor;}

}
