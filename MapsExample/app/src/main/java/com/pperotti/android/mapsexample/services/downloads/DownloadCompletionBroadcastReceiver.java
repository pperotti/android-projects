package com.pperotti.android.mapsexample.services.downloads;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.services.notifications.NotificationsHelper;
import com.pperotti.android.mapsexample.services.routes.RouteManager;

public class DownloadCompletionBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = DownloadCompletionBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the download reference id
        long currentDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (currentDownloadId != -1) {
            //Present Notification
            NotificationsHelper.presentDownloadCompletedNotification(context);

            //Create the route manager
            RouteManager routeManager = new RouteManager(context);

            //Get the info for this download
            Route route = routeManager.getRouteByDownloadId(currentDownloadId);
            if (route != null) {
                //Get the info related with the last download
                DownloadInfo downloadInfo = DownloadHelper.getFileInfoFromReference(context, currentDownloadId);

                //Update Route with new data
                route.setLocalFilePath(downloadInfo.getLocalFilePath());
                route.setState(routeManager.getRouteState(downloadInfo.getDownloadStatus()));

                int affectedRows = routeManager.update(route);
                Log.d(TAG, String.format("Routes Updated: %d", affectedRows));

                //Print the new route
                Log.d(TAG, "" + route.toString());

                //Start POST DOWNLOAD processing
                startPostProcessingIntentService(context, route);
            } else {
                Log.e(TAG, "No Route stored in the phone");
            }
        }
    }

    private void startPostProcessingIntentService(Context context, Route route) {
        Intent intent = DownloadProcessorIntentService.createIntent(context, route.getRouteId());
        context.startService(intent);
    }
}
