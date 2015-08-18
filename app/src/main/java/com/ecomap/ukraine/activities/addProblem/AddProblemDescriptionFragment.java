package com.ecomap.ukraine.activities.addProblem;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.addproblem.manager.AddProblemListener;
import com.ecomap.ukraine.addproblem.manager.AddProblemManager;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.validation.Validator;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Edwin on 15/02/2015.
 */
public class AddProblemDescriptionFragment extends Fragment implements LogInListener, AddProblemListener, ProblemListener {

    /**
     * Holds the Singleton global instance of AddProblemDescriptionFragment.
     */
    private static AddProblemDescriptionFragment instance;
    private static List<Bitmap> bitmapPhotos;
    private static List<String> photoDescriptions;
    @InjectView(R.id.problemTitle)
    EditText problemTitle;
    @InjectView(R.id.problemDescription)
    EditText problemDescription;
    @InjectView(R.id.problemSolution)
    EditText problemSolution;
    @InjectView(R.id.spinner)
    Spinner spinner;
    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    };
    private AddProblemManager addProblemManager;
    private DataManager dataManager;
    private AccountManager accountManager;
    private User user;

    public static AddProblemDescriptionFragment getInstance(List<Bitmap> bitmapPhotos,
                                                            List<String> descriptions) {
        if (instance == null) {
            instance = new AddProblemDescriptionFragment();
        }
        if (bitmapPhotos != null) {
            AddProblemDescriptionFragment.bitmapPhotos = bitmapPhotos;
            AddProblemDescriptionFragment.photoDescriptions = descriptions;
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);

        addProblemManager = AddProblemManager.getInstance(getActivity().getApplicationContext());
        accountManager = AccountManager.getInstance(getActivity().getApplicationContext());
        dataManager = DataManager.getInstance(getActivity().getApplicationContext());

        ButterKnife.inject(this, v);
        problemTitle.setOnFocusChangeListener(focusChangeListener);
        problemDescription.setOnFocusChangeListener(focusChangeListener);
        problemSolution.setOnFocusChangeListener(focusChangeListener);

        return v;
    }

    public void onDestroy() {
        super.onDestroy();
        accountManager.removeLogInListener(this);
        addProblemManager.removeAddProblemListener(this);

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void sendProblem() {
        boolean isProblemValid;
        isProblemValid = new Validator().addProblemValidation(problemTitle);
        if (!isProblemValid) {
            Toast.makeText(getActivity().getApplicationContext(), "Input problem data", Toast.LENGTH_LONG).show();
            return;
        }
        addProblemManager.registerAddProblemListener(this);
        String title = problemTitle.getText().toString();
        String description = problemDescription.getText().toString();

        String solution = problemSolution.getText().toString();
        String latitude = getActivity().getIntent().getDoubleExtra(ExtraFieldNames.LAT, 0) + "";
        String longitude = getActivity().getIntent().getDoubleExtra(ExtraFieldNames.LNG, 0) + "";
        String type = String.valueOf(spinner.getSelectedItemId() + 1);
        showProgresDialog();
        addProblemManager.addProblem(title, description, solution, latitude, longitude, type, String.valueOf(user.getId()),
                String.valueOf(user.getName()), String.valueOf(user.getSurname()), bitmapPhotos, photoDescriptions);
        bitmapPhotos = null;
    }

    @Override
    public void setLogInResult(final User user) {
        this.user = user;
    }


    @Override
    public void onSuccessProblemPosting() {
        successPosting(R.string.problem_added_sucessfully);
    }


    @Override
    public void onFailedProblemPosting() {
        Toast.makeText(getActivity().getApplicationContext(), R.string.problem_post_error,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessPhotoPosting() {
        successPosting(R.string.problem_added_sucessfully);
    }


    @Override
    public void onFailedPhotoPosting() {
        successPosting(R.string.photo_post_error);

    }

    @Override
    public void updateProblemDetails(final Details details) {
    }

    @Override
    public void updateAllProblems(final List<Problem> problems) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(ExtraFieldNames.USER, user);
        startActivity(intent);
        getActivity().finish();
        dataManager.removeProblemListener(this);

    }

    public void successPosting(final int idOfmessage) {
        Toast.makeText(getActivity().getApplicationContext(), idOfmessage, Toast.LENGTH_LONG).show();
        dataManager.registerProblemListener(this);
        dataManager.refreshAllProblem();
    }

    private void showProgresDialog() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Panel);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Posting...");
        progressDialog.show();
    }


}
