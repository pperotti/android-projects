package com.pperotti.android.mapsexample.services.points;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.pperotti.android.mapsexample.services.database.MapsExampleDatabase;

public class PointsManager {

    private static final String TAG = PointsProvider.class.getSimpleName();

    private ContentResolver contentResolver;

    public PointsManager(Context context) {
        contentResolver = context.getContentResolver();
    }

    /**
     * Remove all the information available in the DB
     */
    public void clearAllPoints(long routeId) {
        if (contentResolver != null) {
            try {
                Uri uri = PointsProvider.DataContract.buildPointsCreateUri(routeId);
                contentResolver.delete(uri, null, null);
            } catch (Exception e) {
                Log.d(TAG, "" + e.toString());
            }
        }
    }

    /**
     * Create the table associated with the routeId specified.
     *
     * @param routeId The routeId that identifies a download.
     */
    public void createPointsTable(long routeId) {
        if (contentResolver != null) {
            try {
                Uri uri = PointsProvider.DataContract.buildPointsCreateUri(routeId);
                contentResolver.insert(uri, null);
            } catch (Exception e) {
                Log.d(TAG, "" + e.toString());
            }
        }
    }

    /**
     * Insert a point into the points table associated with routeId.
     *
     * @param routeId RouteId that identifies the download.
     * @param latitude Double value as parsed from GPX parser.
     * @param longitude Double value as parsed from GPX parser.
     */
    public void insertTrackPoint(long routeId, double latitude, double longitude) {
        if (contentResolver != null) {
            try {
                Uri uri = PointsProvider.DataContract.buildPointsItemUri(routeId);
                ContentValues contentValues = new ContentValues();
                contentValues.put(MapsExampleDatabase.PT_COLUMN_LATITUDE, latitude);
                contentValues.put(MapsExampleDatabase.PT_COLUMN_LONGITUDE, longitude);
                contentResolver.insert(uri, contentValues);
            } catch (Exception e) {
                Log.e(TAG, "" + e.toString());
            }
        }
    }

    /**
     * Retrieve a Cursor so the reader can work with points.
     *
     * @return Retrieve a cursor with all the points inserted for this purpose.
     */
    public Cursor getPoints(long routeId) {
        if (contentResolver != null) {
            String[] projection = {
                    MapsExampleDatabase.PT_COLUMN_LATITUDE,
                    MapsExampleDatabase.PT_COLUMN_LONGITUDE
            };
            Uri uri = PointsProvider.DataContract.buildPointsItemUri(routeId);
            return contentResolver.query(uri, null, null, null, null);
        }
        return null;
    }
}
