package com.pperotti.android.mapsexample.downloads;

/**
 * Contains only information about the downloaded file.
 */
public class DownloadInfo {

    //Reference used to perform this download
    private long downloadReference;

    //The status of the download
    private int downloadStatus;

    //THe local file path
    private String localFilePath;

    public long getDownloadReference() {
        return downloadReference;
    }

    public void setDownloadReference(long downloadReference) {
        this.downloadReference = downloadReference;
    }

    public long getDownloadStatus() {
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
