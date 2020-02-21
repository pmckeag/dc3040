package com.example.dc3040.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
;

import com.example.dc3040.dao.PlacesDao;
import com.example.dc3040.database.HolidayRoomDatabase;
import com.example.dc3040.model.Places;

import java.util.List;

public class PlacesRepository {

    private PlacesDao placesDao;
    public static int placeIncrement = 4;

    private LiveData<List<Places>> allPlaces;


    public PlacesRepository(Application application) {
        HolidayRoomDatabase db = HolidayRoomDatabase.getDatabase(application);
        placesDao = db.placesDao();
        allPlaces = placesDao.selectAllPlaces();
    }

    public void insertPlace(Places places) {
        places.setPlaceId(placeIncrement);
        placeIncrement++;
        new insertAsyncTask(placesDao).execute(places);
    }

    public void deletePlace(Places place) {
        placesDao.delete(place);
    }

    public void update(Places place) {
        placesDao.update(place);
    }

    public LiveData<List<Places>> selectAllPlaces() {
        return allPlaces;
    }

    public LiveData<List<Places>> selectPlacesFromHoliday(int holidayId) {
        LiveData<List<Places>> result = placesDao.selectPlacesFromHoliday(holidayId);
        return result;
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
