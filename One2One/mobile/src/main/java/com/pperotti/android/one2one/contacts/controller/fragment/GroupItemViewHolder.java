package com.pperotti.android.one2one.contacts.controller.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.model.Group;

/**
 * Created by pperotti on 12/6/16.
 */
public class GroupItemViewHolder extends RecyclerView.ViewHolder{

    View itemView;

    TextView tvTitle;
    TextView tvContactNumber;

    public GroupItemViewHolder(View itemView) {
        super(itemView);

        tvTitle = (TextView) itemView.findViewById(R.id.group_title);
        tvContactNumber = (TextView) itemView.findViewById(R.id.group_contactNumber);
    }

    public final View bind(final Group item) {

        if (tvTitle != null) tvTitle.setText(item.getTitle());
        //if (tvContactNumber != null) tvContactNumber.setText(item.getContactNumber());

        return itemView;
    }

}
