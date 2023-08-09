package com.example.foodorderapp;

public class User {
    public String fullName,fullMail,fullAddress;
    int userType;
    public User()
    {
        this.fullAddress="";
        this.fullName="";
        this.fullMail="";
        this.userType=0;
    }
    public User(String fullName,String fullMail,String fullAddress,int userType)
    {
        this.fullName = fullName;
        this.fullMail = fullMail;
        this.fullAddress = fullAddress;
        this.userType = userType;
    }
    public int getUserType()
    {
        return userType;
    }

    public String getFullName() {
        return fullName;
    }
}
