package com.ecomap.ukraine.problemposting.manager;

/**
 * Interface of class, which performs notification listeners
 * about posting problems.
 */
public interface AddProblemNotifier {

    void registerAddProblemListener(AddProblemListener listener);

    void removeAddProblemListener(AddProblemListener listener);

}
