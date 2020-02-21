package com.example.dc3040.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
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
import com.example.dc3040.ui.place.PlaceAdapter;
import com.example.dc3040.ui.place.PlaceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private PlaceViewModel placeViewModel;
    private LiveData<List<Places>> observable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        placeViewModel = new ViewModelProvider(this.getActivity()).get(PlaceViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_images);
        final GalleryAdapter adapter = new GalleryAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter.setOnItemClickListener(new GalleryAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                sendGalleryId(position + 1);
                    NavDirections action = GalleryFragmentDirections.actionGalleryToPicture();
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

    public void sendGalleryId(int galleryId) {
        MainActivity.setGalleryId(galleryId);
    }
}