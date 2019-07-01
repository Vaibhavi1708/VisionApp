package com.example.vision.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "diary")

public class DiaryDBModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "notes_body")
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String message) {
        this.body = message;
    }

    public DiaryDBModel(String body) {
        this.body = body;
    }
}
