package com.ecomap.ukraine.activities.addProblem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ecomap.ukraine.R;

import butterknife.ButterKnife;


public class AddPhotoFragment extends Fragment {

    private static AddPhotoFragment instance;
    private static Activity activity;
    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    };

    public static AddPhotoFragment getInstance(Activity activity) {
        if (instance == null) {
            instance = new AddPhotoFragment();
            AddPhotoFragment.activity = activity;
        }
        return instance;
    }

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