package com.ecomap.ukraine.update.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Statistics;
import com.ecomap.ukraine.update.LoadingClient;
import com.ecomap.ukraine.update.StatisticsLoader;
import com.ecomap.ukraine.update.StatisticsResponseReceiver;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Coordinates the work of the database, data loading client and activities.
 * Provides updates of the database.
 */
public class DataManager implements DataResponseReceiver {

    /**
     * The name of the preference to retrieve.
     */
    private static final String TIME = "Time";

    private static final String TOP_10_UPDATE_TIME = "Top 10 Update time";

    private static final String STATISTICS_UPDATE_TIME = "StatisticsItem Update time";

    /**
     * Holds the Singleton global instance of DataManager.
     */
    private static DataManager instance;

    /**
     * Default period of all problems and problem details updating.
     */
    private String DEFAULT_UPDATE_PERIOD = "2";

    /**
     * Set of problem listeners.
     */
    private Collection<DataListener> problemListeners = new CopyOnWriteArrayList<>();

    /**
     * Holds the reference to LoadingClient used by DataManager.
     */
    private LoadingClient loadingClient;

    /**
     * Problem posting statistics loader.
     */
    private StatisticsResponseReceiver statisticsResponseReceiver;

    /**
     * Context of the application.
     */
    private Context context;

    /**
     * Helper in work with data base.
     */
    private DBHelper dbHelper;

    /**
     * Id of current problem
     */
    private int currentProblemId;

    /**
     * Data manager constructor.
     */
    private DataManager(final Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        loadingClient = new LoadingClient(this, context);
    }

    /**
     * Returns Singleton instance of DataManger
     */
    public static DataManager getInstance(final Context context) {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the ProblemListener to add.
     */
    public void registerDataListener(final DataListener listener) {
        problemListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of problemListeners.
     *
     * @param listener the ProblemListener to remove.
     */
    public void removeDataListener(final DataListener listener) {
        problemListeners.remove(listener);
    }

    /**
     * Send to listeners list of all problems.
     */
    public void sendAllProblems(final List<Problem> problems) {
        for (DataListener listener : problemListeners) {
            listener.onAllProblemsUpdate(problems);
        }
    }

    /**
     * Send to listeners details of concrete problem.
     */
    public void sendProblemDetails(final Details details) {
        for (DataListener listener : problemListeners) {
            listener.onProblemDetailsUpdate(details);
        }
    }

    /**
     * Sends object of top10 problems to listeners.
     *
     * @param allTop10Items object of top10 problems.
     */
    public void sendAllTop10Items(final AllTop10Items allTop10Items) {
        for (DataListener listener : problemListeners) {
            listener.onTop10Update(allTop10Items);
        }
    }

    /**
     * Sends object of statistics of problem posting to listeners.
     *
     * @param statistics object of statistics.
     */
    public void sendStatistics(final Statistics statistics) {
        for (DataListener listener : problemListeners) {
            listener.onStatisticsUpdate(statistics);
        }
    }

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    @Override
    public void setAllProblemsResponse(final List<Problem> problems) {
        if (problems != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    dbHelper.updateAllProblems(problems);
                    saveUpdateTime(TIME);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    getAllProblems();
                }
            }.execute();
        } else {
            sendAllProblems(dbHelper.getAllProblems());
        }
    }

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void setProblemDetailsResponse(final Details details) {
        if (details != null) {
            dbHelper.updateProblemDetails(details);
            getProblemDetail(details.getProblemId());
        } else {
            sendProblemDetails(dbHelper.getProblemDetails(currentProblemId));
        }
    }

    /**
     * Receive server response to the request of top 10
     * problems and send int to listeners.
     *
     * @param allTop10Items object of top 10 problem
     */
    @Override
    public void setTop10Response(AllTop10Items allTop10Items) {
        if (allTop10Items != null) {
            dbHelper.updateTop10(allTop10Items);
            saveUpdateTime(TOP_10_UPDATE_TIME);
            getTop10();
        } else {
            sendAllTop10Items(dbHelper.getAllTop10Items());
        }
    }

    /**
     * Receive server response to the request of statistics about
     * problem posting and send it to listeners.
     *
     * @param statistics object of statistics.
     */
    @Override
    public void setStatisticsResponse(Statistics statistics) {
        if (statistics != null) {
            dbHelper.updateStatistics(statistics);
            saveUpdateTime(STATISTICS_UPDATE_TIME);
            getStatistics();
        } else {
            sendStatistics(dbHelper.getStatistics());
        }
    }

    /**
     * Performs when vote successfully sent to server
     */
    @Override
    public void onVoteAdded() {
        for (DataListener listener : problemListeners) {
            listener.onVoteAdded();
        }
    }

    /**
     * Performs when comment was successfully sent to server.
     */
    @Override
    public void onCommentAdded() {
        for (DataListener listener : problemListeners) {
            listener.onCommentAdded();
        }
    }

    /**
     * This method is used to made request for more information about
     * the problem.
     * Initiates the onAllProblemsUpdate of detailed information of the problem
     * in the database, if it is missing or obsolete.
     *
     * @param problemId the id of the problem.
     */
    public void getProblemDetail(final int problemId) {
        Details details = dbHelper.getProblemDetails(problemId);
        currentProblemId = problemId;
        if (details == null) {
            loadingClient.getProblemDetail(problemId);
        } else {
            long lastUpdateTime = Long.valueOf(dbHelper.getLastUpdateTime(problemId));
            if (isUpdateTime(lastUpdateTime)) {
                loadingClient.getProblemDetail(problemId);
            } else {
                sendProblemDetails(details);
            }
        }
    }

    /**
     * This method is used to made request for all problems.
     * Initiates the onAllProblemsUpdate of brief information of the problem in the database,
     * if it is missing or obsolete.
     */
    public void getAllProblems() {
        SharedPreferences settings = context.getSharedPreferences(TIME, Context.MODE_PRIVATE);
        long lastUpdateTime = settings.getLong(TIME, 0);
        if (isUpdateTime(lastUpdateTime)) {
            loadingClient.getAllProblems();
        } else {
            List<Problem> problems = dbHelper.getAllProblems();
            if (problems == null) {
                loadingClient.getAllProblems();
            } else {
                sendAllProblems(problems);
            }
        }
    }

    /**
     * This method is used to made request for get top 10 problems.
     */
    public void getTop10() {
        SharedPreferences settings = context.getSharedPreferences(TOP_10_UPDATE_TIME, Context.MODE_PRIVATE);
        long lastUpdateTime = settings.getLong(TOP_10_UPDATE_TIME, 0);
        if (isUpdateTime(lastUpdateTime)) {
            loadingClient.getTop10();
        } else {
            AllTop10Items allTop10Items = dbHelper.getAllTop10Items();
            if (allTop10Items == null) {
                loadingClient.getTop10();
            } else {
                sendAllTop10Items(allTop10Items);
            }
        }
    }

    /**
     * Make request for statistics of problem posting.
     */
    public void getStatistics() {
        SharedPreferences settings = context.getSharedPreferences(STATISTICS_UPDATE_TIME,
                                                                  Context.MODE_PRIVATE);
        long lastUpdateTime = settings.getLong(STATISTICS_UPDATE_TIME, 0);

        statisticsResponseReceiver = new StatisticsLoader(this);
        if (isUpdateTime(lastUpdateTime)) {
            statisticsResponseReceiver.loadStatistics(loadingClient);
        } else {
            Statistics statistics = dbHelper.getStatistics();
            if (statistics == null) {
                statisticsResponseReceiver.loadStatistics(loadingClient);
            } else {
                sendStatistics(statistics);
            }
        }
    }

    /**
     * This method is used to send vote to server for current problem.
     *
     * @param problemID   id of current problem.
     * @param userID      id of user who send vote.
     * @param userName    name of user who send vote.
     * @param userSurname surname of user who send vote.
     */
    public void postVote(final String problemID, final String userID,
                         final String userName, final String userSurname) {
        loadingClient.postVote(problemID, userID, userName, userSurname);

    }

    /**
     * This method is used to send comment to server for current problem.
     *
     * @param problemID   id of current problem.
     * @param userID      id of user who send comment.
     * @param userName    name of user who send vote.
     * @param userSurname surname of user who send vote.
     * @param content     content of comment.
     */
    public void postComment(final int problemID, final String userID,
                            final String userName, final String userSurname,
                            final String content) {
        loadingClient.postComment(problemID, userID, userName, userSurname, content);
    }

    /**
     * Refresh brief information about all problems.
     */
    public void refreshAllProblem() {
        loadingClient.getAllProblems();
    }

    /**
     * Refresh details of current problem.
     *
     * @param problemId id of current problem.
     */
    public void refreshProblemDetails(final int problemId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                currentProblemId = problemId;
                loadingClient.getProblemDetail(problemId);
                return null;
            }
        }.execute();
    }

    /**
     * Remove all listeners from Data manager.
     */
    public void removeAllListeners() {
        problemListeners.clear();
    }

    /**
     * Saves time of the last database update
     * to the SharedPreferences.
     */
    private void saveUpdateTime(String preferenceKey) {
        SharedPreferences settings = context.getSharedPreferences(preferenceKey, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(preferenceKey, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * Checks the need to update.
     *
     * @param lastUpdateTime time of the last database update.
     * @return the need to update.
     */
    private boolean isUpdateTime(final long lastUpdateTime) {
        return (System.currentTimeMillis() - lastUpdateTime) >= getUpdatingPeriod();
    }

    /**
     * Returns updating period which was set in Settings.
     *
     * @return current updating period.
     */
    private long getUpdatingPeriod() {
        String updatingTime = context.getResources().getString(R.string.updating_time);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int updateTimeId = Integer.valueOf(sharedPreferences.getString(updatingTime,
                DEFAULT_UPDATE_PERIOD));
        UpdateTime up = UpdateTime.getUpdateTimeType(updateTimeId);

        return up.getTimeInMilliseconds();
    }

}