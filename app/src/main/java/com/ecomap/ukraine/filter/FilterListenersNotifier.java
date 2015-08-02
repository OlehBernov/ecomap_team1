package com.ecomap.ukraine.filter;

/**
 * Created by Andriy on 01.08.2015.
 */
public interface FilterListenersNotifier {
    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the FilterListener to add.
     */
    void registerFilterListener(FilterListener listener);


    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the FilterListener to remove.
     */
    void removeFilterListener(FilterListener listener);


}
