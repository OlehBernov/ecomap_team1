package com.ecomap.ukraine.data.manager;

import java.util.Set;

/**
 * Created by Alexander on 24.07.2015.
 */
public interface ListenersNotifier {

    public void registerListener(DataListener listener);
    public void removeListener(DataListener listener);
    public void notifyListeners(final int requestType, Object requestResult,
                                 final Set<DataListener> listeners);
}
