package com.ecomap.ukraine.filter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andriy on 01.08.2015.
 */
public class FilterManager implements FilterListenersNotifier {

    private static final String FILTERS_STATE = "Filters state";

    /**
     * Holds the Singleton global instance of FilterManager.
     */
    private static FilterManager instance;
    /**
     * Set of problem listeners.
     */
    public Set<FilterListener> filterListeners = new HashSet<>();
    /**
     * Activity which callback filter-manager
     */
    private Activity activity;

    /**
     * Filter manager constructor.
     */
    private FilterManager(final Activity activity) {
        this.activity = activity;
    }

    /**
     * Returns Singleton instance of FilterManger
     */
    public static FilterManager getInstance(final Activity activity) {
        if (instance == null) {
            instance = new FilterManager(activity);
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
     * Removes the specified listener from the set of problemaListeners.
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
    public void sendFilterState (final FilterState filterState, final boolean isRendering) {
        for (FilterListener listener : filterListeners) {
            listener.updateFilterState(filterState, isRendering);
        }
    }

   


    /**
     * This method use to get  filter state for filter manager
     *
     * @param filterState this filterState gets for filter manager
     */
        public void getFilterState (final FilterState filterState, final boolean isRendering) {
            sendFilterState(filterState, isRendering);

    }

    /**
     * Gets filter state form SharedPreferences
     *
     * @return filter state
     */
    public FilterState getFilterStateFromPreference() {
        SharedPreferences settings = activity.getApplicationContext()
                .getSharedPreferences(FILTERS_STATE, Context.MODE_PRIVATE);
        String filterStateJson = settings.getString(FILTERS_STATE, null);
        FilterState filterState;
        if (filterStateJson != null) {
            try {
                filterState = new FilterStateConverter().convertToFilterState(filterStateJson);
            } catch (JSONException e) {
                filterState = null;
                Log.e("JSONException", "convert filter");
            }
        } else {
            filterState = null;
            Log.e("JSONException", "filter null");
        }
        return filterState;
    }

}
