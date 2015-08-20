package com.ecomap.ukraine.activities.addProblem;


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
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.activities.Keyboard;
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
 * Fragment for posting description of new problem
 */
public class AddProblemDescriptionFragment extends Fragment implements LogInListener, AddProblemListener, ProblemListener {

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
    private User user;
    private String USER_NAME;
    private String USER_SURNAME;
    private MaterialDialog progressDialog;


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
    public void postProblemValidation () {
        boolean isProblemValid;
        isProblemValid = new Validator().addProblemValidation(problemTitle);
        if (!isProblemValid) {
            Toast.makeText(getActivity()
                           .getApplicationContext(), INPUT_PROBLEM_DATA, Toast.LENGTH_LONG)
                           .show();
            return;
        }
        setChooseNameDialog();
    }

   
    /**
     * Calls when posting are successfull
     * @param idOfmessage
     */
    public void successPosting(final int idOfmessage) {
        Toast.makeText(getActivity().getApplicationContext(), idOfmessage, Toast.LENGTH_LONG).show();
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

        addProblemManager = AddProblemManager.getInstance(getActivity().getApplicationContext());
        accountManager = AccountManager.getInstance(getActivity().getApplicationContext());
        dataManager = DataManager.getInstance(getActivity().getApplicationContext());

        ButterKnife.inject(this, v);

        Keyboard keyboard = new Keyboard(getActivity());
        keyboard.setOnFocusChangeListener(problemTitle);
        keyboard.setOnFocusChangeListener(problemDescription);
        keyboard.setOnFocusChangeListener(problemSolution);

        return v;
    }

    /**
     * Callback on destroy fragment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        accountManager.removeLogInListener(this);
        addProblemManager.removeAddProblemListener(this);
    }


    /**
     * Show progres dialog when problem posting
     */
    private void showProgressDialog() {
        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(POSTING)
                .content(PLEASE_WAIT)
                .progress(true, 0)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .show();
    }

    /**
     * Set result of authorization
     * @param user authorize user
     */
    @Override
    public void setLogInResult(final User user) {
        if(user != null) {
            this.user = user;
            USER_NAME = user.getName();
            USER_SURNAME = user.getSurname();
        }
    }

    /**
     * Represents variants of posting result
     */
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

    /**
     * Get list of all details.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void updateProblemDetails(final Details details) {
    }

    /**
     * Get list of all problems.
     *
     * @param problems list of all problems.
     */
    @Override
    public void updateAllProblems(final List<Problem> problems) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(ExtraFieldNames.USER, user);
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
        String latitude = getActivity().getIntent().getDoubleExtra(ExtraFieldNames.LAT, 0) + "";
        String longitude = getActivity().getIntent().getDoubleExtra(ExtraFieldNames.LNG, 0) + "";
        String type = String.valueOf(spinner.getSelectedItemId() + 1);
        showProgressDialog();
        addProblemManager.addProblem(title, description, solution, latitude,
                longitude, type, String.valueOf(user.getId()),
                USER_NAME, USER_SURNAME, bitmapPhotos, photoDescriptions);
        bitmapPhotos = null;
    }



    /**
     * Set dialog of choise user name
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
                        USER_NAME = "";
                        USER_SURNAME = "";
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
