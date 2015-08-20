package com.ecomap.ukraine.addproblem.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.ecomap.ukraine.addproblem.client.AddProblemClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Coordinates the work of the data posting client and activities.
 */
public class AddProblemManager implements AddProblemRequestReceiver, AddProblemNotifier {

    private static AddProblemManager instance;

    private Set<AddProblemListener> addProblemListeners = new HashSet<>();

    private AddProblemClient addProblemClient;

    /**
     * Constructor
     *
     * @param context application context
     */
    private AddProblemManager(final Context context) {
        addProblemClient = new AddProblemClient(this, context);
    }
    /**
     * Returns Singleton instance of DataManger
     */
    public static AddProblemManager getInstance(final Context context) {
        if (instance == null) {
            instance = new AddProblemManager(context);
        }
        return instance;
    }

    /**
     * Post new problem on server
     */
    public void addProblem(final String title, final String content, final String proposal,
                           final String latitude, final String longitude, final String type,
                           final String userId, final String userName, final String userSurname,
                           final List<Bitmap> bitmaps, final List<String> photoDescriptions) {

        addProblemClient.addProblemDescription(title, content, proposal, latitude, longitude,
                type, userId, userName, userSurname, bitmaps, photoDescriptions);
    }

    /**
     * Represents variants of posting result
     */
    @Override
    public void onSuccesProblemPosting() {
        for (AddProblemListener listener : addProblemListeners) {
            listener.onSuccessProblemPosting();
        }
    }

    @Override
    public void onFailedProblemPosting() {
        for (AddProblemListener listener : addProblemListeners) {
            listener.onFailedProblemPosting();
        }
    }

    @Override
    public void onSuccesPhotoPosting() {
        for (AddProblemListener listener : addProblemListeners) {
            listener.onSuccessPhotoPosting();
        }
    }

    @Override
    public void onFailedPhotoPosting() {
        for (AddProblemListener listener : addProblemListeners) {
            listener.onFailedPhotoPosting();
        }
    }

    /**
     * Adds the specified listener to the set of addProblemListeners. If it is already
     * registered, it is not added a second time.
     */
    @Override
    public void registerAddProblemListener(final AddProblemListener listener) {
        addProblemListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of addProblemListeners.
     */
    @Override
    public void removeAddProblemListener(final AddProblemListener listener) {
        addProblemListeners.remove(listener);
    }

}
