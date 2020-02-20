package com.example.dc3040.ui.place;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;
import com.example.dc3040.model.Places;
import com.example.dc3040.ui.DateFragment;
import com.example.dc3040.ui.holiday.HolidayViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewPlaceFragment extends Fragment {

    private PlaceViewModel placeViewModel;
    private HolidayViewModel holidayViewModel;
    private EditText editPlaceName;
    private Date date;
    private Button dateButton;
    private EditText editPlaceNotes;
    private int holidayAssoc;
    private List<Holiday> holidayList;
    //TODO add photo support

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_newplace, container, false);

        final Spinner spinner = root.findViewById(R.id.holiday_spinner);
        final ArrayAdapter<Holiday> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        holidayViewModel = new ViewModelProvider(this).get(HolidayViewModel.class);
        holidayViewModel.getAllHolidays().observe(this.getViewLifecycleOwner(), new Observer<List<Holiday>>() {
            @Override
            public void onChanged(List<Holiday> holidays) {
                holidayList = holidays;
                for (Holiday holiday : holidayList) {
                    arrayAdapter.add(holiday);
                }
                spinner.setAdapter(arrayAdapter);
            }
        });

        editPlaceName = root.findViewById(R.id.edit_placename);
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
                            editPlaceNotes.getText().toString(),
                            "");
                    placeViewModel.insert(newPlace);
                    NavDirections action = NewPlaceFragmentDirections.actionNewPlaceToPlacesList();
                    Navigation.findNavController(root).navigate(action);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Holiday selectedHoliday = (Holiday) parent.getSelectedItem();
                holidayAssoc = selectedHoliday.getHolidayId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                holidayAssoc = 1;
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
}