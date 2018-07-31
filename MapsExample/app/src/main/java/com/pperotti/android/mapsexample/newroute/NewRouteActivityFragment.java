package com.pperotti.android.mapsexample.newroute;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pperotti.android.mapsexample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewRouteActivityFragment extends Fragment {

    public NewRouteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_route, container, false);
    }
}
