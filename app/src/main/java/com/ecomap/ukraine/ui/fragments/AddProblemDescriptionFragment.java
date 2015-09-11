package com.ecomap.ukraine.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.authentication.validator.Validator;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemForPosting;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.problemposting.manager.AddProblemListener;
import com.ecomap.ukraine.problemposting.manager.AddProblemManager;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.problemupdate.manager.ProblemListener;
import com.ecomap.ukraine.ui.activities.MainActivity;
import com.ecomap.ukraine.util.ExtraFieldNames;
import com.ecomap.ukraine.util.Keyboard;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Fragment for posting description of new problem
 */
public class AddProblemDescriptionFragment extends Fragment implements AddProblemListener,
                                                                       ProblemListener {

    private static final String PLEASE_WAIT = "Please wait...";

    /**
     * Holds the Singleton global instance of AddProblemDescriptionFragment.
     */
    private static AddProblemDescriptionFragment instance;
    private static List<Bitmap> bitmapPhotos;
    private static List<String> photoDescriptions;

    private static final String INPUT_PROBLEM_DATA = "Input problem data";
    private static final String POSTING = "Posting...";

    private AddProblemManager addProblemManager;
    private DataManager dataManager;
    private AccountManager accountManager;
    private String userName;
    private String userSurname;

    @InjectView(R.id.problemTitle) EditText problemTitle;
    @InjectView(R.id.problemDescription) EditText problemDescription;
    @InjectView(R.id.problemSolution) EditText problemSolution;
    @InjectView(R.id.spinner) Spinner spinner;

    /**
     * Returns Singleton instance of AddProblemDescriptionFragment
     */
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

    /**
     * Validation of data of new problem
     */
    public void postProblemValidation(Activity activity) {
        boolean isProblemValid = Validator.addProblemValidation(problemTitle);
        if (!isProblemValid) {
            Toast.makeText(activity, INPUT_PROBLEM_DATA, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        setChooseNameDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataManager = DataManager.getInstance(getActivity());
        addProblemManager = AddProblemManager.getInstance(getActivity());
    }

    public void successPosting(final int idOfMessage) {
        Toast.makeText(getActivity(), idOfMessage, Toast.LENGTH_LONG).show();
        dataManager.registerProblemListener(this);
        dataManager.refreshAllProblem();
    }

    /**
     * Initialize view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        accountManager = AccountManager.getInstance(v.getContext());
        userName = accountManager.getUserFromPreference().getName();
        userSurname = accountManager.getUserFromPreference().getSurname();
        addProblemManager = AddProblemManager.getInstance(getActivity());
        dataManager = DataManager.getInstance(getActivity());

        ButterKnife.inject(this, v);

        Keyboard.setOnFocusChangeListener(problemTitle);
        Keyboard.setOnFocusChangeListener(problemDescription);
        Keyboard.setOnFocusChangeListener(problemSolution);

        return v;
    }

    /**
     * Callback on destroy fragment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        addProblemManager.removeAddProblemListener(this);
    }

    /**
     * Show progress dialog when problem posting
     */
    private void showProgressDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(POSTING)
                .content(PLEASE_WAIT)
                .progress(true, 0)
                .cancelable(false)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .show();
    }

    /**
     * Represents variants of posting result
     */
    @Override
    public void onSuccessProblemPosting() {
        successPosting(R.string.problem_added_successfully);
    }

    @Override
    public void onFailedProblemPosting() {
        Toast.makeText(getActivity(), R.string.problem_post_error,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessPhotoPosting() {
        successPosting(R.string.problem_added_successfully);
    }


    @Override
    public void onFailedPhotoPosting() {
        successPosting(R.string.photo_post_error);

    }

    /**
     * Get list of all details.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void updateProblemDetails(final Details details) {
    }

    @Override
    public void updateTop10(AllTop10Items allTop10Items) {

    }

    /**
     * Get list of all problems.
     *
     * @param problems list of all problems.
     */
    @Override
    public void updateAllProblems(final List<Problem> problems) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        dataManager.removeProblemListener(this);
    }

    /**
     * Post new problem on server
     */
    private void sendProblem() {
        addProblemManager.registerAddProblemListener(this);
        String title = problemTitle.getText().toString();
        String description = problemDescription.getText().toString();

        String solution = problemSolution.getText().toString();
        double latitude = getActivity().getIntent().getDoubleExtra(ExtraFieldNames.LAT, 0);
        double longitude = getActivity().getIntent().getDoubleExtra(ExtraFieldNames.LNG, 0);
        String type = String.valueOf(spinner.getSelectedItemId() + 1);
        showProgressDialog();
        ProblemForPosting problemData = new ProblemForPosting.Builder()
                .setdefaultData()
                .setTitle(title)
                .setContent(description)
                .setProposal(solution)
                .setPosition(new LatLng(latitude, longitude))
                .setType(type)
                .setUser(new User(accountManager.getUserFromPreference().getId(),
                        userName, userSurname, "", "", "", ""))
                .setPhotos(bitmapPhotos)
                .setPhotoDescriptions(photoDescriptions)
                .build();
        addProblemManager.addProblem(problemData);

        bitmapPhotos = null;
    }

    /**
     * Sets dialog of user name selection
     */
    private void setChooseNameDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.Caution)
                .content(R.string.post_problem_anonymously)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .negativeColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .cancelable(false)
                .positiveText(R.string.yes)
                .negativeText(R.string.no).callback(
                new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        userName = "";
                        userSurname = "";
                        dialog.cancel();
                        sendProblem();
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.cancel();
                        sendProblem();
                    }
                })
                .show();
    }

}
