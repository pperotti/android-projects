package com.pperotti.android.mapsexample.services.routes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pperotti.android.mapsexample.services.database.MapsExampleDatabase;

/**
 * Expose the Route table data to the application.
 */
public class RouteProvider extends ContentProvider {

    // Use it when you need all the items available in the storage
    public static final int ITEMS = 1;
    // Use it when you need to retrieve only 1 item from the storage
    public static final int ITEM = 2;
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
        sUriMatcher.addURI(DataContract.AUTHORITY, "items", ITEMS);
        sUriMatcher.addURI(DataContract.AUTHORITY, "items/*", ITEM);
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
            case ITEMS:
                return db.query(MapsExampleDatabase.RT_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder, null);
            case ITEM:
                String key = uri.getPathSegments().get(DataContract.PATH_ITEM_ID_POSITION);
                if (key != null) {
                    String itemSelection = MapsExampleDatabase.RT_COLUMN_ROUTE_ID + " = ?";
                    String[] itemSelectionArgs = {key};
                    return db.query(MapsExampleDatabase.RT_TABLE_NAME, projection, itemSelection,
                            itemSelectionArgs, null, null, null, null);
                } else {
                    throw new IllegalArgumentException("Bad URI " + uri);
                }

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                return DataContract.CONTENT_TYPE_ITEMS;
            case ITEM:
                return DataContract.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                long id = db.insertOrThrow(MapsExampleDatabase.RT_TABLE_NAME, null, contentValues);
                Uri newUri = Uri.withAppendedPath(DataContract.SINGLE_ITEM_CONTENT_URI,
                        String.valueOf(id));
                getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                //Delete 1 or more items in the table depending on the selection
                int ret = db.delete(MapsExampleDatabase.RT_TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return ret;
            case ITEM:
                //Delete the item identified by the KEY
                String key = uri.getPathSegments().get(DataContract.PATH_ITEM_ID_POSITION);
                if (key != null) {
                    String where = MapsExampleDatabase.RT_COLUMN_ROUTE_ID + "=?";
                    String[] whereArgs = {key};
                    int id = db.delete(MapsExampleDatabase.RT_TABLE_NAME, where, whereArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return id;
                } else {
                    return 0;
                }
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case ITEMS:
                int affectedRows = db.updateWithOnConflict(MapsExampleDatabase.RT_TABLE_NAME, contentValues, selection, selectionArgs, SQLiteDatabase.CONFLICT_IGNORE);
                getContext().getContentResolver().notifyChange(uri, null);
                return affectedRows;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    /**
     * Defines the contract for this provider.
     */
    public static final class DataContract {

        public static final String SCHEME = "content://";
        public static final String AUTHORITY = "com.pperotti.android.mapsexample.services.routes.data";
        public static final String PATH_ITEMS = "/items";
        public static final String PATH_ITEM = "/items/";
        public static final Uri CONTENT_URI =
                Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);
        public static final Uri SINGLE_ITEM_CONTENT_URI =
                Uri.parse(SCHEME + AUTHORITY + PATH_ITEM);

        public static final int PATH_ITEM_ID_POSITION = 1;

        //Content Type
        public static final String CONTENT_TYPE_ITEMS = "vnd.android.cursor.dir/vnd.google.item";
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.google.item";

        // This class cannot be instantiated
        private DataContract() {
        }
    }
}
