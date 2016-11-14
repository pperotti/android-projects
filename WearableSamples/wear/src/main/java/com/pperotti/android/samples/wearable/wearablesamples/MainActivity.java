package com.pperotti.android.samples.wearable.wearablesamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ListView mListView;
    private ArrayAdapter<String> mDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1);
        for (int i=0;i<50;i++) {
            mDataAdapter.add("Text" + i);
        }

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mListView = (ListView) stub.findViewById(R.id.listView);
                mListView.setAdapter(mDataAdapter);
            }
        });




    }
}
