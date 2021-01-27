package com.example.counsling.halper;

public class DoctorHalper {
    public String doctorname,specialist,image,doctoremail,password,doctorid;

    public DoctorHalper() {
    }

    public DoctorHalper(String doctorname, String specialist, String image, String doctoremail, String password, String doctorid) {
        this.doctoremail=doctoremail;
        this.password=password;
        this.doctorname = doctorname;
        this.specialist = specialist;
        this.image=image;
        this.doctorid=doctorid;
    }
    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDoctoremail() {
        return doctoremail;
    }

    public void setDoctoremail(String doctoremail) {
        this.doctoremail = doctoremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDoctorid() { return doctorid; }

    public void setDoctorid(String doctorid) { this.doctorid = doctorid; }
}