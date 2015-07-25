package com.ecomap.ukraine.data.manager;

/**
 * Interface DataListener must implements classes,
 * which want to get information about problem from Server.
 */
public interface DataListener {
    /**
     * This method is called if the specified DataManager object's
     * notifyObservers method is called.
     *
     * @param requestType the type of request handled.
     * @param requestResult the result of request.
     */
    void update(int requestType, Object requestResult);
}