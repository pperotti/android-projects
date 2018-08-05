package com.pperotti.android.mapsexample.services.points;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pperotti.android.mapsexample.services.database.MapsExampleDatabase;

/**
 * Expose the Points-* tables data to the application.
 */
public class PointsProvider extends ContentProvider {

    // Use it when you need all the items available in the storage
    public static final int ITEMS = 2001;
    // Use it when you need to retrieve only 1 item from the storage
    public static final int ITEM = 2002;
    // Use it when you need to retrieve only 1 item from the storage
    public static final int CREATE = 2003;
    private static final String TAG = PointsProvider.class.getSimpleName();
    /**
     * A UriMatcher instance
     */
    private static UriMatcher sUriMatcher;

    /**
     * A block that instantiates and sets static objects
     */
    static {
        /*
         * Creates and initializes the URI matcher
         */
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(PointsProvider.DataContract.AUTHORITY, "items", ITEMS);
        sUriMatcher.addURI(PointsProvider.DataContract.AUTHORITY, "item/#", ITEM);
        sUriMatcher.addURI(PointsProvider.DataContract.AUTHORITY, "create/#", CREATE);
    }

    //Reference to local database.
    protected MapsExampleDatabase mapsExampleDatabase;
    protected SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        mapsExampleDatabase = new MapsExampleDatabase(getContext());
        db = mapsExampleDatabase.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case ITEM:
                //Figure Out Table's Name
                String routeId = uri.getPathSegments().get(PointsProvider.DataContract.PATH_ITEM_ID_POSITION);
                String table = MapsExampleDatabase.PT_TABLE_NAME_PREFIX + routeId;

                return db.query(table, projection, selection,
                        selectionArgs, null, null, sortOrder, null);
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                return PointsProvider.DataContract.CONTENT_TYPE_ITEMS;
            case ITEM:
                return PointsProvider.DataContract.CONTENT_TYPE_ITEM;
            case CREATE:
                return PointsProvider.DataContract.CONTENT_TYPE_CREATE;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case ITEM:
                //Figure Out Table's Name
                String routeId = uri.getPathSegments().get(PointsProvider.DataContract.PATH_ITEM_ID_POSITION);
                String table = MapsExampleDatabase.PT_TABLE_NAME_PREFIX + routeId;

                //Insert points using the custom table
                long id = db.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                Uri newUri = Uri.withAppendedPath(PointsProvider.DataContract.SINGLE_ITEM_CONTENT_URI,
                        String.valueOf(id));
                getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
            case CREATE:
                //Figure Out Table's Name
                String routeKey = uri.getPathSegments().get(PointsProvider.DataContract.PATH_ITEM_ID_POSITION);
                String sql = String.format(MapsExampleDatabase.STATEMENT_CREATE_POINTS_TABLE, routeKey);
                Log.d(TAG, sql);
                db.execSQL(sql);
                getContext().getContentResolver().notifyChange(uri, null);
                return uri;

            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleteResult = 0;
        switch (sUriMatcher.match(uri)) {
            case CREATE: //Drop the table for the ID specified.
                String routeId = uri.getPathSegments().get(PointsProvider.DataContract.PATH_ITEM_ID_POSITION);
                if (routeId != null) {
                    db.execSQL(String.format(MapsExampleDatabase.STATEMENT_DROP_POINTS, routeId));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return 1;
                }
                return deleteResult;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new IllegalArgumentException("Unsupported operation");
    }

    /**
     * Defines the contract for this provider.
     */
    public static final class DataContract {

        public static final String SCHEME = "content://";
        public static final String AUTHORITY =
                "com.pperotti.android.mapsexample.services.points.data";
        public static final String PATH_ITEMS = "/items";
        public static final String PATH_ITEM = "/item/";
        public static final String PATH_CREATE = "/create/";

        public static final Uri CONTENT_URI_ITEMS = Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);
        public static final Uri SINGLE_ITEM_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_ITEM);
        public static final Uri SINGLE_CREATE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_CREATE);

        public static final int PATH_ITEM_ID_POSITION = 1;

        //Content Type
        public static final String CONTENT_TYPE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.pperotti.points.items";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.pperotti.points.item";
        public static final String CONTENT_TYPE_CREATE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.pperotti.points.create";

        // This class cannot be instantiated
        private DataContract() {
        }

        /**
         * Build the Uri needed to interact with a points table associated with the routeId specified.
         *
         * @param routeId The routeId associated with this download.
         * @return Uri uri object to access the points' table data.
         */
        public static Uri buildPointsCreateUri(long routeId) {
            return Uri.withAppendedPath(PointsProvider.DataContract.SINGLE_CREATE_CONTENT_URI,
                    String.valueOf(routeId));
        }

        /**
         * Build the Uri needed to interact with a points table associated with the routeId specified.
         *
         * @param routeId The routeId associated with this download.
         * @return Uri uri object to access the points' table data.
         */
        public static Uri buildPointsItemUri(long routeId) {
            return Uri.withAppendedPath(PointsProvider.DataContract.SINGLE_ITEM_CONTENT_URI,
                    String.valueOf(routeId));
        }
    }
}
