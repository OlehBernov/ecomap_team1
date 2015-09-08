package com.ecomap.ukraine.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;

import butterknife.ButterKnife;


public class AddPhotoFragment extends Fragment {

    private static AddPhotoFragment instance;

    public static AddPhotoFragment getInstance() {
        if (instance == null) {
            instance = new AddPhotoFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2, container, false);
      //  ButterKnife.inject(this, v);

        return v;
    }

}