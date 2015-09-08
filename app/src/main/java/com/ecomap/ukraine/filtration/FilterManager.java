package com.ecomap.ukraine.filtration;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ecomap.ukraine.util.ExtraFieldNames;

import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;


public class FilterManager implements FilterListenersNotifier {

    /**
     * Holds the Singleton global instance of FilterManager.
     */
    private static FilterManager instance;

    protected final String TAG = getClass().getSimpleName();

    /**
     * Set of problem listeners.
     */
    private Set<FilterListener> filterListeners = new HashSet<>();

    /**
     * Activity which callback filter-manager
     */
    private Context context;

    /**
     * The latest version of filter settings.
     */
    private FilterState currentFilterState;

    /**
     * Filter manager constructor.
     */
    private FilterManager(final Context context) {
        this.context = context;
    }

    /**
     * Returns Singleton instance of FilterManger
     */
    public static FilterManager getInstance(final Context context) {
        if (instance == null) {
            instance = new FilterManager(context);
        }
        return instance;
    }

    /**
     * Adds the specified listener to the set of filterListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the FilterListener to add.
     */
    public void registerFilterListener(final FilterListener listener) {
        filterListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of problemListeners.
     *
     * @param listener the FilterListener to remove.
     */
    public void removeFilterListener(final FilterListener listener) {
        filterListeners.remove(listener);
    }

    /**
     * This method use to send to listeners filter state
     *
     * @param filterState this filterState sends to all listeners
     */
    @Override
    public void sendFilterState(final FilterState filterState) {
        currentFilterState = filterState;
        for (FilterListener listener : filterListeners) {
            listener.updateFilterState(filterState);
        }
    }

    public void setRenderer() {
        for (FilterListener listener : filterListeners) {
            listener.setRenderer();
        }
    }

    /**
     * This method use to get  filter state for filter manager
     *
     * @param filterState this filterState gets for filter manager
     */
    public void updateFilterState(final FilterState filterState) {
        sendFilterState(filterState);
    }

    /**
     * Gets filter state form SharedPreferences
     *
     * @return filter state
     */
    public FilterState getFilterStateFromPreference() {
        SharedPreferences settings = context
                                     .getSharedPreferences(ExtraFieldNames.FILTERS_STATE, Context.MODE_PRIVATE);
        String filterStateJson = settings.getString(ExtraFieldNames.FILTERS_STATE, null);
        FilterState filterState;
        if (filterStateJson != null) {
            try {
                filterState = FilterStateConverter.convertToFilterState(filterStateJson);
            } catch (JSONException e) {
                filterState = null;
                Log.e(TAG, "convert filter");
            }
        } else {
            filterState = null;
            Log.e(TAG, "filter null");
        }
        return filterState;
    }

    public FilterState getCurrentFilterState() {
        if (currentFilterState != null) {
            return currentFilterState;
        } else {
            return getFilterStateFromPreference();
        }
    }

}
