package com.example.dc3040.ui.place;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewPlaceFragment extends Fragment {

    private PlaceViewModel placeViewModel;
    private HolidayViewModel holidayViewModel;
    private EditText editPlaceName;
    private Date date;
    private Button dateButton;
    private EditText editPlaceNotes;
    private int holidayAssoc;
    private List<Holiday> holidayList;
    private Button photoButton;
    private String photoLocation;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;

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

        photoButton = root.findViewById(R.id.button_photo);

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        photoLocation = "";

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

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
                            photoLocation);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getActivity(),
                        "com.example.dc3040.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        photoLocation = image.getAbsolutePath();
        return image;
    }
}