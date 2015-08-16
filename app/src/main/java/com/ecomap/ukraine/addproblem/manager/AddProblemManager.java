package com.ecomap.ukraine.addproblem.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.ecomap.ukraine.account.client.LogInClient;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.addproblem.client.AddProblemClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andriy on 12.08.2015.
 */
public class AddProblemManager implements AddProblemRequestReceiver, AddProblemNotifier {

    private static AddProblemManager instance;

    private Set<AddProblemListener> addProblemListeners = new HashSet<>();

    private AddProblemClient addProblemClient;

    private Context context;


    private AddProblemManager(final Context context) {
        this.context = context;
        addProblemClient = new AddProblemClient(this, context);
    }

    public static AddProblemManager getInstance(final Context context) {
        if (instance == null) {
            instance = new AddProblemManager(context);
        }
        return instance;
    }

    @Override
    public void setAddProblemRequestResult(boolean result) {
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
    public void sendAddProblemResult(boolean result) {
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
