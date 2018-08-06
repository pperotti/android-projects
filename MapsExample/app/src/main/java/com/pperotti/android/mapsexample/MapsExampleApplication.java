package com.pperotti.android.mapsexample;

import android.app.Application;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import com.pperotti.android.mapsexample.services.downloads.DownloadCompletionBroadcastReceiver;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.File;
import java.nio.channels.FileLockInterruptionException;

public class MapsExampleApplication extends Application {

    private static final String TAG = MapsExampleApplication.class.getSimpleName();

    DownloadCompletionBroadcastReceiver downloadCompletionBroadcastReceiver = new DownloadCompletionBroadcastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate()");

        JodaTimeAndroid.init(this);

        registerReceiver(downloadCompletionBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        File cacheDir = getCacheDir();
        Log.d(TAG, "getCacheDir()=" + cacheDir.toString() + " canRead=" + cacheDir.canRead());
        File filesDir = new File(getFilesDir() + "/gpx");
        if (filesDir.exists()) {
            Log.d(TAG, filesDir.toString() + " exists");
        } else {
            Log.d(TAG, "creating dir: " + filesDir.toString());
            filesDir.mkdir();
        }
        //getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) -> /storage/emulated/0/Android/data/com.pperotti.android.mapsexample/files/Download
        //getFilesDir() -> /data/user/0/com.pperotti.android.mapsexample/files/

        String localFilePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                + File.separator
                + "gpx"
                + File.separator
                + "123";
        Log.d(TAG, "F="+ localFilePath);
        Log.d(TAG, "F=getExternalStorageDirectory"+ Environment.getExternalStorageDirectory());
        Log.d(TAG, "F=getExternalStoragePublicDirectory="+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        Log.d(TAG, "F=getRootDirectory="+ Environment.getRootDirectory());


//        Log.d(TAG, "getFilesDir()=" + filesDir.toString() + " exists=" + filesDir.canRead());
//        Log.d(TAG, "getDir(Environment.DIRECTORY_DOWNLOADS)=" + getDir(Environment.DIRECTORY_DOWNLOADS, MODE_PRIVATE));



    }

}
