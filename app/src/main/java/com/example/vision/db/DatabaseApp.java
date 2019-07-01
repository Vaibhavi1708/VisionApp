package com.example.vision.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.vision.models.ContactsDBModel;
import com.example.vision.models.DiaryDBModel;


@Database(entities = {DiaryDBModel.class, ContactsDBModel.class}, version = 1, exportSchema = false)
public abstract class DatabaseApp extends RoomDatabase {

    private static DatabaseApp INSTANCE;

    public abstract DiaryDao diaryDao();
    public abstract ContactsDao contactsDao();

    public static DatabaseApp getDiaryDatabaseApp(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DatabaseApp.class, "diary-database")
// allow queries on the main thread.
// Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

