package com.pperotti.android.mapsexample.domain.routes;

/**
 * Define all the possible states a new trace can have.
 */
public enum RouteState {

    NOT_STARTED,

    ENQUEUED,

    DOWNLOADED,

    NOT_DOWNLOADED,

    PARSING,

    PARSED,

    PARSING_FAILED,

    PROCESSING,

    PROCESSED,

    PROCESSING_FAILED
}
