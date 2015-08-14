package com.ecomap.ukraine.addproblem.manager;

/**
 * Created by Andriy on 12.08.2015.
 */
public interface AddProblemNotifier {

    void registerAddProblemListener(final AddProblemListener listener);


    void removeAddProblemListener(final AddProblemListener listener);

    void sendAddProblemResult(boolean result);
}
