package com.pperotti.android.mapsexample.services.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapsExampleDatabase extends SQLiteOpenHelper {

    private static final String TAG = MapsExampleDatabase.class.getSimpleName();

    //Route Table
    public static final String RT_TABLE_NAME = "routes";
    public static final String RT_COLUMN_ROUTE_ID = "routeId";
    public static final String RT_COLUMN_DOWNLOAD_ID = "downloadId";
    public static final String RT_COLUMN_DOWNLOAD_URL = "downloadUrl";
    public static final String RT_COLUMN_LOCAL_PATH = "localPath";
    public static final String RT_COLUMN_STATE = "state";
    public static final String RT_COLUMN_NAME = "name";
    public static final String RT_COLUMN_TIMESTAMP = "timestamp";

    //Metrics Table
    public static final String MT_TABLE_NAME = "metrics";
    public static final String MT_COLUMN_ROUTE_ID = RT_COLUMN_ROUTE_ID;
    public static final String MT_COLUMN_UNIT = "metricUnit";
    public static final String MT_COLUMN_VALUE = "metricValue";

    //Points Table
    public static final String PT_TABLE_NAME_PREFIX = "points";
    public static final String PT_COLUMN_ROUTE_ID = RT_COLUMN_ROUTE_ID;
    public static final String PT_COLUMN_LATITUDE = "latitude";
    public static final String PT_COLUMN_LONGITUDE = "longitude";

    //Database Name
    private static final String DATABASE_NAME = "mapsexample.sqlite";

    //Database Version
    private static final int CURRENT_DATABASE_VERSION = 1;

    //Statement CREATE 'routes'
    private static final String STATEMENT_CREATE_ROUTES = "CREATE TABLE "
            + RT_TABLE_NAME
            + " ("
            + RT_COLUMN_ROUTE_ID + " LONG PRIMARY KEY, "
            + RT_COLUMN_DOWNLOAD_ID + " LONG,"
            + RT_COLUMN_DOWNLOAD_URL + " TEXT,"
            + RT_COLUMN_LOCAL_PATH + " TEXT,"
            + RT_COLUMN_STATE + " TEXT,"
            + RT_COLUMN_NAME + " TEXT,"
            + RT_COLUMN_TIMESTAMP + " DATETIME"
            + ")";

    //Statement CREATE 'metrics'
    private static final String STATEMENT_CREATE_METRICS = "CREATE TABLE "
            + MT_TABLE_NAME
            + " ("
            + MT_COLUMN_ROUTE_ID + " LONG PRIMARY KEY, "
            + MT_COLUMN_UNIT + " TEXT,"
            + MT_COLUMN_VALUE + " TEXT"
            + ")";

    //Statement CREATE 'points-*'
    public static final String STATEMENT_CREATE_POINTS_TABLE = "CREATE TABLE "
            + PT_TABLE_NAME_PREFIX
            + "%s ("
            + PT_COLUMN_LATITUDE + " TEXT,"
            + PT_COLUMN_LONGITUDE + " TEXT"
            + ")";

    //Statement DROP 'routes'
    private static final String STATEMENT_DROP_ROUTES = "DROP TABLE IF EXISTS routes";

    //Statement DROP 'metrics'
    private static final String STATEMENT_DROP_METRICS = "DROP TABLE IF EXISTS metrics";

    //Statement DROP 'points-'
    public static final String STATEMENT_DROP_POINTS = "DROP TABLE IF EXISTS points%s";

    MapsExampleDatabase mapsExampleDatabase;

    public MapsExampleDatabase(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(TAG, "onCreate");

        //Drop all
        sqLiteDatabase.execSQL(STATEMENT_DROP_ROUTES);
        sqLiteDatabase.execSQL(STATEMENT_DROP_METRICS);

        //Create databases
        sqLiteDatabase.execSQL(STATEMENT_CREATE_ROUTES);
        sqLiteDatabase.execSQL(STATEMENT_CREATE_METRICS);

        //TODO: Add indexes
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Do nothing for the moment since we are in version 1.
        Log.d(TAG, "onUpgrade");
    }
}
