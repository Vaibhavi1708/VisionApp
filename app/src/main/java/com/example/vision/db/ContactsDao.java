package com.example.vision.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.vision.models.ContactsDBModel;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM contacts")
    List<ContactsDBModel> getAll();

    @Query("SELECT * FROM contacts where mobile LIKE :mobile OR work LIKE :mobile OR main LIKE :mobile")
    ContactsDBModel findByName(String mobile);

// @Query("SELECT * FROM contacts where mobile like :mobile AND work like :phone AND home like :phone")
// ContactsDBModel findByName(String mobile);


    @Query("SELECT COUNT(*) from contacts")
    int countContacts();

    @Insert
    void insertAll(ContactsDBModel... contacts);

    @Delete
    void delete(ContactsDBModel contact);
}

