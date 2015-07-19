package com.ecomap.ukraine.restclient;

/**
 * Created by Oleh on 7/19/2015.
 */
public interface RestListener {
    void send(int requestType, Object requestResult);
}
