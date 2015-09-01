package com.ecomap.ukraine.details.convertion;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {

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
