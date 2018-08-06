package com.pperotti.android.mapsexample.services.routes;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.domain.routes.RouteState;

/**
 * This class helps interacting with the ROUTES DB.
 */
public class RouteManager {

    //Value to use as key in extras to pass route id.
    public static final String EXTRA_ROUTE_ID = "extras_route_id";
    //Constant to define there is no valid route id identified.
    public static final long DEFAULT_NO_ROUTE_ID = -1;
    private static final String TAG = RouteManager.class.getSimpleName();
    private ContentResolver contentResolver;

    public RouteManager(Context context) {
        contentResolver = context.getContentResolver();
    }

    /**
     * Remove all the information available in the DB
     */
    public void clearAllRoutes() {
        if (contentResolver != null) {
            try {
                contentResolver.delete(
                        RouteProvider.DataContract.CONTENT_URI, null, null);
            } catch (Exception e) {
                Log.d(TAG, "" + e.toString());
            }
        }
    }

    /**
     * Persiste the received Route object to the DB.
     *
     * @param route The Route object to add.
     */
    public void add(Route route) {
        //Validate route
        if (route == null || route.getRouteId() == 0) {
            throw new IllegalArgumentException("You should pass a valid Route object.");
        }

        if (contentResolver != null) {

            ContentValues values = new ContentValues();
            values.put(RouteProvider.Columns.ROUTE_ID, route.getRouteId());
            values.put(RouteProvider.Columns.DOWNLOAD_ID, route.getDownloadId());
            values.put(RouteProvider.Columns.DOWNLOAD_URL, route.getDownloadUrl());
            values.put(RouteProvider.Columns.LOCAL_PATH, route.getLocalFilePath());
            values.put(RouteProvider.Columns.STATE, route.getState().name());
            values.put(RouteProvider.Columns.NAME, route.getName());
            values.put(RouteProvider.Columns.DOWNLOAD_TIMESTAMP, route.getDownloadTimestamp());

            try {
                contentResolver.insert(
                        RouteProvider.DataContract.CONTENT_URI, values);
            } catch (Exception e) {
                Log.e(TAG, "Cannot add this Route: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieve the Route object from the routeId.
     *
     * @param routeId The ID used to download the route.
     * @return The Route object associated with downloadId.
     */
    public Route getRouteByRouteId(long routeId) {
        Route route = null;
        if (contentResolver != null) {
            String selection = RouteProvider.Columns.ROUTE_ID + " = ?";
            String[] selectionArgs = {String.valueOf(routeId)};
            Cursor cursor = contentResolver.query(
                    RouteProvider.DataContract.CONTENT_URI,
                    RouteProvider.Columns.ALL_COLUMNS_PROJECTION,
                    selection,
                    selectionArgs,
                    null
            );
            if (cursor != null) {
                route = getRouteFromCursor(cursor);
            }
        }
        return route;
    }

    /**
     * Retrieve the Route object from the downloadId.
     *
     * @param downloadId The ID used to download the route.
     * @return The Route object associated with downloadId.
     */
    public Route getRouteByDownloadId(long downloadId) {
        Route route = null;
        if (contentResolver != null) {
            String selection = RouteProvider.Columns.DOWNLOAD_ID + " = ?";
            String[] selectionArgs = {String.valueOf(downloadId)};
            Cursor cursor = contentResolver.query(
                    RouteProvider.DataContract.CONTENT_URI,
                    RouteProvider.Columns.ALL_COLUMNS_PROJECTION,
                    selection,
                    selectionArgs,
                    null
            );
            if (cursor != null) {
                route = getRouteFromCursor(cursor);
            }
        }
        return route;
    }


    private Route getRouteFromCursor(Cursor cursor) {
        try {
            cursor.moveToFirst();

            int routeIdIndex = cursor.getColumnIndex(RouteProvider.Columns.ROUTE_ID);
            int downloadIdIndex = cursor.getColumnIndex(RouteProvider.Columns.DOWNLOAD_ID);
            int downloadUrlIndex = cursor.getColumnIndex(RouteProvider.Columns.DOWNLOAD_URL);
            int localPathIndex = cursor.getColumnIndex(RouteProvider.Columns.LOCAL_PATH);
            int stateIndex = cursor.getColumnIndex(RouteProvider.Columns.STATE);
            int nameIndex = cursor.getColumnIndex(RouteProvider.Columns.NAME);
            int timestampIndex = cursor.getColumnIndex(RouteProvider.Columns.DOWNLOAD_TIMESTAMP);

            return new Route()
                    .setRouteId(cursor.getLong(routeIdIndex))
                    .setDownloadId(cursor.getLong(downloadIdIndex))
                    .setDownloadUrl(cursor.getString(downloadUrlIndex))
                    .setLocalFilePath(cursor.getString(localPathIndex))
                    .setState(RouteState.valueOf(cursor.getString(stateIndex)))
                    .setName(cursor.getString(nameIndex))
                    .setDownloadTimestamp(cursor.getLong(timestampIndex));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * Retrieves a valid RouteState object based on the download status obtain after the download was completed
     *
     * @param downloadStatus The download status as received from DownloadManager.
     * @return A valid RouteState.
     */
    public RouteState getRouteState(int downloadStatus) {
        switch (downloadStatus) {
            case DownloadManager.STATUS_FAILED:
                return RouteState.NOT_DOWNLOADED;
            case DownloadManager.STATUS_SUCCESSFUL:
                return RouteState.DOWNLOADED;
            default:
                return RouteState.ENQUEUED;
        }
    }

    /**
     * Update the information in the database with the information in the Route object.
     *
     * @param route The Route object used to update database.
     */
    public int update(Route route) {
        if (route != null && contentResolver != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(RouteProvider.Columns.ROUTE_ID, route.getRouteId());
                values.put(RouteProvider.Columns.DOWNLOAD_ID, route.getDownloadId());
                values.put(RouteProvider.Columns.DOWNLOAD_URL, route.getDownloadUrl());
                values.put(RouteProvider.Columns.LOCAL_PATH, route.getLocalFilePath());
                values.put(RouteProvider.Columns.STATE, route.getState().name());
                values.put(RouteProvider.Columns.NAME, route.getName());
                values.put(RouteProvider.Columns.DOWNLOAD_TIMESTAMP, route.getDownloadTimestamp());

                String where = RouteProvider.Columns.ROUTE_ID + "= ?";
                String[] whereArgs = {String.valueOf(route.getRouteId())};

                return contentResolver.update(
                        RouteProvider.DataContract.CONTENT_URI,
                        values,
                        where,
                        whereArgs
                );
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }
        return 0;
    }
}
