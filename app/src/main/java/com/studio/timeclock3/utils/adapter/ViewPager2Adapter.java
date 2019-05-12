package com.studio.timeclock3.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.studio.timeclock3.R;

import java.util.ArrayList;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewPager2Holder> {

    private final Context context;
    private ArrayList amountOfWeeks;

    public ViewPager2Adapter(Context context, ArrayList amountOfWeeks) {
        this.context=context;
        this.amountOfWeeks = amountOfWeeks;
    }

    @NonNull
    @Override
    public ViewPager2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPager2Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPager2Holder holder, int position) {

        Integer single = (Integer) amountOfWeeks.get(position);
        Logger.i(String.valueOf(single));
        WorkdayRecyclerViewAdapter workdayRecyclerViewAdapter = new WorkdayRecyclerViewAdapter(context, single);
        holder.recyclerPage.setAdapter(workdayRecyclerViewAdapter);
        Logger.i("HOLDER: " + holder.recyclerPage.getChildCount());
        holder.recyclerPage.setHasFixedSize(true);
        holder.recyclerPage.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    @Override
    public int getItemCount() {
        return amountOfWeeks.size();
    }

    public class ViewPager2Holder extends RecyclerView.ViewHolder {
        RecyclerView recyclerPage;


        public ViewPager2Holder(@NonNull View itemView) {
            super(itemView);
            recyclerPage = itemView.findViewById(R.id.recyclerPage);
        }
    }
}
