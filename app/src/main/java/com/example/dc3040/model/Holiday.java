package com.example.dc3040.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.dc3040.database.DateConverter;

import java.util.Date;

@Entity(tableName = "holidays")
@TypeConverters(DateConverter.class)
public class Holiday {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "holidayId")
    private int holidayId;

    @NonNull
    @ColumnInfo (name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "start_date")
    private Date startDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private Date endDate;

    @ColumnInfo(name = "notes")
    private String notes;

    public Holiday(@NonNull String name, Date startDate, Date endDate, String notes) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public int getHolidayId() {
        return this.holidayId;
    }

    public String getName() {
        return this.name;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public String toString(){
        return this.name;
    }
}
