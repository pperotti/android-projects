package com.pperotti.android.one2one.home.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.model.Task;

import java.util.Calendar;

/**
 * Created by pperotti on 12/6/16.
 */
public class HomeItemViewHolder extends RecyclerView.ViewHolder{

    View itemView;

    TextView tvSubject;
    TextView tvSummary;
    TextView tvDate;
    ImageView delete;

    HomeItemDeleteListener deletionListener;

    public HomeItemViewHolder(View itemView, HomeItemDeleteListener deletionListener) {
        super(itemView);

        tvSubject = (TextView) itemView.findViewById(R.id.home_itemEntrySubject);
        tvSummary = (TextView) itemView.findViewById(R.id.home_itemEntrySummary);
        tvDate = (TextView) itemView.findViewById(R.id.home_itemEntryDate);
        delete = (ImageView) itemView.findViewById(R.id.home_itemEntryDelete);

        this.deletionListener = deletionListener;
    }

    public final View bind(final Task item) {

        tvSubject.setText(item.getSubject());
        tvSummary.setText(Html.fromHtml(item.getContent()));

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(item.getTimeInMillis());

        tvDate.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Remove
                item.delete();

                deletionListener.onItemDeleted(item);
            }
        });

        return itemView;
    }

    public interface HomeItemDeleteListener {
        void onItemDeleted(Task deletedItem);
    }
}
