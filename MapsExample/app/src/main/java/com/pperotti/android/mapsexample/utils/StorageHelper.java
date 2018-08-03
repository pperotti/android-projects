package com.pperotti.android.mapsexample.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This helper class has the responsibility of abstracting the medium a value is persisted *
 */
public class StorageHelper {

    //Value that identifies the common shared preference storage.
    private static final String SHARED_PREFERENCE_STORAGE_NAME = "com.pperotti.android.mapsexample.spstorage";

    //Key to persist the ongoing download
    public static final String DOWNLOAD_IN_PROGRESS_ID = "download_reference_id";

    //Default value used when getLong is used
    public static final long DEFAULT_LONG_VALUE = 0L;


    /**
     * Persist a value in the common SharedPreference db.
     *
     * @param context Context object used to obtain the common's shared preference storage.
     * @param key The key use to identify ths value to store.
     * @param value The value to store.
     */
    public static void putLong(Context context, String key, long value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value).apply();
            editor.commit();
        }
    }

    /**
     * Retrieve the long value stored in shared preference.
     *
     * @param context Context object.
     * @param key The key used to search a long value.
     * @return The long value stored or DEFAULT_LONG_VALUE
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, DEFAULT_LONG_VALUE);
    }

    /**
     * Retrieve the value stored under the key specified
     *
     * @param context Context object used to obtain the common's shared preference storage.
     * @param key The key use to identify ths value to store.
     * @param defaultValue The value to store.
     *
     * @return the long stored in the shared preferences or the defaultValue specified.
     */
    public static long getLong(Context context, String key, long defaultValue) {
        if (context != null && key != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Remove Long value from storage regardless its value
     *
     * @param context Context object.
     * @param key Value for the key to remove in the storage.
     */
    public static void removeLong(Context context, String key) {
        if (context != null && key != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_STORAGE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key).apply();
            editor.commit();
        }
    }
}
