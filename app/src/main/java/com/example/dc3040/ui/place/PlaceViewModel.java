package com.example.dc3040.ui.place;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dc3040.model.Places;
import com.example.dc3040.repository.PlacesRepository;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {

    private PlacesRepository repository;
    private LiveData<List<Places>> allPlaces;

    public PlaceViewModel(Application application) {
        super(application);
        repository = new PlacesRepository(application);
        allPlaces = repository.selectAllPlaces();
    }

    public LiveData<List<Places>> selectAllPlaces() {
        return this.allPlaces;
    }

    public void insert(Places place) {
        repository.insertPlace(place);
    }

    public LiveData<List<Places>> selectPlacesFromHoliday(int holidayId) {
        return repository.selectPlacesFromHoliday(holidayId);
    }
}
