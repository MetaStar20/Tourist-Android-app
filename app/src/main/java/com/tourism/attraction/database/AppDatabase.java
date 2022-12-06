package com.tourism.attraction.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tourism.attraction.database.dao.AttractionDao;
import com.tourism.attraction.database.model.Attraction;

@Database(entities = {Attraction.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract AttractionDao attractionDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "tourism.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}