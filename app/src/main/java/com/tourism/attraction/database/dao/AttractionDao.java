package com.tourism.attraction.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tourism.attraction.database.model.Attraction;

import java.util.List;

@Dao
public interface AttractionDao {
    @Query("SELECT * FROM attraction")
    List<Attraction> getAllAttractions();

    @Query("SELECT * FROM attraction WHERE id=:id")
    List<Attraction> getAttraction(int id);

    @Insert
    void insertAll(Attraction... attractions);

    @Delete
    void deleteAttraction(Attraction attraction);

    @Query("UPDATE attraction SET rating=:rating WHERE id=:id")
    void updateRating(String rating, int id);
}