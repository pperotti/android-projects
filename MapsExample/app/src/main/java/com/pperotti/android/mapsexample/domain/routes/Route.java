package com.pperotti.android.mapsexample.domain.routes;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Handle all the properties a route can have.
 */
public class Route {

    //Identifier assign to this particular download.
    private long routeId;

    //ReferenceId as defined by the DownloadManager
    private long downloadId;

    //The URL used to start this process
    private String downloadUrl;

    //The path where the file is stored locally.
    private String localFilePath;

    //The name assigned to this download
    private String name;

    //State for the current Route
    private RouteState state;

    //The time where the download started.
    private String downloadTimestamp;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public Route() {
        state = RouteState.NOT_STARTED;
    }

    public Route createRouteId() {
        routeId = System.nanoTime();
        return this;
    }

    public long getRouteId() {
        return routeId;
    }

    public Route setRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public Route setDownloadId(long downloadId) {
        this.downloadId = downloadId;
        return this;
    }

    public String getDownloadUrl() {
        return TextUtils.isEmpty(downloadUrl) ? "" : downloadUrl;
    }

    public Route setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getLocalFilePath() {
        return TextUtils.isEmpty(localFilePath) ? "" : localFilePath;
    }

    public Route setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
        return this;
    }

    public String getName() {
        return TextUtils.isEmpty(name) ? "" : name;
    }

    public Route setName(String name) {
        this.name = name;
        return this;
    }

    public String getDownloadTimestamp() {
        return downloadTimestamp;
    }

    public Route setDownloadTimestamp(long downloadTimestamp) {
        return setDownloadTimestamp(simpleDateFormat.format(downloadTimestamp));
    }

    public Route setDownloadTimestamp(String downloadTimestamp) {
        this.downloadTimestamp = downloadTimestamp;
        return this;
    }

    public RouteState getState() {
        return state;
    }

    public Route setState(RouteState state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Route:"
                        + "\nRouteId=%d"
                        + "\nDownloadId=%d"
                        + "\nDownloadURL=%s"
                        + "\nLocalPath=%s"
                        + "\nState=%s"
                        + "\nName=%s"
                        + "\nTimestamp=%s",
                getRouteId(),
                getDownloadId(),
                getDownloadUrl(),
                getLocalFilePath(),
                getState().name(),
                getName(),
                getDownloadTimestamp()
        );
    }
}
