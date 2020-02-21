package com.example.dc3040.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dc3040.MainActivity;
import com.example.dc3040.R;
import com.example.dc3040.model.Places;
import com.example.dc3040.ui.place.PlaceViewModel;

import java.util.List;

public class PictureFragment extends Fragment {

    private int galleryId;
    private PlaceViewModel placeViewModel;
    private Places photoPlace;
    private LiveData<List<Places>> observable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryId = MainActivity.getGalleryId();
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_picture, container, false);
        observable = placeViewModel.selectAllPlaces();

        observable.observe(this.getViewLifecycleOwner(), new Observer<List<Places>>() {
            @Override
            public void onChanged(List<Places> places) {
                for (Places place : places) {
                    if (place.getPlaceId() == galleryId) {
                        photoPlace = place;
                    }
                }
                displayPicture(photoPlace, root);
            }
        });

        return root;
    }

    private void displayPicture(Places photoPlace, View root) {
        ImageView imageView = root.findViewById(R.id.image);
        Glide.with(this.getActivity())
                .load(photoPlace.getPhoto())
                .asBitmap()
                .error(R.drawable.ic_cloud_off_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
