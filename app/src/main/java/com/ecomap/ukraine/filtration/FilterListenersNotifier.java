package com.ecomap.ukraine.filtration;


public interface FilterListenersNotifier {

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the FilterListener to add.
     */
    void registerFilterListener(final FilterListener listener);

    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the FilterListener to remove.
     */
    void removeFilterListener(final FilterListener listener);

}
