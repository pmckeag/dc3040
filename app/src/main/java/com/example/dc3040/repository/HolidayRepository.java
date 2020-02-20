package com.example.dc3040.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
;

import com.example.dc3040.dao.HolidayDao;
import com.example.dc3040.database.HolidayRoomDatabase;
import com.example.dc3040.model.Holiday;

import java.util.List;

public class HolidayRepository {

    private HolidayDao holidayDao;

    private LiveData<List<Holiday>> allHolidays;
    private Holiday oneHoliday;

    public HolidayRepository(Application application) {
        HolidayRoomDatabase db = HolidayRoomDatabase.getDatabase(application);
        holidayDao = db.holidayDao();
        allHolidays = holidayDao.selectAllHolidays();
    }

    public void insertHoliday(Holiday holiday) {
        new insertAsyncTask(holidayDao).execute(holiday);
    }

    public void deleteHoliday(int holidayId) {
    }

    public Holiday getOneHoliday(int holidayId) {
        oneHoliday = holidayDao.getOneHoliday(holidayId);
        return oneHoliday;
    }

    public LiveData<List<Holiday>> getAllHolidays() {
        return allHolidays;
    }

    public void update(Holiday holiday) {
        new updateAsyncTask(holidayDao).execute(holiday);
    }

    private static class insertAsyncTask extends AsyncTask<Holiday, Void, Void> {
        private HolidayDao asyncHolidayDao;

        insertAsyncTask(HolidayDao holidayDao) {
            asyncHolidayDao = holidayDao;
        }

        @Override
        protected Void doInBackground(final Holiday... params) {
            asyncHolidayDao.insertHoliday(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Holiday, Void, Void> {
        private HolidayDao asyncHolidayDao;

        updateAsyncTask(HolidayDao holidayDao) {
            asyncHolidayDao = holidayDao;
        }

        protected Void doInBackground(final Holiday... params) {
            asyncHolidayDao.update(params[0]);
            return null;
        }
    }
}
