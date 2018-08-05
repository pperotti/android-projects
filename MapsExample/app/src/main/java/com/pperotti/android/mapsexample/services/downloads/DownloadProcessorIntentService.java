package com.pperotti.android.mapsexample.services.downloads;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.domain.routes.RouteState;
import com.pperotti.android.mapsexample.services.notifications.NotificationsHelper;
import com.pperotti.android.mapsexample.services.points.PointsManager;
import com.pperotti.android.mapsexample.services.routes.RouteManager;

/**
 * This class is responsible for reading all the data from the PointsProvider
 * in order to process that data. The UI will be constantly notified.
 */
public class DownloadProcessorIntentService extends IntentService {

    private static final String TAG = DownloadProcessorIntentService.class.getSimpleName();

    public DownloadProcessorIntentService() {
        super("DownloadProcessorIntentService");
    }

    /**
     * Create Intent to start this service to process the Route with the routeId specified.
     *
     * @param context Regular Context object.
     * @param routeId Identified created during new route setup.
     * @return Intent to be used to start this service.
     */
    public static Intent createIntent(Context context, long routeId) {
        Intent intent = new Intent(context, DownloadProcessorIntentService.class);
        intent.putExtra(RouteManager.EXTRA_ROUTE_ID, routeId);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "INTENT=" + intent);
        if (intent != null) {
            long routeId = intent.getLongExtra(
                    RouteManager.EXTRA_ROUTE_ID,
                    RouteManager.DEFAULT_NO_ROUTE_ID);
            if (routeId != RouteManager.DEFAULT_NO_ROUTE_ID) {

                RouteManager routeManager = new RouteManager(getApplicationContext());
                PointsManager pointsManager = new PointsManager(getApplicationContext());

                Route route = routeManager.getRouteByRouteId(routeId);
                route.setState(RouteState.PROCESSING);
                int affectedRow = routeManager.update(route);

                //Update Notification
                NotificationsHelper.presentNotification(getApplicationContext(), route);

                //TODO: Implement the actual processing
            }
        }
    }
}
