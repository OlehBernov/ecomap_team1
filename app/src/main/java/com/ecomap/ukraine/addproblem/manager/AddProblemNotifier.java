package com.ecomap.ukraine.addproblem.manager;

public interface AddProblemNotifier {

    void registerAddProblemListener(AddProblemListener listener);

    void removeAddProblemListener(AddProblemListener listener);

}
