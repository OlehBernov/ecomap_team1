package com.ecomap.ukraine.updating.convertion;


import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.Types.ActivityType;
import com.ecomap.ukraine.models.Types.ProblemStatus;
import com.ecomap.ukraine.models.Types.ProblemType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs converting JSON to entities.
 *
 * @author Kyrychenko Oleksandr
 */
public class JSONParser {

    /**
     * Message which will written in exception,
     * if Parser get null argument.
     */
    private static final String NULL_ARGUMENT = "Argument is null";

    /**
     * Message which will assigned to numeric argument of entities,
     * if value from server be null.
     */
    private static final int DEFAULT_VALUE = -1;

    /**
     * Converts brief information from JSON to List of Problem objects.
     *
     * @param briefProblemsJson brief information about problems from server.
     * @return List of Problem objects with brief information about problems.
     * @throws JSONException if argument do not correct.
     */
    public List<Problem> parseBriefProblems(final String briefProblemsJson)
            throws JSONException {

        if (briefProblemsJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }

        List<Problem> briefProblems = new ArrayList<>();
        JSONArray problemsArray = new JSONArray(briefProblemsJson);
        for (int i = 0; i < problemsArray.length(); i++) {
            JSONObject jsonProblem = problemsArray.getJSONObject(i);
            Problem problem = getBasicDescription(jsonProblem);
            briefProblems.add(problem);
        }

        return briefProblems;
    }

    /**
     * Converts detailed information about concrete problem
     * from JSON to Detail object.
     *
     * @param detailedProblemJson detailed information about
     *                            concrete problem from server.
     * @return Detail object with detailed information about
     * concrete problem.
     * @throws JSONException if argument do not correct.
     */
    public Details parseDetailedProblem(final String detailedProblemJson)
            throws JSONException {

        if (detailedProblemJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }

        JSONArray detailedProblemArray = new JSONArray(detailedProblemJson);
        JSONObject problemDetails = (detailedProblemArray
                .getJSONArray(JSONFields.DETAILS_POSITION))
                .getJSONObject(0);

        List<ProblemActivity> problemActivities;
        problemActivities = getProblemActivities(detailedProblemArray
                .getJSONArray(JSONFields.PROBLEM_ACTIVITY_POSITION));

        Details details;
        details = new Details(
                problemDetails.optInt(JSONFields.ID, DEFAULT_VALUE),
                problemDetails.optInt(JSONFields.SEVERITY, DEFAULT_VALUE),
                problemDetails.optInt(JSONFields.MODERATION, DEFAULT_VALUE),
                problemDetails.optInt(JSONFields.VOTES, DEFAULT_VALUE),
                problemDetails.getString(JSONFields.PROBLEM_CONTENT),
                problemDetails.getString(JSONFields.PROPOSAL),
                problemDetails.getString(JSONFields.TITLE),
                problemActivities,
                getPhotos(detailedProblemArray
                        .getJSONArray(JSONFields.PHOTOS_POSITION)),
                String.valueOf(System.currentTimeMillis())
        );

        return details;
    }


    /**
     * Converts brief information about concrete problem from JSONObject
     * to Problem object.
     *
     * @param problemJsonObject brief information about concrete problem.
     * @return Problem object with brief information about
     * concrete problem.
     * @throws JSONException if argument do not correct.
     */
    private Problem getBasicDescription(final JSONObject problemJsonObject)
            throws JSONException {
        Problem problem;

        int problemStatusId = problemJsonObject.optInt(JSONFields.PROBLEM_STATUS,
                DEFAULT_VALUE);
        int problemTypesId = problemJsonObject.optInt(JSONFields.PROBLEM_TYPES_ID,
                DEFAULT_VALUE);

        problem = new Problem(
                problemJsonObject.optInt(JSONFields.ID, DEFAULT_VALUE),
                ProblemStatus.getProblemStatus(problemStatusId),
                ProblemType.getProblemType(problemTypesId),
                problemJsonObject.getString(JSONFields.TITLE),
                problemJsonObject.getString(JSONFields.PROBLEM_DATE),
                problemJsonObject.getDouble(JSONFields.LATITUDE),
                problemJsonObject.getDouble(JSONFields.LONGITUDE)
        );

        return problem;
    }

    /**
     * Converts information about all activities of
     * concrete problem from JSONOArray to List of ProblemActivity.
     *
     * @param problemActivitiesArray JSONArray with all activities of
     *                               concrete problem.
     * @return List with ProblemActivity objects.
     * @throws JSONException if argument do not correct.
     */
    private List<ProblemActivity> getProblemActivities(final JSONArray problemActivitiesArray)
            throws JSONException {

        List<ProblemActivity> problemActivities = new ArrayList<>();
        ProblemActivity currentProblemActivity;
        for (int i = 0; i < problemActivitiesArray.length(); i++) {
            JSONObject commentObject = problemActivitiesArray.getJSONObject(i);
            JSONObject contentObject;

            try {
                contentObject = new JSONObject(commentObject
                        .getString(JSONFields.PROBLEM_ACTIVITY_CONTENT));
            } catch (JSONException e) {
                continue;
            }

            int activityTypeId = commentObject.optInt(JSONFields.ACTIVITY_TYPES_ID, DEFAULT_VALUE);
            ActivityType activityTypeEnum = ActivityType.getActivityType(activityTypeId);

            currentProblemActivity = new ProblemActivity(
                    commentObject.optInt(JSONFields.PROBLEMS_ID, DEFAULT_VALUE),
                    commentObject.optInt(JSONFields.ID, DEFAULT_VALUE),
                    activityTypeEnum,
                    commentObject.optInt(JSONFields.COMMENT_USERS_ID, DEFAULT_VALUE),
                    contentObject.getString(JSONFields.COMMENT_CORE),
                    commentObject.getString(JSONFields.PROBLEM_ACTIVITY_DATE),
                    contentObject.getString(JSONFields.USER_NAME)
            );

            problemActivities.add(currentProblemActivity);
        }

        return problemActivities;
    }

    /**
     * Converts information about all photos of
     * concrete problem from JSONOArray to List of Photo.
     *
     * @param photosArray JSONArray with all photos of
     *                    concrete problem.
     * @return List of Photo objects.
     * @throws JSONException if argument do not correct.
     */
    private List<Photo> getPhotos(final JSONArray photosArray)
            throws JSONException {

        List<Photo> photos = new ArrayList<>();

        Photo currentPhoto;
        for (int i = 0; i < photosArray.length(); i++) {
            JSONObject photoObject = photosArray.getJSONObject(i);

            currentPhoto = new Photo(
                    photoObject.optInt(JSONFields.PROBLEMS_ID, DEFAULT_VALUE),
                    photoObject.optInt(JSONFields.ID, DEFAULT_VALUE),
                    photoObject.optInt(JSONFields.PHOTO_USERS_ID, DEFAULT_VALUE),
                    photoObject.optInt(JSONFields.PHOTO_STATUS, DEFAULT_VALUE),
                    JSONFields.PHOTOS_PATH + photoObject.getString(JSONFields.LINK),
                    photoObject.getString(JSONFields.PHOTO_DESCRIPTION)
            );
            photos.add(currentPhoto);
        }

        return photos;
    }

}
