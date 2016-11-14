package com.pperotti.android.one2one.home.view.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pperotti on 12/6/16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeItemViewHolder> {

    private List<Task> mTasks = new ArrayList<Task>();

    public HomeAdapter(Bundle savedInstanceState) {

    }

    @Override
    public HomeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new HomeItemViewHolder(view, new DeletionListener());
    }

    @Override
    public void onBindViewHolder(HomeItemViewHolder holder, int position) {

        Task task = mTasks.get(position);

        //Get item from collection and populate the data
        final View itemView = holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void addAll(List<Task> items) {
        this.mTasks = items;
    }

    private class DeletionListener implements HomeItemViewHolder.HomeItemDeleteListener {

        @Override
        public void onItemDeleted(Task deletedItem) {
            mTasks.remove(deletedItem);
            notifyDataSetChanged();
        }
    }
}
