package com.example.dc3040.ui.gallery;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dc3040.R;
import com.example.dc3040.model.Places;

import org.w3c.dom.Text;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Places> mplaces; // Cached copy of places
    private Context context;

    public GalleryAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.rv_images, parent, false);
        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if (mplaces != null) {
            Places current = mplaces.get(position);
            ImageView imageView = holder.galleryItemView;
            TextView labelView = holder.galleryLabelView;

            Glide.with(context)
                    .load(current.getPhoto())
                    .placeholder(R.drawable.ic_cloud_off_black_24dp)
                    //.override(36, 36)
                    .fitCenter()
                    .into(imageView);
            labelView.setText(current.getName());

        } else {
            Log.v("oops", "ALL NULLS");
            // Covers the case of data not being ready yet.
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

    class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView galleryItemView;
        private final TextView galleryLabelView;

        private GalleryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            galleryItemView = itemView.findViewById(R.id.iv_photo);
            galleryLabelView = itemView.findViewById(R.id.iv_label);
        }

        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        GalleryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
