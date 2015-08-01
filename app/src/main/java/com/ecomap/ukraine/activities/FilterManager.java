package com.ecomap.ukraine.activities;

import com.ecomap.ukraine.filter.FilterState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andriy on 01.08.2015.
 */
public class FilterManager implements FilterListenersNotifier {

    /**
     * Holds the Singleton global instance of FilterManager.
     */
    private static FilterManager instance;


    /**
     * Set of problem listeners.
     */
    public Set<FilterListener> filterListeners = new HashSet<>();

    /**
     * Adds the specified listener to the set of filterListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the FilterListener to add.
     */
    public void registerFilterListener(FilterListener listener) {
        filterListeners.add(listener);
    }

    /**
     * Returns Singleton instance of FilterManger
     */
    public static FilterManager getInstance() {
        if (instance == null) {
            instance = new FilterManager();
        }
        return instance;
    }


    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the FilterListener to remove.
     */
    public void removeFilterListener(FilterListener listener) {
        filterListeners.remove(listener);
    }


    /**
     * This method use to send to listeners filter state
     * @param filterState this filterState sends to all listeners
     */
    public void sendFilterState (FilterState filterState) {
        for (FilterListener listener : filterListeners) {
            listener.updateFilterState(filterState);
        }
    }

    /**
     * This method use to get  filter state for filter manager
     * @param filterState this filterState gets for filter manager
     */
        public void getFilterState (FilterState filterState) {
            sendFilterState(filterState);

    }

}
