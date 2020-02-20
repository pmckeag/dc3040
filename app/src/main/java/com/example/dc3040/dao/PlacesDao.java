package com.example.dc3040.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dc3040.model.Places;

import java.util.List;

@androidx.room.Dao
public interface PlacesDao {

    @Insert
    void insertPlace(Places place);

    @Query("DELETE FROM places WHERE holidayId = :holidayId ;")
    void deletePlaceFromHoliday(int holidayId);

    @Query("DELETE FROM places WHERE placeId = :placeId ;")
    void deletePlace(int placeId);

    @Query("DELETE FROM places")
    void deleteAllPlaces();

    @Query("SELECT * FROM places WHERE holidayId = :holidayId ;")
    LiveData<List<Places>> selectPlacesFromHoliday(int holidayId);

    @Query("SELECT * FROM places WHERE placeId = :placeId ;")
    LiveData<List<Places>> selectPlace(int placeId);

    @Query("SELECT * FROM places ;")
    LiveData<List<Places>> selectAllPlaces();
}
