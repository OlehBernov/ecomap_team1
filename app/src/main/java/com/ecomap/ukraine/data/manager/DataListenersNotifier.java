package com.ecomap.ukraine.data.manager;

/**
 * Interface of class, which performs notification listeners
 * about result of server response.
 */
public interface DataListenersNotifier {

    /**
     * Adds the specified listener to the set of dataListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the DataListener to add.
     */
    void registerDataListener(DataListener listener);

    /**
     * Removes the specified listener from the set of dataListeners.
     *
     * @param listener the DataListener to remove.
     */
    void removeDataListener(DataListener listener);

    /**
     * Notify all dataListeners about server response
     * and send them received information.
     *
     * @param requestType type of request.
     * @param requestResult server response converted to the objects of entities.
     */
    void notifyDataListeners(final int requestType, Object requestResult);
}
