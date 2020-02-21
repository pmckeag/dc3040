package com.example.dc3040.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.dc3040.dao.HolidayDao;
import com.example.dc3040.dao.PlacesDao;
import com.example.dc3040.model.Companions;
import com.example.dc3040.model.Holiday;
import com.example.dc3040.model.Places;
import com.example.dc3040.repository.HolidayRepository;
import com.example.dc3040.repository.PlacesRepository;

import java.util.Date;

@Database(entities = {Holiday.class, Places.class, Companions.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class HolidayRoomDatabase extends RoomDatabase {

    public abstract HolidayDao holidayDao();
    public abstract PlacesDao placesDao();

    private static HolidayRoomDatabase INSTANCE;

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static HolidayRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HolidayRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HolidayRoomDatabase.class, "holiday_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final HolidayDao holidayDao;
        private final PlacesDao placesDao;

        Holiday holiday1 = new Holiday("Rome",
                new Date(2019, 8, 31),
                new Date(2019, 9, 14),
                "Beach and city break");
        Holiday holiday2 = new Holiday("Stockholm",
                new Date(2020, 1, 2),
                new Date(2020, 1, 8),
                "City break in winter");

        Places place1 = new Places(1,
                "Colosseum",
                new Date(2019, 9, 10),
                "Visited the Colosseum",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Colosseum_in_Rome%2C_Italy_-_April_2007.jpg/800px-Colosseum_in_Rome%2C_Italy_-_April_2007.jpg");

        Places place2 = new Places(1,
                "St Peter's Basilica",
                new Date(2019, 9, 11),
                "",
                "https://upload.wikimedia.org/wikipedia/commons/f/f5/Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg");

        Places place3 = new Places(2,
                "Kungstradgarden",
                new Date(2020, 1, 3),
                "",
                "https://upload.wikimedia.org/wikipedia/commons/0/0a/Kungstradgarden_2008.jpg");

        PopulateDbAsync(HolidayRoomDatabase db) {
            holidayDao = db.holidayDao();
            placesDao = db.placesDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            holidayDao.deleteAllHolidays();
            placesDao.deleteAllPlaces();


            holiday1.setHolidayId(1);
            holiday2.setHolidayId(2);
            place1.setPlaceId(1);
            place2.setPlaceId(2);
            place3.setPlaceId(3);

            holidayDao.insertHoliday(holiday1);
            holidayDao.insertHoliday(holiday2);

            placesDao.insertPlace(place1);
            placesDao.insertPlace(place2);
            placesDao.insertPlace(place3);
            return null;
        }
    }
}
