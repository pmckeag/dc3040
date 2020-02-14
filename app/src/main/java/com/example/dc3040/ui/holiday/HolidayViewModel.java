package com.example.dc3040.ui.holiday;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HolidayViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public HolidayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is holiday fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
