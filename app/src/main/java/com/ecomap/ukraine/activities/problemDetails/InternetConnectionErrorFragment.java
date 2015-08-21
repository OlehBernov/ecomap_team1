package com.ecomap.ukraine.activities.problemDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;

public class InternetConnectionErrorFragment extends android.support.v4.app.Fragment {

    public static InternetConnectionErrorFragment newInstance() {
        return new InternetConnectionErrorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.internet_connection_error_fragment, container, false);
    }

}
