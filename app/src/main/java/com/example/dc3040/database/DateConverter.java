package com.example.dc3040.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp / 1000);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : (date.getTime() * 1000);
    }
}
