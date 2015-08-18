package com.ecomap.ukraine.addproblem.convertion;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    public String parseAddedProblemInformation(final String addedProblemJSON) throws JSONException {
        if (addedProblemJSON == null) {
            throw new JSONException(JSONFields.NULL_ARGUMENT);
        }
        JSONObject addedProblem = new JSONObject(addedProblemJSON);
        return String.valueOf(addedProblem
                .getJSONObject(JSONFields.JSON)
                .getInt(JSONFields.INSERT_ID));
    }

}
