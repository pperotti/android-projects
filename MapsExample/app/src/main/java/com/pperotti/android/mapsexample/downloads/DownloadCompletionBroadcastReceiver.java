package com.pperotti.android.mapsexample.downloads;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pperotti.android.mapsexample.utils.DownloadHelper;
import com.pperotti.android.mapsexample.utils.NotificationsHelper;
import com.pperotti.android.mapsexample.utils.StorageHelper;

public class DownloadCompletionBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = DownloadCompletionBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the download reference id
        long currentDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (currentDownloadId != -1) {
            //Present Notification
            NotificationsHelper.presentDownloadCompletedNotification(context);

            //Remove Notification from common storage
            StorageHelper.removeLong(context, StorageHelper.DOWNLOAD_IN_PROGRESS_ID);

            //Get the info for this download
            DownloadInfo downloadInfo = DownloadHelper.getFileInfoFromReference(context, currentDownloadId);
            Log.d(TAG, String.format("Download Info\nReference: %d\nStatus: %d\nFile Path: %s",
                    downloadInfo.getDownloadReference(),
                    downloadInfo.getDownloadStatus(),
                    downloadInfo.getLocalFilePath()
            ));

            //TODO: Process the data (in a different thread)

            //TODO: Notify other components if needed
        }
    }
}
