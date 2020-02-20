package com.example.dc3040.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "companions")
public class Companions {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "companionId")
    private int companionId;

    @NonNull
    @ColumnInfo(name = "holidayId")
    private int holidayId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    public Companions(int holidayId, String name) {
        this.holidayId = holidayId;
        this.name = name;
    }

    public void setCompanionId(int companionId) {
        this.companionId = companionId;
    }

    public int getCompanionId() {
        return this.companionId;
    }

    public int getHolidayId() {
        return this.holidayId;
    }

    public String getName() {
        return this.name;
    }
}

