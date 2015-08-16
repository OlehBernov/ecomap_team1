package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.ecomap.ukraine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab2 extends Fragment {

    private static Tab2 instance;
    private static Activity activity;

    public static Tab2 getInstance(Activity activity) {
        if (instance == null) {
            instance = new Tab2();
            Tab2.activity = activity;
        }
        return instance;
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2, container, false);
        ButterKnife.inject(this, v);

        return v;
    }

    public void setOnFocusChangeListener(EditText editText) {
        editText.setOnFocusChangeListener(focusChangeListener);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}