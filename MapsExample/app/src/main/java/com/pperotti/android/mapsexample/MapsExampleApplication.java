package com.pperotti.android.mapsexample;

import android.app.Application;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.util.Log;

import com.pperotti.android.mapsexample.downloads.DownloadCompletionBroadcastReceiver;

public class MapsExampleApplication extends Application {

    private static final String TAG = MapsExampleApplication.class.getSimpleName();

    DownloadCompletionBroadcastReceiver downloadCompletionBroadcastReceiver = new DownloadCompletionBroadcastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate()");

        registerReceiver(downloadCompletionBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
