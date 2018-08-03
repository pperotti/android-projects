package com.pperotti.android.mapsexample.newroute;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.pperotti.android.mapsexample.R;
import com.pperotti.android.mapsexample.utils.StorageHelper;

import java.io.File;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewRouteActivityFragment extends Fragment {

    private static final String TAG = NewRouteActivity.class.getSimpleName();

    private Button newrouteDownload;
    private EditText newrouteUrl;

    public NewRouteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

                long downloadInProgress = StorageHelper.getLong(
                        getContext(),
                        StorageHelper.DOWNLOAD_IN_PROGRESS_ID);
                if (downloadInProgress != StorageHelper.DEFAULT_LONG_VALUE) {
                    Toast.makeText(getContext(), R.string.error_download_in_progress, Toast.LENGTH_LONG).show();
                //    return;
                }

                if (isValidUrl(url)) {
                    //Download File
                    long refId = enqueueFileDownload(url);
                    Log.d(TAG, String.format("downloadRefId=%d", refId));
                    StorageHelper.putLong(getContext(), StorageHelper.DOWNLOAD_IN_PROGRESS_ID, refId);

                    //TODO: Stop (We should do this via actions)
                    getActivity().finish();
                }
            }
        });
    }

    private boolean isValidUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            //TODO: Add extra validations.
            return true;
        }
        return false;
    }

    private long enqueueFileDownload(String url) {
        try {
            Uri uri = Uri.parse(url);
            String fileName = uri.getLastPathSegment();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
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
        return StorageHelper.DEFAULT_LONG_VALUE;
    }
}
