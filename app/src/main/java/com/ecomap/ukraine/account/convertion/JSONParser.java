package com.ecomap.ukraine.account.convertion;

import com.ecomap.ukraine.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alexander on 06.08.2015.
 */
public class JSONParser {

    private static final String NULL_ARGUMENT = "Argument is null";

    private static int DEFAULT_VALUE = -1;

    /**
     * TODO docs
     *
     * @param userInformationJson
     * @return
     * @throws JSONException
     */
    public User parseUserInformation(final String userInformationJson)
            throws JSONException {

        if (userInformationJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }

        JSONObject userInformation = new JSONObject(userInformationJson);
        User user = new User(
                userInformation.optInt(JSONFields.USER_ID, DEFAULT_VALUE),
                userInformation.getString(JSONFields.NAME),
                userInformation.getString(JSONFields.SURNAME),
                userInformation.getString(JSONFields.ROLE),
                userInformation.getString(JSONFields.IAT),
                userInformation.getString(JSONFields.TOKEN),
                userInformation.getString(JSONFields.EMAIL)
        );

        return user;
    }

    public User parseRegistrationInformation (final String registrationUserInformationJson, final String email)
            throws JSONException {
        if (registrationUserInformationJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }
        JSONObject registrationUserInformation = new JSONObject(registrationUserInformationJson);
        User user = new User(
                registrationUserInformation.getJSONObject(JSONFields.REGISTRATION_ID).getInt(JSONFields.ID_OF_REGISTRATION_USER),
                registrationUserInformation.getString(JSONFields.NAME),
                registrationUserInformation.getString(JSONFields.SURNAME),
                registrationUserInformation.getString(JSONFields.ROLE),
                registrationUserInformation.getString(JSONFields.IAT),
                registrationUserInformation.getString(JSONFields.TOKEN),
                email
        );

        return user;
    }
}
