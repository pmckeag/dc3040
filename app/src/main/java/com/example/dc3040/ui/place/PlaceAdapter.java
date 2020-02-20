package com.example.dc3040.ui.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dc3040.R;
import com.example.dc3040.model.Places;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private final LayoutInflater mInflater;
    private List<Places> mplaces; // Cached copy of places

    public PlaceAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        if (mplaces != null) {
            Places current = mplaces.get(position);
            holder.placeItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.placeItemView.setText("No places");
        }
    }

    public void setPlaces(List<Places> places){
        mplaces = places;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mplaces has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mplaces != null)
            return mplaces.size();
        else return 0;
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final TextView placeItemView;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            placeItemView = itemView.findViewById(R.id.textView);
        }
    }

}
