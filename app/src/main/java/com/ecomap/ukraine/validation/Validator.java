package com.ecomap.ukraine.validation;


import android.util.Patterns;
import android.widget.EditText;

import com.ecomap.ukraine.R;

/**
 * Performs field validation.
 */
public class Validator {

    /**
     * Performs validation of registration fields.
     *
     * @param name user name field.
     * @param surname user surname field.
     * @param email user email field.
     * @param password user password field.
     * @param passwordConfirmation password confirmation field.
     * @return whether the all registration fields is valid.
     */
    public static boolean registrationValidation(final EditText name, final EditText surname,
                                                 final EditText email, final EditText password,
                                                 final EditText passwordConfirmation) {

        /*Performs validation on all fields.
         Allows show error messages on all wrong field each time. */
        return nameValidation(name)
               & surnameValidation(surname)
               & emailValidation(email)
               & passwordValidation(password)
               & passwordConfirmation(password, passwordConfirmation);
    }

    /**
     * Performs validation of log in fields.
     *
     * @param email user email field.
     * @param password user password field.
     * @return whether the log in field is valid.
     */
    public static boolean logInValid(final EditText email, final EditText password) {
        return emailValidation(email) & passwordValidation(password);
    }

    /**
     * Performs validation of new user problem.
     *
     * @param problemTitle problem title field.
     * @return whether the new problem content is valid.
     */
    public static boolean addProblemValidation(final EditText problemTitle) {
        if(problemTitle == null) {
            return false;
        }
        String title = problemTitle.getText().toString();
        if (title.isEmpty()) {
            problemTitle.setError(ValidationRequirements.Title_FIELD_EMPTY);
            return false;
        }

        return true;
    }

    /**
     * Performs validation of user name field.
     *
     * @param nameField user name field.
     * @return whether the user name field is valid.
     */
    private static boolean nameValidation(final EditText nameField) {
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

    /**
     * Performs validation of user surname.
     *
     * @param surnameField user surname field.
     * @return whether the user surname is valid.
     */
    private static boolean surnameValidation(final EditText surnameField) {
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

    /**
     * Performs user password validation.
     *
     * @param emailField user email field.
     * @return whether the user email is valid.
     */
    private static boolean emailValidation(final EditText emailField) {
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

    /**
     * Performs user password validation.
     *
     * @param passwordField user password field.
     * @return whether the user password is valid.
     */
    private static boolean passwordValidation(final EditText passwordField) {
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

    /**
     * Performs validation of password confirmation.
     *
     * @param passwordField user password field.
     * @param passwordConfirmationField password confirmation field.
     * @return whether the passwords matches.
     */
    private static boolean passwordConfirmation(final EditText passwordField,
                                         final EditText passwordConfirmationField) {
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
