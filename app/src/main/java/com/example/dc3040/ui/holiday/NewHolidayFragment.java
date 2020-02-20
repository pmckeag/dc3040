package com.example.dc3040.ui.holiday;

import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;
import com.example.dc3040.ui.DateFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class NewHolidayFragment extends Fragment {
    private HolidayViewModel holidayViewModel;
    private EditText editHolidayView;
    private Date startDate;
    private Date endDate;
    private Button startDateButton;
    private String dateBuilder;
    private EditText editHolidayNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_newholiday, container, false);
        editHolidayView = root.findViewById(R.id.edit_name);
        editHolidayNotes = root.findViewById(R.id.edit_notes);
        holidayViewModel = new ViewModelProvider(this).get(HolidayViewModel.class);

        startDateButton = root.findViewById(R.id.show_start_date);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDatePicker(root);
            }
        });

        Button saveButton = root.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Holiday newHoliday;
                if (TextUtils.isEmpty(editHolidayView.getText()) || startDate == null || endDate == null) {
                    Snackbar.make(view, "Please enter a name and dates", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                } else if (startDate.after(endDate)) {
                    Snackbar.make(view, "Start date is after end date", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                } else {
                    newHoliday = new Holiday(editHolidayView.getText().toString(),
                            startDate,
                            endDate,
                            editHolidayNotes.getText().toString());
                    holidayViewModel.insert(newHoliday);
                    NavDirections action = NewHolidayFragmentDirections.actionNewHolidayToHolidayList();
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
            if (startDate == null) {
                startDate = new Date(year, month + 1, dayOfMonth);
                dateBuilder = "Start date: " + dayOfMonth + "/" + (month + 1) + "/" + year;
                startDateButton.setText(dateBuilder +  "\nPlease enter end date");
            } else  if (endDate == null) {
                endDate = new Date(year, month + 1, dayOfMonth);
                dateBuilder = dateBuilder + "\nEnd date: " +  + dayOfMonth + "/" + (month + 1) + "/" + year;
                startDateButton.setText(dateBuilder);
            } else {
                endDate = null;
                startDate = new Date(year, month + 1, dayOfMonth);
                dateBuilder = "Start date: " + dayOfMonth + "/" + (month + 1) + "/" + year
                        + "\nPlease enter end date";
                startDateButton.setText(dateBuilder);
            }
        }
    };
}
