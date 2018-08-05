package com.pperotti.android.mapsexample.domain.routes;

public class GPXPoint {

    long latitud;

    long longitude;

    public long getLatitud() {
        return latitud;
    }

    public GPXPoint setLatitud(long latitud) {
        this.latitud = latitud;
        return this;
    }

    public long getLongitude() {
        return longitude;
    }

    public GPXPoint setLongitude(long longitude) {
        this.longitude = longitude;
        return this;
    }
}
