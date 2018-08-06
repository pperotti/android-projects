package com.pperotti.android.mapsexample.services.downloads;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

/**
 * Download helper activity to retrieve status and file of the downloaded file.
 */
public class DownloadHelper {

    public static final DownloadInfo getFileInfoFromReference(Context context, long reference) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(reference);
        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();

        int statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(statusColumnIndex);

        cursor.close();

        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadStatus(status);

        return downloadInfo;
    }
}
