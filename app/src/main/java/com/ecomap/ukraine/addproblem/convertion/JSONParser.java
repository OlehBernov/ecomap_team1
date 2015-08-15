package com.ecomap.ukraine.addproblem.convertion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andriy on 12.08.2015.
 */
public class JSONParser {

    private static final String NULL_ARGUMENT = "Argument is null";


    public String parseAddedProblemInformation (final String addedProblemJSON) throws JSONException {
        if (addedProblemJSON == null) {
            throw new JSONException(NULL_ARGUMENT);
        }
        JSONObject addedProblem = new JSONObject(addedProblemJSON);
        String id = String.valueOf(addedProblem.getJSONObject("json").getInt("insertId"));
        return id;
    }
}
