package com.ecomap.ukraine.addproblem.manager;

/**
 * Interface of class, which receives server responses.
 */
public interface AddProblemRequestReceiver {

    void onSuccesProblemPosting();

    void onFailedProblemPosting();

    void onSuccesPhotoPosting();

    void onFailedPhotoPosting();

}
