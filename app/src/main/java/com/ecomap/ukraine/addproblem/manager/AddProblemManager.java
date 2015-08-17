package com.ecomap.ukraine.addproblem.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.ecomap.ukraine.addproblem.client.AddProblemClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class AddProblemManager implements AddProblemRequestReceiver, AddProblemNotifier {

    private static AddProblemManager instance;

    private Set<AddProblemListener> addProblemListeners = new HashSet<>();

    private AddProblemClient addProblemClient;

    private AddProblemManager(final Context context) {
        addProblemClient = new AddProblemClient(this, context);
    }

    public static AddProblemManager getInstance(final Context context) {
        if (instance == null) {
            instance = new AddProblemManager(context);
        }
        return instance;
    }

    @Override
    public void setAddProblemRequestResult(final boolean result) {
        sendAddProblemResult(result);
    }

    @Override
    public void registerAddProblemListener(final AddProblemListener listener) {
        addProblemListeners.add(listener);
    }

    @Override
    public void removeAddProblemListener(final AddProblemListener listener) {
        addProblemListeners.remove(listener);
    }

    @Override
    public void sendAddProblemResult(final boolean result) {
        for (AddProblemListener listener: addProblemListeners) {
            listener.setAddProblemResult(result);
        }
    }

    public void addProblem (final String title, final String content, final String proposal,
                            final String latitude, final String longitude, final String type,
                            final String userId, final String userName, final String userSurname,
                            final ArrayList<Bitmap> bitmaps) {

        addProblemClient.addProblemDescription(title, content, proposal, latitude, longitude,
                                               type, userId, userName, userSurname, bitmaps);
    }

}
