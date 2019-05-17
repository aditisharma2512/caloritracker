package com.example.calorietracker;

import java.util.Date;

public class Credential {
    private String username;
    private String passwordhash;
    private Date signupdate;
    private Users userid;

    public Credential() {
    }

    public Credential(String username, String passwordhash, Date signupdate, Users userid)
    {
        this.username = username;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
        this.userid = userid;
    }

    public Credential(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }



}
