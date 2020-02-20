package com.example.dc3040.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
;

import com.example.dc3040.dao.PlacesDao;
import com.example.dc3040.database.HolidayRoomDatabase;
import com.example.dc3040.model.Places;

import java.util.List;

public class PlacesRepository {

    private PlacesDao placesDao;

    private LiveData<List<Places>> allPlaces;

    public PlacesRepository(Application application) {
        HolidayRoomDatabase db = HolidayRoomDatabase.getDatabase(application);
        placesDao = db.placesDao();
        allPlaces = placesDao.selectAllPlaces();
    }

    public void insertPlace(Places places) {
        new insertAsyncTask(placesDao).execute(places);
    }

    public void deletePlace(int placesId) {

    }

    public LiveData<List<Places>> selectAllPlaces() {
        return allPlaces;
    }


    private static class insertAsyncTask extends AsyncTask<Places, Void, Void> {
        private PlacesDao asyncPlacesDao;

        insertAsyncTask(PlacesDao placesDao) {
            asyncPlacesDao = placesDao;
        }

        @Override
        protected Void doInBackground(final Places... params) {
            asyncPlacesDao.insertPlace(params[0]);
            return null;
        }
    }
}
