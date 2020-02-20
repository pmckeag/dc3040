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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;
import com.example.dc3040.model.Places;
import com.example.dc3040.ui.DateFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class NewPlaceFragment extends Fragment {

    private PlaceViewModel placeViewModel;
    private EditText editPlaceName;
    private Date date;
    private Button dateButton;
    private EditText editPlaceLocation;
    private EditText editPlaceNotes;
    private int holidayAssoc = 1;
    //TODO add spinner for holiday association
    //TODO add photo support

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_newplace, container, false);
        editPlaceName = root.findViewById(R.id.edit_placename);
        editPlaceLocation = root.findViewById(R.id.edit_location);
        editPlaceNotes = root.findViewById(R.id.edit_place_notes);

        dateButton = root.findViewById(R.id.place_date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDatePicker(root);
            }
        });

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        Button saveButton = root.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Places newPlace;
                if (TextUtils.isEmpty(editPlaceName.getText()) || date == null || holidayAssoc == 0) {
                    Snackbar.make(view, "Please enter a name, date, and holiday", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                } else {
                    newPlace = new Places(holidayAssoc,
                            editPlaceName.getText().toString(),
                            date,
                            editPlaceLocation.getText().toString(),
                            editPlaceNotes.getText().toString(),
                            "");
                    placeViewModel.insert(newPlace);
                    NavDirections action = NewPlaceFragmentDirections.actionNewPlaceToPlacesList();
                    Navigation.findNavController(root).navigate(action);
                }
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
            String buttonText = "Date: " + dayOfMonth + "/" + month + "/" + year;
            dateButton.setText(buttonText);
        }
    };
}