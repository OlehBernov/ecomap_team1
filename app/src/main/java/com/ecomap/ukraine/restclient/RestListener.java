package com.ecomap.ukraine.restclient;

/**
 * Created by Oleh on 7/19/2015.
 */
public interface RestListener {
    void update(int requestType, Object requestResult);
}
