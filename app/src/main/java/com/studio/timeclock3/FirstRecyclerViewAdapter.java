package com.studio.timeclock3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class FirstRecyclerViewAdapter extends RecyclerView.Adapter<FirstRecyclerViewAdapter.ViewHolder> {

    private RecyclerDay[] recyclerDays;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    FirstRecyclerViewAdapter(Context context, RecyclerDay[] recyclerDays) {
        this.mInflater = LayoutInflater.from(context);
        this.recyclerDays = recyclerDays;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //verknüpfen des Layouts
        View view = mInflater.inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecyclerDay theRecyclerDays = recyclerDays[position];
        holder.dateStringRecycler.setText(recyclerDays[position].getDateStringRecycler());
        holder.dateNumberRecycler.setText(String.valueOf(recyclerDays[position].getDateNumberRecycler()));
//        holder.balanceTimeRecycler.setText("+" + String.valueOf((String.format("%.1f", Math.random() *1 + 0.5))));
        holder.startTimeRecycler.setText(recyclerDays[position].getStartTimeRecycler());
        holder.endTimeRecycler.setText(recyclerDays[position].getEndTimeRecycler());
        holder.balanceTimeRecycler.setText(String.valueOf(recyclerDays[position].getBalanceTimeRecycler()));
        holder.constraint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"click on date: " + theRecyclerDays.getDateNumberRecycler(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return recyclerDays.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //verknüpfen der Views(Id)
        TextView dateStringRecycler;
        TextView dateNumberRecycler;
        TextView startTimeRecycler;
        TextView endTimeRecycler;
        TextView balanceTimeRecycler;
        ConstraintLayout constraint_layout;


        ViewHolder(View itemView) {
            super(itemView);
            dateStringRecycler = itemView.findViewById(R.id.dateStringRecycler);
            dateNumberRecycler = itemView.findViewById(R.id.dateNumberRecycler);
            startTimeRecycler = itemView.findViewById(R.id.startTimeRecycler);
            endTimeRecycler = itemView.findViewById(R.id.endTimeRecycler);
            balanceTimeRecycler = itemView.findViewById(R.id.balanceTimeRecycler);
            constraint_layout = itemView.findViewById(R.id.constraint_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return recyclerDays.get(id);
//    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
