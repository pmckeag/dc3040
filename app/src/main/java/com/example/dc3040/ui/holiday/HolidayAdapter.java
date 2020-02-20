package com.example.dc3040.ui.holiday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dc3040.R;
import com.example.dc3040.model.Holiday;

import java.util.List;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder> {

    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Holiday> mHolidays;

    public HolidayAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HolidayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new HolidayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HolidayViewHolder holder, int position) {
        if (mHolidays != null) {
            Holiday current = mHolidays.get(position);
            holder.HolidayItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.HolidayItemView.setText("No Holidays");
        }
    }

    void setHolidays(List<Holiday> holidays){
        mHolidays = holidays;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mHolidays has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mHolidays != null)
            return mHolidays.size();
        else return 0;
    }

    class HolidayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView HolidayItemView;

        private HolidayViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            HolidayItemView = itemView.findViewById(R.id.textView);
        }

        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        HolidayAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
