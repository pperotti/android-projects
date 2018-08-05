package com.pperotti.android.mapsexample.services.downloads;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.services.routes.RouteManager;

/**
 * This class will perform the POST-DOWNLOAD operations. It will constantly notify the UI about the
 * changes in the state so the UI can be updated as soon as possible.
 */
public class DownloadProcessorIntentService extends IntentService {

    private static final String TAG = DownloadProcessorIntentService.class.getSimpleName();

    public static final String EXTRA_ROUTE_ID = "extras_route_id";
    public static final long DEFAULT_NO_ROUTE_ID = -1;

    public DownloadProcessorIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long routeId = intent.getLongExtra(EXTRA_ROUTE_ID, DEFAULT_NO_ROUTE_ID);
        if (routeId != DEFAULT_NO_ROUTE_ID) {

            RouteManager routeManager = new RouteManager(getApplicationContext());

            Route route = routeManager.getRouteByRouteId(routeId);
            Log.d(TAG, "Processing ...\n" + route.toString());

        }
    }

    /**
     * Create Intent to start this service to process the Route with the routeId specified.
     *
     * @param routeId Identified created during new route setup.
     * @return Intent to be used to start this service.
     */
    public static Intent createIntent(long routeId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ROUTE_ID, routeId);
        return intent;
    }
}
