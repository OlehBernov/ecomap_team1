package com.ecomap.ukraine.problemposting.manager;

/**
 * Interface of class, which receives server responses.
 */
public interface AddProblemRequestReceiver {

    void onSuccesProblemPosting();

    void onFailedProblemPosting();

    void onSuccesPhotoPosting();

    void onFailedPhotoPosting();

}
