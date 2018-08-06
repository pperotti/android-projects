package com.pperotti.android.mapsexample.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.pperotti.android.mapsexample.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Adapter that works with Cursors.
 */
public class MyRouteRecyclerViewAdapter extends RecyclerViewCursorAdapter<MyRouteRecyclerViewAdapter.RouteViewHolder> {

    private static final String TAG = MyRouteRecyclerViewAdapter.class.getSimpleName();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


//    private final List<RouteItem> mValues;
//    private final OnListFragmentInteractionListener mListener;
//
//    public MyRouteRecyclerViewAdapter(List<RouteItem> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    public MyRouteRecyclerViewAdapter(Context context) {
        super(context);
        setupCursorAdapter(null, 0, R.layout.item_route, false);
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RouteViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(final RouteViewHolder holder, int position) {

        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    public class RouteViewHolder extends RecyclerViewCursorViewHolder {
        public final TextView fileName;
        public final TextView filePath;
        public final TextView state;
        public final TextView timestamp;

        public RouteViewHolder(View view) {
            super(view);
            fileName = view.findViewById(R.id.route_file_name);
            filePath = view.findViewById(R.id.route_file_path);
            state = view.findViewById(R.id.route_state);
            timestamp = view.findViewById(R.id.route_timestamp);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            filePath.setText(cursor.getString(4));
            fileName.setText(cursor.getString(5));
            state.setText(cursor.getString(6));
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(cursor.getLong(7));
            timestamp.setText(simpleDateFormat.format(c.getTime()));
        }
    }
}
