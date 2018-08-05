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

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;

/**
 * This class will perform the parsing-related operations. It will constantly notify the UI about the
 * changes in the state so the UI can be updated as soon as possible.
 */
public class DownloadParserIntentService extends IntentService {

    private static final String TAG = DownloadParserIntentService.class.getSimpleName();

    public DownloadParserIntentService() {
        super("DownloadParserIntentService");
    }

    /**
     * Create Intent to start this service to process the Route with the routeId specified.
     *
     * @param context Regular Context object.
     * @param routeId Identified created during new route setup.
     * @return Intent to be used to start this service.
     */
    public static Intent createIntent(Context context, long routeId) {
        Intent intent = new Intent(context, DownloadParserIntentService.class);
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
                route.setState(RouteState.PARSING);
                int affectedRow = routeManager.update(route);

                //Update Notification
                NotificationsHelper.presentNotification(getApplicationContext(), route);

                Log.d(TAG, String.format("AffectedRows=%d", affectedRow));
                Log.d(TAG, "Processing ...\n" + route.toString());

                //Create table to host the points
                pointsManager.createPointsTable(route.getRouteId());

                //Parse the data
                parseData(getApplicationContext(), routeManager, pointsManager, route);
            }
        }
    }

    private void parseData(Context context,
                           RouteManager routeManager,
                           PointsManager pointsManager,
                           Route route) {

        GPXParser gpxParser = new GPXParser();

        Gpx parsedGpx = null;
        try {
            File file = new File(route.getLocalFilePath());
            InputStream in = new FileInputStream(file);
            parsedGpx = gpxParser.parse(in);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            route.setState(RouteState.PARSING_FAILED);
            routeManager.update(route);

            //Display Notification
            NotificationsHelper.presentNotification(context, route);
            return;
        }

        if (parsedGpx == null) {
            Log.e(TAG, "PARSING DATA FAILED!");
            route.setState(RouteState.PARSING_FAILED);
        } else {
            Log.d(TAG, "PARSING DATA");

            //Print Track List
            List<Track> trackList = parsedGpx.getTracks();
            for (Track track : trackList) {
                processTrack(pointsManager, route, track);
            }

            //Update the State to PARSED
            route.setState(RouteState.PARSED);

            //TODO: start processing (maybe another intent service?
        }

        routeManager.update(route);
        Log.d(TAG, "PARSE PROCESS COMPLETED!" + route.toString());

        //Update Notification
        NotificationsHelper.presentNotification(getApplicationContext(), route);

        //It's time to move on so going to next step to process the data
        startProcessorIntentService(context, route);
    }

    private void processTrack(PointsManager pointsManager, Route route, Track track) {
        Log.d(TAG, String.format("Track name=%s number=%d",
                track.getTrackName(),
                track.getTrackNumber()));

        List<TrackSegment> segmentList = track.getTrackSegments();
        for (TrackSegment currentSegment : segmentList) {
            processSegment(pointsManager, route, currentSegment);
        }
    }

    private void processSegment(PointsManager pointsManager, Route route, TrackSegment trackSegment) {
        List<TrackPoint> trackPointList = trackSegment.getTrackPoints();
        int progress = 1;
        int max = trackPointList.size();
        long routeId = route.getRouteId();
        for (TrackPoint trackPoint : trackPointList ) {
            pointsManager.insertTrackPoint(routeId, trackPoint.getLatitude(), trackPoint.getLongitude());

            //Update Notification Progress¡
            NotificationsHelper.updateNotification(getApplicationContext(), route, progress, max);

            //Make sure we update the progress.
            progress++;
        }
    }

    private void startProcessorIntentService(Context context, Route route) {
        Intent intent = DownloadProcessorIntentService.createIntent(context, route.getRouteId());
        context.startService(intent);
    }
}
