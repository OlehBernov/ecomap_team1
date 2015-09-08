package com.ecomap.ukraine.authentication.validator;

/**
* Requirements for correct log in and registration text fields.
*/
public class ValidationRequirements {

	/**
	* Minimal length of user name.
	*/
    public static final int MIN_NAME_LENGTH = 3;
	
	/**
	* Maximal length of user name.
	*/
    public static final int MAX_NAME_LENGTH = 45;
	
	/**
	* Minimal length of user surname.
	*/
    public static final int MIN_SURNAME_LENGTH = 3;
	
	/**
	* Maximal length of user surname.
	*/
    public static final int MAX_SURNAME_LENGTH = 45;
	
	/**
	* Minimal length of user password.
	*/
    public static final int MIN_PASSWORD_LENGTH = 3;
	
	/**
	* Maximal length of user password.
	*/
    public static final int MAX_PASSWORD_LENGTH = 45;

	/**
	* Message for user about empty field.
	*/
    public static final String NAME_FIELD_EMPTY = "Please fill out name field";

	/**
	* Message for user about empty new problem title.
	*/
    public static final String Title_FIELD_EMPTY = "Please fill out problem title field";

	/**
	* Message for user about too short name.
	*/
    public static final String NAME_FIELD_SHORT = "Name must be at least " +
            MIN_NAME_LENGTH + " characters.";

	/**
	* Message for user about too long name.
	*/
    public static final String NAME_FIELD_LONG ="Name must be no longer than " +
            MAX_NAME_LENGTH + " characters.";

	/**
	* Message for user about empty surname field.
	*/
    public static final String SURNAME_FIELD_EMPTY = "Please fill out surname field";

	/**
	* Message for user about too short surname.
	*/
    public static final String SURNAME_FIELD_SHORT = "Surname must be at least" +
            MIN_SURNAME_LENGTH + " characters.";

	/**
	* Message for user about too long surname.
	*/
    public static final String SURNAME_FIELD_LONG = "Surname must be no longer than " +
            MAX_SURNAME_LENGTH + " characters.";

	/**
	* Message for user about empty email field.
	*/
    public static final String EMAIL_FIELD_EMPTY = "Please fill out email field";

	/**
	* Message for user about error in email structure.
	*/
    public static final String EMAIL_NOT_VALID = "Email address is incorrect.";

	/**
	* Message for user about empty email field.
	*/
    public static final String PASSWORD_FIELD_EMPTY = "Please fill out password field";

	/**
	* Message for user about too short password.
	*/
    public static final String PASSWORD_FIELD_SHORT = "Password must be at least " +
            MIN_PASSWORD_LENGTH + " characters.";

	/**
	* Message for user about too long password.
	*/
    public static final String PASSWORD_FIELD_LONG = "Password must be no longer than" +
            MAX_PASSWORD_LENGTH + " characters.";

	/**
	* Message for user about empty confirmation field.
	*/
    public static final String CONFIRMATION_FIELD_EMPTY = "Please fill out confirmation field";

	/**
	* Message for user about that passwords do not matches.
	*/
    public static final String CONFIRMATION_FAILED = "Passwords do not match.";

}
