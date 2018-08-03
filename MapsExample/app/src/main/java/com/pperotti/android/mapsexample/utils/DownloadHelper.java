package com.pperotti.android.mapsexample.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;

import com.pperotti.android.mapsexample.downloads.DownloadInfo;

/**
 * Download helper activity to retrieve status and file of the downloaded file.
 */
public class DownloadHelper {

    public static final DownloadInfo getFileInfoFromReference(Context context, long reference) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DownloadManager.class);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(reference);
        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();

        int statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(statusColumnIndex);

        int localFilePathIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String localFilePath = cursor.getString(localFilePathIndex);

        cursor.close();

        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadReference(reference);
        downloadInfo.setDownloadStatus(status);
        downloadInfo.setLocalFilePath(localFilePath);

        return downloadInfo;
    }




}
