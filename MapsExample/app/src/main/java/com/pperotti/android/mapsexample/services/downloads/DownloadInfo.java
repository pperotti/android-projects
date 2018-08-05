package com.pperotti.android.mapsexample.services.downloads;

/**
 * Contains only information about the existing download.
 */
public class DownloadInfo {

    //The status of the download
    private int downloadStatus;

    //THe local file path
    private String localFilePath;

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String savedFilePath) {
        this.localFilePath = savedFilePath;
    }
}
