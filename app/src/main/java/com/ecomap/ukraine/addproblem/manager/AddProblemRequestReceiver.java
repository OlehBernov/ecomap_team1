package com.ecomap.ukraine.addproblem.manager;

public interface AddProblemRequestReceiver {

    void onSuccesProblemPosting();

    void onFailedProblemPosting();

    void onSuccesPhotoPosting();

    void onFailedPhotoPosting();

}
