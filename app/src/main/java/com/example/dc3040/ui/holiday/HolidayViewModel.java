package com.example.dc3040.ui.holiday;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dc3040.repository.HolidayRepository;
import com.example.dc3040.model.Holiday;

import java.util.List;

public class HolidayViewModel extends AndroidViewModel {

    private HolidayRepository repository;
    private LiveData<List<Holiday>> allHolidays;

    public HolidayViewModel(Application application) {
        super(application);
        repository = new HolidayRepository(application);
        allHolidays = repository.getAllHolidays();
    }

    public LiveData<List<Holiday>> getAllHolidays() {
        return this.allHolidays;
    }

    public void insert(Holiday holiday) {
        repository.insertHoliday(holiday);
    }

    public void delete(int holidayId) { repository.deleteHoliday(holidayId); }

    public Holiday getOneHoliday(int holidayId) {  return repository.getOneHoliday(holidayId); }

    public void update(Holiday holiday) { repository.update(holiday); }
}
