package com.ecomap.ukraine.validation;


class ValidationRequirements {

    public static int MIN_NAME_LENGTH = 3;
    public static int MAX_NAME_LENGTH = 45;
    public static int MIN_SURNAME_LENGTH = 3;
    public static int MAX_SURNAME_LENGTH = 45;
    public static int MIN_PASSWORD_LENGTH = 3;
    public static int MAX_PASSWORD_LENGTH = 45;

    public static final String NAME_FIELD_EMPTY = "Please fill out name field";

    public static final String NAME_FIELD_SHORT = "Name must be at least " +
            MIN_NAME_LENGTH + " characters.";

    public static final String NAME_FIELD_LONG ="Name must be no longer than " +
            MAX_NAME_LENGTH + " characters.";

    public static final String SURNAME_FIELD_EMPTY = "Please fill out surname field";

    public static final String SURNAME_FIELD_SHORT = "Surname must be at least" +
            MIN_SURNAME_LENGTH + " characters.";

    public static final String SURNAME_FIELD_LONG = "Surname must be no longer than " +
            MAX_SURNAME_LENGTH + " characters.";

    public static final String EMAIL_FIELD_EMPTY = "Please fill out email field";

    public static final String EMAIL_NOT_VALID = "Email address is incorrect.";

    public static final String PASSWORD_FIELD_EMPTY = "Please fill out password field";

    public static final String PASSWORD_FIELD_SHORT = "Password must be at least " +
            MIN_PASSWORD_LENGTH + " characters.";

    public static final String PASSWORD_FIELD_LONG = "Password must be no longer than" +
            MAX_PASSWORD_LENGTH + " characters.";

    public static final String CONFIRMATION_FIELD_EMPTY = "Please fill out confirmation field";

    public static final String CONFIRMATION_FAILED = "Passwords do not match.";

}
