package com.pperotti.android.mapsexample.ui.newroute;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pperotti.android.mapsexample.R;
import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.domain.routes.RouteState;
import com.pperotti.android.mapsexample.services.routes.RouteManager;

import java.io.File;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewRouteActivityFragment extends Fragment {

    private static final String TAG = NewRouteActivityFragment.class.getSimpleName();
    private static final long RESULT_PROBLEM_DURING_ENQUEUE = -1;
    private Button newrouteDownload;
    private EditText newrouteUrl;
    private RouteManager routeManager;

    public NewRouteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        routeManager = new RouteManager(getContext());
        return inflater.inflate(R.layout.fragment_new_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newrouteUrl = view.findViewById(R.id.newroute_url);
        newrouteDownload = view.findViewById(R.id.newroute_download_button);
        newrouteDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = newrouteUrl.getText().toString();

                Uri uri = Uri.parse(url);
                String fileName = uri.getLastPathSegment();

                //Download File
                long refId = enqueueFileDownload(uri, fileName);

                //Setup Calendar to format timestamp
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());


                //Create the object to persist the fact that we enqueued the new Route
                Route newRoute = new Route()
                        .createRouteId()
                        .setDownloadId(refId)
                        .setDownloadUrl(url)
                        .setDownloadTimestamp(c.getTimeInMillis())
                        .setState(RouteState.ENQUEUED)
                        .setName(fileName);

                Log.d(TAG, "NEW DOWNLOAD..." + newRoute.toString());

                //Persist the new Route
                routeManager.add(newRoute);

                //TODO: Stop (We should do this via actions)
                getActivity().finish();
            }
        });
    }

    private long enqueueFileDownload(Uri url, String fileName) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(url);
            request.setAllowedOverRoaming(false);
            request.setTitle(getString(R.string.newroute_download_in_progress_title));
            request.setDescription(getString(R.string.newroute_download_in_progress_text, fileName));
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    File.separator
                            + getContext().getPackageName()
                            + File.separator + "gpx"
                            + File.separator + fileName);

            DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            return dm.enqueue(request);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return RESULT_PROBLEM_DURING_ENQUEUE;
    }
}
