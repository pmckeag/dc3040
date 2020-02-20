package com.example.dc3040.ui.holiday;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dc3040.MainActivity;
import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.annotation.Target;
import java.util.List;

public class HolidayFragment extends Fragment {

    private HolidayViewModel holidayViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        holidayViewModel = new ViewModelProvider(this).get(HolidayViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_holiday, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = HolidayFragmentDirections.actionHolidayListToNewHoliday();
                Navigation.findNavController(root).navigate(action);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        final HolidayAdapter adapter = new HolidayAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter.setOnItemClickListener(new HolidayAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                sendHolidayId(position + 1);
            }
        });

        holidayViewModel.getAllHolidays().observe(this.getViewLifecycleOwner(), new Observer<List<Holiday>>() {
            @Override
            public void onChanged(List<Holiday> holidays) {
                adapter.setHolidays(holidays);
            }
        });

        return root;
    }

    public void sendHolidayId(int holidayId) {
        Bundle bundle = new Bundle();
        bundle.putInt("holidayId", holidayId);

        ViewHolidayFragment viewHolidayFragment = new ViewHolidayFragment();
        viewHolidayFragment.setArguments(bundle);

        getChildFragmentManager().beginTransaction().replace(R.id.content, viewHolidayFragment).commit();
    }

}
