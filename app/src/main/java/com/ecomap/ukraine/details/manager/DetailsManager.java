package com.ecomap.ukraine.details.manager;

import android.content.Context;


import com.ecomap.ukraine.details.client.DetaisClient;

import java.util.HashSet;
import java.util.Set;


public class DetailsManager implements DetailsRequestReceiver, DetailsNotifier {

    private static DetailsManager instance;
    private Set<DetailsListener> detailsListeners = new HashSet<>();
    private DetaisClient detaisClient;

    private DetailsManager(final Context context) {
        detaisClient = new DetaisClient(this, context);
    }

    public static DetailsManager getInstance(final Context context) {
        if (instance == null) {
            instance = new DetailsManager(context);
        }
        return instance;
    }

    @Override
    public void onVoteAdded () {
        for (DetailsListener listener : detailsListeners) {
            listener.onVoteAdded();
        }
    }

    @Override
    public void onCommentAdded () {
        for (DetailsListener listener : detailsListeners) {
            listener.onCommentAdded();
        }
    }

    @Override
    public void registerDetailsListener(DetailsListener listener) {
        detailsListeners.add(listener);
    }

    @Override
    public void removeAllDetailsListener() {
        detailsListeners.clear();
    }

    @Override
    public void removeDetailsListener(DetailsListener listener) {
        detailsListeners.remove(listener);
    }

    public void postVote (final String problemID, final String userID,
                          final String userName, final String userSurname) {
        detaisClient.postVote(problemID, userID, userName, userSurname);

    }

    public void postComment (final int problemID, final String userID,
                             final String userName, final String userSurname,
                             final String content) {
        detaisClient.postComment(problemID, userID, userName, userSurname, content);
    }


}
