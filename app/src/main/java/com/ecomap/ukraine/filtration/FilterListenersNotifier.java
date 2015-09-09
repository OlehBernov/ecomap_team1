package com.ecomap.ukraine.filtration;


public interface FilterListenersNotifier {

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the FilterListener to add.
     */
    void registerFilterListener(FilterListener listener);

    /**
     * Removes the specified listener from the set of problemListeners.
     *
     * @param listener the FilterListener to remove.
     */
    void removeFilterListener(FilterListener listener);

    /**
     * Sends FilterState object to listeners.
     *
     * @param filterState current filter state.
     */
    void sendFilterState(FilterState filterState);
}
