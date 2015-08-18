package com.ecomap.ukraine.addproblem.manager;


public interface AddProblemListener {

    void onSuccessProblemPosting();

    void onFailedProblemPosting();

    void onSuccessPhotoPosting();

    void onFailedPhotoPosting();

}
