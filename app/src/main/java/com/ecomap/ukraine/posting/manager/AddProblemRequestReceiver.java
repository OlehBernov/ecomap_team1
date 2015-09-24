package com.ecomap.ukraine.posting.manager;

/**
 * Interface of class, which receives server responses.
 */
public interface AddProblemRequestReceiver {

    void onSuccessProblemPosting();

    void onFailedProblemPosting();

    void onSuccessPhotoPosting();

    void onFailedPhotoPosting();

}
