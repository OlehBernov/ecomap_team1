package com.ecomap.ukraine.posting.manager;

/**
 * Interface of classes, which want to get
 * information about posting problem.
 */
public interface AddProblemListener {

    void onSuccessProblemPosting();

    void onFailedProblemPosting();

    void onSuccessPhotoPosting();

    void onFailedPhotoPosting();

}
