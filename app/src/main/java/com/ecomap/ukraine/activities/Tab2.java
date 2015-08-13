package com.ecomap.ukraine.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ecomap.ukraine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab2 extends Fragment {

    private static Tab2 instance;

    public static Tab2 getInstance(Context context) {
        if (instance == null) {
            instance = new Tab2();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        ButterKnife.inject(this, v);

        return v;
    }

}