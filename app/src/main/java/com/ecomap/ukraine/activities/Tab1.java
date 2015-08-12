package com.ecomap.ukraine.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.validation.Validator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab1 extends Fragment {

    /**
     * Holds the Singleton global instance of Tab1.
     */
    private static Tab1 instance;

    @InjectView(R.id.problemTitle) EditText problemTitle;
    @InjectView(R.id.problemDescription) EditText problemDescription;
    @InjectView(R.id.problemSolution) EditText problemSolution;
    @InjectView(R.id.spinner) Spinner spinner;
    @InjectView(R.id.btn_send_problem) Button sendProblemButton;

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                hideKeyboard(v);
            }
        }
    };

    public static Tab1 getInstance() {
        if (instance == null) {
            instance = new Tab1();
        }
        return instance;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_1,container,false);
        ButterKnife.inject(this, v);
        problemTitle.setOnFocusChangeListener(focusChangeListener);
        problemDescription.setOnFocusChangeListener(focusChangeListener);
        problemSolution.setOnFocusChangeListener(focusChangeListener);
        sendProblemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProblem();
            }
        });

        return v;
    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void sendProblem() {
        boolean isProblemValid;
        isProblemValid = new Validator().addProblemValidation(problemTitle);
        if (!isProblemValid) {
            return;
        }
        }


}
