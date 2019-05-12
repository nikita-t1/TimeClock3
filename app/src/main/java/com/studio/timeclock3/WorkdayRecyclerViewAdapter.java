package com.studio.timeclock3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkdayRecyclerViewAdapter extends RecyclerView.Adapter<WorkdayRecyclerViewAdapter.RecyclerViewHolder> {

    private final int position;
    private final Context context;

    public WorkdayRecyclerViewAdapter(Context context, int position) {
        this.position = position;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//        holder.dateStringRecycler.setText("Porn");
//        holder.dateNumberRecycler.setText("Potato");
//        holder.startTimeRecycler.setText("Putin");
//        holder.balanceTimeRecycler.setText("Panzer");
//        holder.endTimeRecycler.setText("Putte");
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView dateStringRecycler;
        TextView dateNumberRecycler;
        TextView startTimeRecycler;
        TextView balanceTimeRecycler;
        TextView endTimeRecycler;

        public RecyclerViewHolder(View view) {
            super(view);
            dateStringRecycler = view.findViewById(R.id.dateStringRecycler);
            dateNumberRecycler = view.findViewById(R.id.dateNumberRecycler);
            startTimeRecycler = view.findViewById(R.id.startTimeRecycler);
            endTimeRecycler = view.findViewById(R.id.endTimeRecycler);
            balanceTimeRecycler = view.findViewById(R.id.balanceTimeRecycler);
        }
    }
}
