package com.example.vision.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "contacts")

public class ContactsDBModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fname;
    private String lname;
    private String mobile;
    private String work;
    private String home;
    private String main;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public ContactsDBModel() {
    }

    public ContactsDBModel(String fname, String lname, String mobile, String work, String home, String main) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.work = work;
        this.home = home;
        this.main = main;
    }
}

