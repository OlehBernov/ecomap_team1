package com.ecomap.ukraine.problemdetails.manager;

/**
 * Created by Andriy on 26.08.2015.
 */
public interface DetailsNotifier {

    void registerDetailsListener(DetailsListener listener);

    void removeDetailsListener(DetailsListener listener);

    void removeAllDetailsListener();
}
