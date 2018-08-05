package com.pperotti.android.mapsexample.services.downloads;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.pperotti.android.mapsexample.R;
import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.services.notifications.NotificationsHelper;
import com.pperotti.android.mapsexample.services.routes.RouteManager;

public class DownloadCompletionBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = DownloadCompletionBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Toast.makeText(context, R.string.error_permission_write_external_storage_required, Toast.LENGTH_LONG).show();
            return;
        }

        //Get the download reference id
        long currentDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (currentDownloadId != -1) {
            //Create the route manager
            RouteManager routeManager = new RouteManager(context);

            //Get the info for this download
            Route route = routeManager.getRouteByDownloadId(currentDownloadId);
            if (route != null) {

                //Get the info related with the last download
                DownloadInfo downloadInfo = DownloadHelper.getFileInfoFromReference(context, currentDownloadId);

                //Update Route with new data
                route.setState(routeManager.getRouteState(downloadInfo.getDownloadStatus()));

                //Update Notification
                NotificationsHelper.presentNotification(context, route);

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
        Intent intent = DownloadParserIntentService.createIntent(context, route.getRouteId());
        context.startService(intent);
    }
}
