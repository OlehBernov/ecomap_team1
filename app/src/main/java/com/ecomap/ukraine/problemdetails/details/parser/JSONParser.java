package com.ecomap.ukraine.problemdetails.details.parser;

import org.json.JSONException;
import org.json.JSONObject;


public final class JSONParser {

    private JSONParser(){}

    public static JSONObject generateCommentObj (final String userID,
                                                 final String userName, final String userSurname,
                                                 final String content) throws JSONException {
        JSONObject dataJson = new JSONObject();
        JSONObject resultJson = new JSONObject();

        dataJson.put(JSONFields.USER_ID, userID);
        dataJson.put(JSONFields.USER_NAME, userName);
        dataJson.put(JSONFields.USER_SURNAME, userSurname);
        dataJson.put(JSONFields.CONTENT, content);
        resultJson.put(JSONFields.DATA, dataJson);
        return resultJson;
    }

}
