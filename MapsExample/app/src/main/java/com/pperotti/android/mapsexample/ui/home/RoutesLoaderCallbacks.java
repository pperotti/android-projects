package com.pperotti.android.mapsexample.ui.home;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

public class RoutesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RoutesLoaderCallbacks.class.getSimpleName();

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
