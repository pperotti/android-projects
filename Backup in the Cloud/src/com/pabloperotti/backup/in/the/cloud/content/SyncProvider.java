package com.pabloperotti.backup.in.the.cloud.content;

import com.pabloperotti.backup.in.the.cloud.MyApp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SyncProvider extends ContentProvider {

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub

		Log.i(MyApp.TAG, "SyncProvider.delete()");

		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub

		Log.i(MyApp.TAG, "SyncProvider.getType()");

		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub

		Log.i(MyApp.TAG, "SyncProvider.insert()");

		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.i(MyApp.TAG, "SyncProvider.onCreate()");

		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.i(MyApp.TAG, "SyncProvider.query()");

		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.i(MyApp.TAG, "SyncProvider.update()");

		return 0;
	}

}
