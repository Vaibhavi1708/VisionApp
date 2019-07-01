package com.example.vision.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.vision.models.DiaryDBModel;

import java.util.List;


@Dao
public interface DiaryDao {
    @Query("SELECT * FROM diary")
    List<DiaryDBModel> getAll();

    @Query("SELECT * FROM diary where notes_body LIKE :body")
    DiaryDBModel findByName(String body);

    @Query("SELECT COUNT(*) from diary")
    int countNotes();

    @Insert
    void insertAll(DiaryDBModel... notes);

    @Delete
    void delete(DiaryDBModel note);
}
