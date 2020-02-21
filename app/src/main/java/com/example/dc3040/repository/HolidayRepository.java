package com.example.dc3040.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.dc3040.dao.HolidayDao;
import com.example.dc3040.database.HolidayRoomDatabase;
import com.example.dc3040.model.Holiday;

import java.util.List;

public class HolidayRepository {

    public static int holidayIncrement = 3;

    private HolidayDao holidayDao;

    private LiveData<List<Holiday>> allHolidays;

    public HolidayRepository(Application application) {
        HolidayRoomDatabase db = HolidayRoomDatabase.getDatabase(application);
        holidayDao = db.holidayDao();
        allHolidays = holidayDao.selectAllHolidays();
    }

    public void insertHoliday(Holiday holiday) {
        holiday.setHolidayId(holidayIncrement);
        holidayIncrement++;
        new insertAsyncTask(holidayDao).execute(holiday);
    }

    public void deleteHoliday(int holidayId) {
        new deleteAsyncTask(holidayDao).execute(holidayId);
    }


    public LiveData<Holiday> getOneHoliday(int holidayId) {
        LiveData<Holiday> result = holidayDao.getOneHoliday(holidayId);
        return result;
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

    private static class selectAsyncTask extends AsyncTask<Integer, Void, LiveData<Holiday>> {
        private HolidayDao asyncHolidayDao;

        public interface AsyncResponse {
            void processFinish(LiveData<Holiday> output);
        }

        public AsyncResponse delegate = null;

        selectAsyncTask(HolidayDao holidayDao, AsyncResponse delegate) {
            asyncHolidayDao = holidayDao;
            this.delegate = delegate;
        }

        @Override
        protected LiveData<Holiday> doInBackground(final Integer... params) {
            asyncHolidayDao.getOneHoliday(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(LiveData<Holiday> result) {
            delegate.processFinish(result);
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Object, Void, Void> {
        private HolidayDao asyncHolidayDao;

        deleteAsyncTask(HolidayDao holidayDao) {
            asyncHolidayDao = holidayDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            asyncHolidayDao.deleteHoliday((Integer) params[0]);
            return null;
        }

    }

}

