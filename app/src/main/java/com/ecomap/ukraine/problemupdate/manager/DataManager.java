package com.ecomap.ukraine.problemupdate.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.problemupdate.sync.LoadingClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Coordinates the work of the database, data loading client and activities.
 * Provides updates of the database.
 */
public class DataManager implements ProblemListenersNotifier,
        ProblemRequestReceiver {

    /**
     * The name of the preference to retrieve.
     */
    private static final String TIME = "Time";

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
    private Collection<ProblemListener> problemListeners = new CopyOnWriteArrayList<>();

    /**
     * Holds the reference to LoadingClient used by DataManager.
     */
    private LoadingClient loadingClient;

    /**
     * Context of the application.
     */
    private Context context;

    /**
     * Helper in work with data base.
     */
    private DBHelper dbHelper;

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
            instance = new DataManager(context);
        }
        return instance;
    }

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the ProblemListener to add.
     */
    public void registerProblemListener(final ProblemListener listener) {
        problemListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of problemListeners.
     *
     * @param listener the ProblemListener to remove.
     */
    public void removeProblemListener(final ProblemListener listener) {
        problemListeners.remove(listener);
    }

    /**
     * Send to listeners list of all problems.
     */
    @Override
    public void sendAllProblems(final List<Problem> problems) {
        for (ProblemListener listener : problemListeners) {
            listener.updateAllProblems(problems);
        }
    }

    /**
     * Send to listeners details of concrete problem.
     */
    @Override
    public void sendProblemDetails(final Details details) {
        for (ProblemListener listener : problemListeners) {
            listener.updateProblemDetails(details);
        }
    }

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    @Override
    public void setAllProblemsRequestResult(final List<Problem> problems) {
        if (problems != null) {
            dbHelper.updateAllProblems(problems);
            saveUpdateTime();
            getAllProblems();
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
    public void setProblemDetailsRequestResult(final Details details) {
        if (details != null) {
            dbHelper.updateProblemDetails(details);
            getProblemDetail(details.getProblemId());
        } else {
            sendProblemDetails(dbHelper.getProblemDetails(currentProblemId));
        }
    }

    /**
     * This method is used to made request for more information about
     * the problem.
     * Initiates the updateAllProblems of detailed information of the problem
     * in the database, if it is missing or obsolete.
     *
     * @param problemId the id of the problem.
     */
    public void getProblemDetail(final int problemId) {
        Details details = dbHelper.getProblemDetails(problemId);
        if (details == null) {
            loadingClient.getProblemDetail(problemId);
        } else {
            long lastUpdateTime = Long.valueOf(dbHelper.getLastUpdateTime(problemId));
            if (isUpdateTime(lastUpdateTime)) {
                currentProblemId = problemId;
                loadingClient.getProblemDetail(problemId);
            } else {
                sendProblemDetails(details);
            }
        }
    }

    /**
     * This method is used to made request for all problems.
     * Initiates the updateAllProblems of brief information of the problem in the database,
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

    public void refreshAllProblem() {
        loadingClient.getAllProblems();
    }

    //TODO: add to navigation
    public void refreshAllProblems() {
        SharedPreferences settings = context.getSharedPreferences(TIME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(TIME, 0);
        editor.apply();
        getAllProblems();
    }

    public void refreshProblemDetails(int problemId) {
        currentProblemId = problemId;
        loadingClient.getProblemDetail(problemId);
    }

    /**
     * Saves time of the last database update
     * to the SharedPreferences.
     */
    private void saveUpdateTime() {
        SharedPreferences settings = context.getSharedPreferences(TIME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(TIME, System.currentTimeMillis());
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

    /**
     * Possible modes of data updating.
     */
    public enum UpdateTime {

        /**
         * Updating performs when application starts.
         */
        EVERY_TIME(0),

        /**
         * Updating performs once a day.
         */
        ONCE_A_DAY(1),

        /**
         * Updating performs once a week.
         */
        ONCE_A_WEEK(2),

        /**
         * Updating performs once a month.
         */
        ONCE_A_MONTH(3);

        /**
         * Time in milliseconds for updating each time when application starts.
         */
        private static final int UPDATE = 20_000;

        /**
         * Time in milliseconds for updating once a day.
         */
        private static final int DAY_IN_MILLISECONDS = 86_400_000;

        /**
         * Time in milliseconds for updating once a week.
         */
        private static final int WEEK_IN_MILLISECONDS = 604_800_000;

        /**
         * Time in milliseconds for updating once a month.
         */
        private static final long MONTH_IN_MILLISECONDS = 26_297_438_30L;

        private int id;

        /**
         * Constructor of updating type mode.
         *
         * @param id id of the updating time mode.
         */
        UpdateTime(int id) {
            this.id = id;
        }

        /**
         * Return type of updating time according to problem type id.
         *
         * @param id update time id.
         * @return update time type.
         */
        public static UpdateTime getUpdateTimeType(int id) {
            for (UpdateTime type : UpdateTime.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return ONCE_A_WEEK;
        }

        /**
         * Returns id of the current problem.
         *
         * @return id of the problem.
         */
        public int getId() {
            return id;
        }

        /**
         * Transforms updating time mode id to time in milliseconds for this mode.
         *
         * @return time in milliseconds.
         */
        public long getTimeInMilliseconds() {
            switch (id) {
                case 0:
                    return UPDATE;
                case 1:
                    return DAY_IN_MILLISECONDS;
                case 2:
                    return WEEK_IN_MILLISECONDS;
                case 3:
                    return MONTH_IN_MILLISECONDS;
                default:
                    return WEEK_IN_MILLISECONDS;
            }
        }

    }
}