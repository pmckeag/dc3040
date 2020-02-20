package com.example.dc3040.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dc3040.model.Holiday;

import java.util.List;

@androidx.room.Dao
public interface HolidayDao {

    @Insert
    void insertHoliday(Holiday holiday);

    @Query("DELETE FROM holidays WHERE holidayId = :holidayId")
    void deleteHoliday(int holidayId);

    @Query("DELETE FROM holidays")
    void deleteAllHolidays();

    @Query("SELECT * FROM holidays WHERE holidayId = :holidayId")
    Holiday getOneHoliday(int holidayId);

    @Query("SELECT * FROM holidays")
    LiveData<List<Holiday>> selectAllHolidays();

    @Update
    void update(Holiday holiday);
}
