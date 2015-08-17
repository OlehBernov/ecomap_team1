package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;


import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.addproblem.manager.AddProblemListener;
import com.ecomap.ukraine.addproblem.manager.AddProblemManager;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.validation.Validator;


import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.InjectView;


public class Tab1 extends Fragment implements LogInListener, AddProblemListener, ProblemListener {

    /**
     * Holds the Singleton global instance of Tab1.
     */
    private static Tab1 instance;

    private  String USER_ID = "";

    private  String USER_NAME = "";

    private  String USER_SURNAME = "";

    private AddProblemManager addProblemManager;

    DataManager dataManager;

    AccountManager accountManager;

    private static ArrayList<Bitmap> bitmapPhotos;

    private  Intent intent;

    private User user;



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

    public static Tab1 getInstance(ArrayList<Bitmap> bitmapPhotos) {
        if (instance == null) {
            instance = new Tab1();
        }
        if(bitmapPhotos != null)
        Tab1.bitmapPhotos = bitmapPhotos;
        return instance;
    }

    public Tab1 () {
     super();
        this.USER_ID = "";
        this.USER_NAME = "";
        this.USER_SURNAME = "";


    }

    public void onDestroy() {
        super.onDestroy();
        accountManager.removeLogInListener(this);
        addProblemManager.removeAddProblemListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_1,container,false);

        addProblemManager = AddProblemManager.getInstance(getActivity().getApplicationContext());
        accountManager = AccountManager.getInstance(getActivity().getApplicationContext());
        dataManager = DataManager.getInstance(getActivity().getApplicationContext());

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
        addProblemManager.registerAddProblemListener(this);
        String title = problemTitle.getText().toString();
        String description = problemDescription.getText().toString();

        String solution = problemSolution.getText().toString();
        String latitude = getActivity().getIntent().getDoubleExtra("Lat", 0) + "";
        String longitude = getActivity().getIntent().getDoubleExtra("Lng", 0) + "";
        String type = String.valueOf(spinner.getSelectedItemId() + 1);
        showProgresDialog();
//        addProblemManager.addProblem(title, description, solution, latitude, longitude, type, USER_ID,
//                USER_NAME, USER_SURNAME, bitmapPhotos);
//        bitmapPhotos = null;
        }

    @Override
   public void setLogInResult(final User user) {
        /*USER_ID = String.valueOf(user.getId());
        USER_NAME = String.valueOf(user.getName());
        USER_SURNAME = String.valueOf(user.getSurname());*/
        this.user = user;

    }

    @Override
    public void setAddProblemResult(boolean result) {

        if(result == true) {
            dataManager.registerProblemListener(this);
            dataManager.refreshAllProblem();

        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Connection error", Toast.LENGTH_LONG) .show();
            sendProblemButton.setEnabled(true);
        }
    }

    private void showProgresDialog() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Panel);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        sendProblemButton.setEnabled(false);
    }

    @Override
    public void updateProblemDetails(final Details details) {

    }

    @Override
    public void updateAllProblems(final List<Problem> problems) {
        Toast.makeText(getActivity().getApplicationContext(), "Problem added sucessfully", Toast.LENGTH_LONG) .show();
        intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
        getActivity().finish();
        dataManager.removeProblemListener(this);
    }





}
