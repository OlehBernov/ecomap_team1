package com.ecomap.ukraine.problemposting;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Performs converting JSON to entities.
 */
public final class JSONParser {

    private JSONParser(){}

    public static String parseAddedProblemInformation(final String addedProblemJSON) throws JSONException {
        if (addedProblemJSON == null) {
            throw new JSONException(JSONFields.NULL_ARGUMENT);
        }
        JSONObject addedProblem = new JSONObject(addedProblemJSON);
        return String.valueOf(addedProblem
                .getJSONObject(JSONFields.JSON)
                .getInt(JSONFields.INSERT_ID));
    }

}
