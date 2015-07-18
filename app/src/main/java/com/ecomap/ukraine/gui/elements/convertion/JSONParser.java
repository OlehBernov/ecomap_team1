package com.ecomap.ukraine.gui.elements.convertion;


import com.ecomap.ukraine.gui.elements.Comment;
import com.ecomap.ukraine.gui.elements.Details;

import java.util.ArrayList;

public class JSONParser {

    public ArrayList<Comment>
    parseBriefProblems(final String briefProblemsJson) {
        return new ArrayList<Comment>();
    }

    public ArrayList<Comment>
    parseFullProblem(final String fullProblemJson, final String userNameJson) {
        return new ArrayList<Comment>();
    }

    public ArrayList<Details> parseDetails(final String fullProblemJson,
                                           final String userNameJson) {
        return new ArrayList<Details>();
    }

    public int parseUserId(final String fullProblemJson) {
        return 1;
    }

}
