package com.ecomap.ukraine.problemdetails.manager;

import android.content.Context;

import com.ecomap.ukraine.problemdetails.sync.DetailsClient;

import java.util.HashSet;
import java.util.Set;


public class DetailsManager implements DetailsRequestReceiver, DetailsNotifier {

    private static DetailsManager instance;
    private Set<DetailsListener> detailsListeners = new HashSet<>();
    private DetailsClient detailsClient;

    private DetailsManager(final Context context) {
        detailsClient = new DetailsClient(this, context);
    }

    public static DetailsManager getInstance(final Context context) {
        if (instance == null) {
            instance = new DetailsManager(context);
        }
        return instance;
    }

    @Override
    public void onVoteAdded() {
        for (DetailsListener listener : detailsListeners) {
            listener.onVoteAdded();
        }
    }

    @Override
    public void onCommentAdded() {
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

    public void postVote(final String problemID, final String userID,
                         final String userName, final String userSurname) {
        detailsClient.postVote(problemID, userID, userName, userSurname);

    }

    public void postComment(final int problemID, final String userID,
                            final String userName, final String userSurname,
                            final String content) {
        detailsClient.postComment(problemID, userID, userName, userSurname, content);
    }

}
