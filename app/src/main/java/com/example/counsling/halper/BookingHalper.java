package com.example.counsling.halper;

import java.io.Serializable;

public class BookingHalper implements Serializable {
    String username,usernumber,problem,Follow_on_Instagram,userid,callyou,token;

    public BookingHalper() {
    }

    public BookingHalper(String username, String usernumber, String problem, String follow_on_Instagram, String userid, String callyou,String token) {
        this.username = username;
        this.usernumber = usernumber;
        this.problem = problem;
        this.Follow_on_Instagram = follow_on_Instagram;
        this.userid=userid;
        this.callyou=callyou;
        this.token=token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getFollow_on_Instagram() {
        return Follow_on_Instagram;
    }

    public void setFollow_on_Instagram(String follow_on_Instagram) {
        Follow_on_Instagram = follow_on_Instagram;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCallyou() {
        return callyou;
    }

    public void setCallyou(String callyou) {
        this.callyou = callyou;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
