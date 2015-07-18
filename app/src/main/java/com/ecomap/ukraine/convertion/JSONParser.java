package com.ecomap.ukraine.convertion;


import com.ecomap.ukraine.gui.elements.Comment;
import com.ecomap.ukraine.gui.elements.Details;
import com.ecomap.ukraine.gui.elements.Photo;
import com.ecomap.ukraine.gui.elements.Problem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.ecomap.ukraine.constants.JSONFields;

public class JSONParser {

    public static final String NULL_ARGUMENT = "Argument is null";

    public static final int DEFAULT_VALUE = -1;

    public List<Problem>
    parseBriefProblems(final String briefProblemsJson) throws JSONException {

        if (briefProblemsJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        };;

        List<Problem> briefProblems = new ArrayList<Problem>();

        JSONArray problemsArray = new JSONArray(briefProblemsJson);
        for (int i = 0; i < problemsArray.length(); i++) {
            JSONObject jsonProblem = problemsArray.getJSONObject(i);
            Problem problem = getBasicDescription(jsonProblem);
            briefProblems.add(problem);
        }

        return briefProblems;
    }

    private Problem
    getBasicDescription(final JSONObject problemObject) throws JSONException {
        Problem problem;

        problem = new Problem(problemObject.optInt(JSONFields.ID,
                                                   DEFAULT_VALUE),
                              problemObject.optInt(JSONFields.PROBLEM_STATUS,
                                                   DEFAULT_VALUE),
                              problemObject.optInt(JSONFields.PROBLEM_TYPES_ID,
                                                   DEFAULT_VALUE),
                              problemObject.getString(JSONFields.TITLE),
                              problemObject.getString(JSONFields.PROBLEM_DATE),
                              problemObject.getDouble(JSONFields.LATITUDE),
                              problemObject.getDouble(JSONFields.LONGTITUDE));

        return problem;
    }

    public Details
    parseDetailedProblem(final String detailedProblemJson) throws
                                                           JSONException {

        if (detailedProblemJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }

        JSONArray detailedProblemArray = new JSONArray(detailedProblemJson);
        JSONObject problemDetails = (detailedProblemArray
                                     .getJSONArray(JSONFields.DETAILS_POSITION))
                                     .getJSONObject(0);
        Details details;
        details = new Details(problemDetails.optInt(JSONFields.SEVERITY,
                                                    DEFAULT_VALUE),
                              problemDetails.optInt(JSONFields.MODERATION,
                                                    DEFAULT_VALUE),
                              problemDetails.optInt(JSONFields.VOTES,
                                                    DEFAULT_VALUE),
                              problemDetails.getString(JSONFields
                                                       .PROBLEM_CONTENT),
                              problemDetails.getString(JSONFields
                                                       .PROPOSAL),
                              getComments(detailedProblemArray
                                         .getJSONArray(JSONFields
                                                       .COMMENTS_POSITION)),
                              getPhotos(detailedProblemArray
                                        .getJSONArray(JSONFields
                                                      .PHOTOS_POSITION)));

        return details;
    }

    private List<Comment>
    getComments(final JSONArray commentsArray) throws JSONException {
        List<Comment> comments = new ArrayList<Comment>();
        Comment currentComment;
        for (int i = 0; i < commentsArray.length(); i++) {
            JSONObject commentObject = commentsArray.getJSONObject(i);
            JSONObject contentObject;
            contentObject = new JSONObject(commentObject
                                           .getString(JSONFields
                                                      .COMMENT_CONTENT));

            currentComment = new Comment(commentObject
                                         .optInt(JSONFields.ID, DEFAULT_VALUE),
                                         commentObject
                                         .optInt(JSONFields.ACTIVITY_TYPES_ID,
                                                 DEFAULT_VALUE),
                                         commentObject
                                         .optInt(JSONFields.COMMENT_USERS_ID,
                                                 DEFAULT_VALUE),
                                         contentObject
                                         .getString(JSONFields.COMMENT_CORE),
                                         commentObject
                                         .getString(JSONFields.COMMENT_DATE),
                                         contentObject
                                         .getString(JSONFields.USER_NAME));

            comments.add(currentComment);
        }

        return comments;
    }

    private List<Photo>
    getPhotos(final JSONArray photosArray) throws JSONException {

        List<Photo> photos = new ArrayList<Photo>();

        Photo currentPhoto;
        for (int i = 0; i < photosArray.length(); i++) {
            JSONObject photoObject = photosArray.getJSONObject(i);

            currentPhoto = new Photo(photoObject.optInt(JSONFields.ID,
                                                        DEFAULT_VALUE),
                                     photoObject.optInt(JSONFields
                                                        .PHOTO_USERS_ID,
                                                        DEFAULT_VALUE),
                                     photoObject.optInt(JSONFields.PHOTO_STATUS,
                                                        DEFAULT_VALUE),
                                     photoObject.getString(JSONFields.LINK),
                                     photoObject.getString(JSONFields
                                                          .PHOTO_DESCRIPTION));
            photos.add(currentPhoto);
        }

        return photos;
    }

}
