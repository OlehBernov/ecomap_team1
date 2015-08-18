package com.ecomap.ukraine.addproblem.manager;

public interface AddProblemNotifier {

    void registerAddProblemListener(final AddProblemListener listener);

    void removeAddProblemListener(final AddProblemListener listener);


}
