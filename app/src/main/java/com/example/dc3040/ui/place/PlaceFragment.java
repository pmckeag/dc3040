package com.example.dc3040.ui.place;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dc3040.MainActivity;
import com.example.dc3040.R;
import com.example.dc3040.model.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PlaceFragment extends Fragment {

    private PlaceViewModel placeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_place, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = PlaceFragmentDirections.actionPlacesListToNewPlace();
                Navigation.findNavController(root).navigate(action);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        final PlaceAdapter adapter = new PlaceAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter.setOnItemClickListener(new PlaceAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                sendPlaceId(position + 1);
                NavDirections action = PlaceFragmentDirections.actionPlacesListToViewPlace();
                Navigation.findNavController(root).navigate(action);
            }
        });

        placeViewModel.selectAllPlaces().observe(this.getViewLifecycleOwner(), new Observer<List<Places>>() {
            @Override
            public void onChanged(List<Places> places) {
                adapter.setPlaces(places);
            }
        });

        return root;
    }

    public void sendPlaceId(int placeId) {
        MainActivity.setPlaceId(placeId);
    }
}