package com.example.dc3040.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "places")
public class Places {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "placeId")
    private int placeId;

    @NonNull
    @ColumnInfo(name = "holidayId")
    private int holidayId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "photo")
    private String photo;

    public Places(int holidayId, String name, Date date, String notes, String photo) {
        this.holidayId = holidayId;
        this.name = name;
        this.date = date;
        this.notes = notes;
        this.photo = photo;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getPlaceId() {
        return this.placeId;
    }

    public int getHolidayId() {
        return this.holidayId;
    }

    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }


    public String getNotes() {
        return this.notes;
    }

    public String getPhoto() {
        return photo;
    }

}

