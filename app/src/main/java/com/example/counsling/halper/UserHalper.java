package com.example.counsling.halper;

import java.io.Serializable;

public class UserHalper{
    public String name,email,phonenumber,password,userid;

    public UserHalper() {
    }

    public UserHalper(String name, String email, String phonenumber, String password,String userid) {

        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.userid=userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
