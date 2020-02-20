package com.example.dc3040.ui.holiday;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dc3040.MainActivity;
import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;
import com.example.dc3040.model.Places;
import com.example.dc3040.ui.DateFragment;
import com.example.dc3040.ui.place.PlaceAdapter;
import com.example.dc3040.ui.place.PlaceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewHolidayFragment extends Fragment {

    private Holiday inputHoliday;
    private HolidayViewModel holidayViewModel;
    private int holidayId;
    private String holidayName;
    private Date startDate;
    private Date endDate;
    private String holidayNotes;
    private EditText editHolidayName;
    private Button dateButton;
    private EditText editHolidayNotes;
    private String dateBuilder;
    private LiveData<Holiday> observable;
    private PlaceViewModel placeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        holidayId = MainActivity.getHolidayId();
        holidayViewModel = new ViewModelProvider(this).get(HolidayViewModel.class);
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_view_holiday, container, false);
        observable = holidayViewModel.getOneHoliday(holidayId);

        observable.observe(this.getViewLifecycleOwner(), new Observer<Holiday>() {
            @Override
            public void onChanged(Holiday holiday) {
                if (holiday != null)
                    inputHoliday = holiday;

                    holidayName = inputHoliday.getName();
                    startDate = inputHoliday.getStartDate();
                    endDate = inputHoliday.getEndDate();
                    holidayNotes = inputHoliday.getNotes();

                    dateButton = root.findViewById(R.id.show_start_date);
                    dateButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            showDatePicker(root);
                        }
                    });

                    final EditText editHolidayName = root.findViewById(R.id.edit_name);
                    final EditText editHolidayNotes = root.findViewById(R.id.edit_notes);
                    editHolidayName.setText(holidayName);
                    editHolidayNotes.setText(holidayNotes);
                    dateBuilder = "Start date: " + startDate.getDay() + "/" + startDate.getMonth() + "/"
                            + startDate.getYear() + "\nEnd date: " + endDate.getDay() + "/"
                            + endDate.getMonth() + "/" + endDate.getYear();
                    dateButton.setText(dateBuilder);

                    createPlaceList(inputHoliday.getHolidayId(), root);

                    FloatingActionButton editFab = root.findViewById(R.id.button_edit_holiday);
                    editFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holidayName = editHolidayName.getText().toString();
                            holidayNotes = editHolidayNotes.getText().toString();
                            if (holidayName == null || holidayName.equals("")
                                    || startDate == null || endDate == null) {
                                Snackbar.make(view, "Please enter a name and dates", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null)
                                        .show();
                            } else if (startDate.after(endDate)) {
                                Snackbar.make(view, "Start date is after end date", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null)
                                        .show();
                            } else {
                                Holiday outputHoliday = new Holiday(holidayName, startDate, endDate, holidayNotes);
                                outputHoliday.setHolidayId(holidayId);
                                holidayViewModel.update(outputHoliday);
                                NavDirections action = ViewHolidayFragmentDirections.actionViewHolidayToHolidayList();
                                Navigation.findNavController(root).navigate(action);

                                FloatingActionButton deleteFab = root.findViewById(R.id.button_delete_holiday);
                                deleteFab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holidayViewModel.delete(holidayId);
                                        NavDirections action = ViewHolidayFragmentDirections.actionViewHolidayToHolidayList();
                                        Navigation.findNavController(root).navigate(action);
                                    }
                                });
                            }
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
            if (startDate == null) {
                startDate = new Date(year, month + 1, dayOfMonth);
                dateBuilder = "Start date: " + dayOfMonth + "/" + (month + 1) + "/" + year;
                dateButton.setText(dateBuilder + "\nPlease enter end date");
            } else  if (endDate == null) {
                endDate = new Date(year, month + 1, dayOfMonth);
                dateBuilder = dateBuilder + "\nEnd date: " + dayOfMonth + "/" + (month + 1) + "/" + year;
                dateButton.setText(dateBuilder);
            } else {
                endDate = null;
                startDate = new Date(year, month + 1, dayOfMonth);
                dateBuilder = "Start date: " + dayOfMonth + "/" + (month + 1) + "/" + year
                        + "\nPlease enter end date";
                dateButton.setText(dateBuilder);
            }
        }
    };

    private void createPlaceList(final int holidayId, View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        final PlaceAdapter adapter = new PlaceAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        placeViewModel.selectAllPlaces().observe(this.getViewLifecycleOwner(), new Observer<List<Places>>() {
            @Override
            public void onChanged(List<Places> places) {
                List<Places> lessPlaces = new ArrayList<>();
                for (Places place : places) {
                    if (place.getHolidayId() == holidayId) {
                        lessPlaces.add(place);
                    }
                }
                adapter.setPlaces(lessPlaces);
            }
        });
    }
}
