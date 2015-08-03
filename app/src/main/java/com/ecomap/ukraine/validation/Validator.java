package com.ecomap.ukraine.validation;


import android.util.Patterns;
import android.widget.EditText;

import com.ecomap.ukraine.R;

public class Validator {

    public boolean registrationValidation(EditText name, EditText surname, EditText email,
                                          EditText password, EditText passwordConfirmation) {
        return nameValidation(name)
               & surnameValidation(surname)
               & emailValidation(email)
               & passwordValidation(password)
               & passwordConfirmation(password, passwordConfirmation);
    }

    public boolean logInValid(EditText email, EditText password) {
        return emailValidation(email) & passwordValidation(password);
    }

    private boolean nameValidation(EditText nameField) {
        String name = nameField.getText().toString();
        if (name.isEmpty()) {
            nameField.setError(ValidationRequirements.NAME_FIELD_EMPTY);
            return false;
        } else if(name.length() < ValidationRequirements.MIN_NAME_LENGTH) {
            nameField.setError(ValidationRequirements.NAME_FIELD_SHORT);
            return false;
        } else if (name.length() > ValidationRequirements.MAX_NAME_LENGTH) {
            nameField.setError(ValidationRequirements.NAME_FIELD_LONG);
            return false;
        }
        return true;
    }

    private boolean surnameValidation(EditText surnameField) {
        String surname = surnameField.getText().toString();
        if (surname.isEmpty()) {
            surnameField.setError(ValidationRequirements.SURNAME_FIELD_EMPTY);
            return false;
        } else if(surname.length() < ValidationRequirements.MIN_SURNAME_LENGTH) {
            surnameField.setError(ValidationRequirements.SURNAME_FIELD_SHORT);
            return false;
        } else if(surname.length() > ValidationRequirements.MAX_SURNAME_LENGTH) {
            surnameField.setError(ValidationRequirements.SURNAME_FIELD_LONG);
            return false;
        }
        return true;
    }

    private boolean emailValidation(EditText emailField) {
        String email = emailField.getText().toString();
        if (email.isEmpty()) {
            emailField.setError(ValidationRequirements.EMAIL_FIELD_EMPTY);
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError(ValidationRequirements.EMAIL_NOT_VALID);
            return false;
        }
        return true;
    }

    private boolean passwordValidation(EditText passwordField) {
        String password = passwordField.getText().toString();
        if (password.isEmpty()) {
            passwordField.setError(ValidationRequirements.PASSWORD_FIELD_EMPTY);
            return false;
        } else if (password.length() < ValidationRequirements.MIN_PASSWORD_LENGTH) {
            passwordField.setError(ValidationRequirements.PASSWORD_FIELD_SHORT);
            return false;
        } else if (password.length() > ValidationRequirements.MAX_PASSWORD_LENGTH) {
            passwordField.setError(ValidationRequirements.PASSWORD_FIELD_LONG);
            return false;
        }
        return true;
    }

    private boolean passwordConfirmation(EditText passwordField, EditText passwordConfirmationField) {
        String passwordConfirmation = passwordConfirmationField.getText().toString();
        String password = passwordField.getText().toString();
        if (passwordConfirmation.isEmpty()) {
            passwordConfirmationField.setError(ValidationRequirements.CONFIRMATION_FIELD_EMPTY);
            return false;
        } else if (!passwordConfirmation.equals(password)){
            passwordConfirmationField.setError(ValidationRequirements.CONFIRMATION_FAILED);
            passwordConfirmationField.setHint(R.string.confirm_password);
            return false;
        }
        return true;
    }

}