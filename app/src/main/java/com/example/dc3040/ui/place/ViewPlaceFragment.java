package com.example.dc3040.ui.place;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.dc3040.MainActivity;
import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;
import com.example.dc3040.model.Places;
import com.example.dc3040.ui.DateFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;

public class ViewPlaceFragment extends Fragment {

    private int placeId;
    private PlaceViewModel placeViewModel;
    private EditText editPlaceName;
    private Button dateButton;
    private EditText editPlaceNotes;
    private LiveData<List<Places>> allPlaces;
    private Places inputPlace;
    private Date date;
    private String placeName;
    private String placeNotes;
    private String placePhoto;
    private ImageView photo;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        placeId = MainActivity.getPlaceId();
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_view_place, container, false);
        allPlaces = placeViewModel.selectAllPlaces();

        allPlaces.observe(this.getViewLifecycleOwner(), new Observer<List<Places>>() {
            @Override
            public void onChanged(List<Places> places) {
                for (Places place : places) {
                    if (place.getPlaceId() == placeId) {
                        inputPlace = place;
                    }
                }
                date = inputPlace.getDate();
                editPlaceName = root.findViewById(R.id.edit_placename);
                dateButton = root.findViewById(R.id.place_date_button);
                editPlaceNotes = root.findViewById(R.id.edit_place_notes);
                photo = root.findViewById(R.id.place_photo);


                editPlaceName.setText(inputPlace.getName());
                dateButton.setText("Date: " + date.getDay() + "/"
                        + date.getMonth() + "/"
                        + date.getYear());
                editPlaceNotes.setText(inputPlace.getNotes());
                placePhoto = inputPlace.getPhoto();

                displayPhoto(photo, placePhoto);

                dateButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        showDatePicker(root);
                    }
                });

                FloatingActionButton editFab = root.findViewById(R.id.button_edit_place);
                editFab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        placeName = editPlaceName.getText().toString();
                        placeNotes = editPlaceNotes.getText().toString();

                        if (placeName == null || placeName.equals("") || date == null) {
                            Snackbar.make(view, "Please enter a name and a date", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .show();
                        } else {
                            Places outputPlace = new Places(inputPlace.getHolidayId(),
                                    placeName,
                                    date,
                                    placeNotes,
                                    "");
                            outputPlace.setPlaceId(placeId);
                            placeViewModel.update(outputPlace);
                            NavDirections action = ViewPlaceFragmentDirections.actionViewPlaceToPlacesList();
                            Navigation.findNavController(root).navigate(action);
                        }
                    }
                });

                FloatingActionButton deleteFab = root.findViewById(R.id.button_delete_place);
                deleteFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeViewModel.delete(inputPlace);
                        NavDirections action = ViewPlaceFragmentDirections.actionViewPlaceToPlacesList();
                        Navigation.findNavController(root).navigate(action);
                    }
                });
            }


        });

        return root;
    }

    private void showDatePicker(View view) {
        DateFragment date = new DateFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallback(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = new Date(year, month, dayOfMonth);
            String buttonText = "Date: " + dayOfMonth + "/" + (month + 1)+ "/" + year;
            dateButton.setText(buttonText);
        }
    };

    public void displayPhoto(ImageView photo, String placePhoto) {
        Glide.with(this.getActivity())
                .load(placePhoto)
                .placeholder(R.drawable.ic_cloud_off_black_24dp)
                //.override(36, 36)
                .fitCenter()
                .into(photo);
    }

}
