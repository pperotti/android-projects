package com.pperotti.android.one2one.contacts.controller.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pperotti on 12/6/16.
 */
public class ContactListAdapter extends RecyclerView.Adapter<GroupItemViewHolder> {

    private List<Group> mGroups = new ArrayList<Group>();

    public ContactListAdapter(Bundle savedInstanceState) {

    }

    @Override
    public GroupItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new GroupItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupItemViewHolder holder, int position) {

        Group group = mGroups.get(position);

        //Get item from collection and populate the data
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public void addItem(Group group) {
        this.mGroups.add(group);
    }

    public Group getItem(int position) {
        return this.mGroups.get(position);
    }

    public void addAll(List<Group> groups) {
        this.mGroups = groups;
    }
}
