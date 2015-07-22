package com.ecomap.ukraine.restclient;

/**
 * Created by Oleh on 7/19/2015.
 */
public interface RestListener {
    /**
     * This method is called if the specified DataManager object's
     * notifyObservers method is called.
     *
     * @param requestType the type of request handled.
     * @param requestResult the result of request.
     */
    void update(int requestType, Object requestResult);
}