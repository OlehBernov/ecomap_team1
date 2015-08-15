package com.ecomap.ukraine.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.ecomap.ukraine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab2 extends Fragment {

    private static Tab2 instance;
    private static Context context;

    public static Tab2 setContext(Context context) {
        if (instance == null) {
            Tab2.context = context;
            instance = new Tab2();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        ButterKnife.inject(this, v);
        ScrollView scrollView = (ScrollView) v.findViewById(R.id.scrollView2);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               hideKeyboard(v);
               return true;
           }
       });

        return v;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}