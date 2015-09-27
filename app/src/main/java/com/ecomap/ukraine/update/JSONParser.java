package com.ecomap.ukraine.update;


import android.util.SparseIntArray;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.Top10Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs converting JSON to entities.
 */
public final class JSONParser {

    /**
     * Text which contains some information field of problem.
     * Such text string will replace to empty string.
     */
    public static final String WRONG_TEXT = "null";
    /**
     * Path to photos on server.
     */
    public static final String PHOTOS_PATH = "http://ecomap.org/photos/large/";
    /**
     * Message which will written in exception,
     * if Parser get null argument.
     */
    private static final String NULL_ARGUMENT = "Argument is null";

    private JSONParser() {
    }

    /**
     * Converts brief information from JSON to List of Problem objects.
     *
     * @param briefProblemsJson brief information about problems from server.
     * @return List of Problem objects with brief information about problems.
     * @throws JSONException if argument do not correct.
     */
    public static List<Problem> parseBriefProblems(final String briefProblemsJson)
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

    public static JSONObject generateCommentObj(final String userID,
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

    public static AllTop10Items parseAllTop10Items(final String allTop10ItemsJson)
            throws JSONException {
        if (allTop10ItemsJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }
        JSONArray allTop10ItemsArray = new JSONArray(allTop10ItemsJson);
        JSONArray top10VotesArray = allTop10ItemsArray.getJSONArray(0);
        JSONArray top10SeverityArray = allTop10ItemsArray.getJSONArray(1);
        JSONArray top10CommentArray = allTop10ItemsArray.getJSONArray(2);
        List<Top10Item> top10VoteList = getTop10VoteList(top10VotesArray);
        List<Top10Item> top10SeverityList = getTop10SeverityList(top10SeverityArray);
        List<Top10Item> top10CommentList = getTop10CommentList(top10CommentArray);

        return new AllTop10Items(top10CommentList, top10SeverityList,
                top10VoteList);
    }

    /**
     * Converts detailed information about concrete problem
     * from JSON to Detail object.
     *
     * @param detailedProblemJson detailed information about
     *                            concrete problem from server.
     * @return Details object with detailed information about
     * concrete problem.
     * @throws JSONException if argument is not correct.
     */
    public static Details parseDetailedProblem(final String detailedProblemJson)
            throws JSONException {

        if (detailedProblemJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }

        JSONArray detailedProblemArray = new JSONArray(detailedProblemJson);
        JSONObject problemDetails = detailedProblemArray
                .getJSONArray(JSONFields.DETAILS_POSITION)
                .getJSONObject(0);

        List<ProblemActivity> problemActivities;
        problemActivities = getProblemActivities(detailedProblemArray
                .getJSONArray(JSONFields.PROBLEM_ACTIVITY_POSITION));

        String problemContent = problemDetails.getString(JSONFields.PROBLEM_CONTENT);
        String proposal = problemDetails.getString(JSONFields.PROPOSAL);
        Details details;
        details = new Details(
                problemDetails.optInt(JSONFields.ID, -1),
                problemDetails.optInt(JSONFields.SEVERITY, -1),
                problemDetails.optInt(JSONFields.MODERATION, -1),
                problemDetails.optInt(JSONFields.VOTES, -1),
                problemContent.equals(WRONG_TEXT) ? "" : problemContent,
                proposal.equals(WRONG_TEXT) ? "" : proposal,
                problemDetails.getString(JSONFields.TITLE),
                problemActivities,
                getPhotos(detailedProblemArray
                        .getJSONArray(JSONFields.PHOTOS_POSITION)),
                String.valueOf(System.currentTimeMillis())
        );

        return details;
    }


    /**
     * @param statisticsItemJson statistics of problem posting from server.
     * @return SparseIntArray with problem id and relevant number of posing.
     * @throws JSONException if argument is not correct.
     */
    public static SparseIntArray parseStatisticsItem(final String statisticsItemJson)
            throws JSONException {
        if (statisticsItemJson == null) {
            throw new JSONException(NULL_ARGUMENT);
        }

        SparseIntArray statisticsItem = new SparseIntArray();
        JSONArray statisticItemsJSONArray = new JSONArray(statisticsItemJson);
        for (int i = 0; i < statisticItemsJSONArray.length(); i++) {
            JSONObject itemJSONObject = statisticItemsJSONArray.getJSONObject(i);
            statisticsItem.append(itemJSONObject.getInt(JSONFields.STATISTICS_ID),
                    itemJSONObject.getInt(JSONFields.STATISTICS_VALUE));
        }
        return statisticsItem;
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
    private static Problem getBasicDescription(final JSONObject problemJsonObject)
            throws JSONException {
        Problem problem;

        problem = new Problem(
                problemJsonObject.optInt(JSONFields.ID, -1),
                problemJsonObject.optInt(JSONFields.PROBLEM_STATUS, -1),
                problemJsonObject.optInt(JSONFields.PROBLEM_TYPES_ID, -1),
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
    private static List<ProblemActivity> getProblemActivities(final JSONArray problemActivitiesArray)
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

            currentProblemActivity = new ProblemActivity(
                    commentObject.optInt(JSONFields.PROBLEMS_ID, -1),
                    commentObject.optInt(JSONFields.ID, -1),
                    commentObject.optInt(JSONFields.ACTIVITY_TYPES_ID, -1),
                    commentObject.optInt(JSONFields.COMMENT_USERS_ID, -1),
                    contentObject.getString(JSONFields.COMMENT_CORE),
                    commentObject.getString(JSONFields.PROBLEM_ACTIVITY_DATE),
                    contentObject.optString(JSONFields.USER_NAME, "")
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
    private static List<Photo> getPhotos(final JSONArray photosArray)
            throws JSONException {

        List<Photo> photos = new ArrayList<>();

        Photo currentPhoto;
        for (int i = 0; i < photosArray.length(); i++) {
            JSONObject photoObject = photosArray.getJSONObject(i);

            currentPhoto = new Photo(
                    photoObject.optInt(JSONFields.PROBLEMS_ID, -1),
                    photoObject.optInt(JSONFields.ID, -1),
                    photoObject.optInt(JSONFields.PHOTO_STATUS, -1),
                    PHOTOS_PATH + photoObject.getString(JSONFields.LINK),
                    photoObject.getString(JSONFields.PHOTO_DESCRIPTION)
            );
            photos.add(currentPhoto);
        }

        return photos;
    }

    private static List<Top10Item> getTop10VoteList(final JSONArray top10VotesArray)
            throws JSONException {
        List<Top10Item> top10VoteList = new ArrayList<>();
        Top10Item currentTop10Item;
        for (int i = 0; i < top10VotesArray.length(); i++) {
            JSONObject currentItemObject = top10VotesArray.getJSONObject(i);
            int itemId = currentItemObject.optInt(JSONFields.TOP_10_ITEM_ID, -1);
            String title = currentItemObject.optString(JSONFields.TOP_10_TITLE, "");
            int value = currentItemObject.optInt(JSONFields.TOP_10_VOTES, -1);
            currentTop10Item = new Top10Item(itemId, title, value);
            top10VoteList.add(currentTop10Item);
        }

        return top10VoteList;
    }

    private static List<Top10Item> getTop10SeverityList(final JSONArray top10SeverityArray)
            throws JSONException {
        List<Top10Item> top10SeverityList = new ArrayList<>();
        Top10Item currentTop10Item;
        for (int i = 0; i < top10SeverityArray.length(); i++) {
            JSONObject currentItemObject = top10SeverityArray.getJSONObject(i);
            int itemId = currentItemObject.optInt(JSONFields.TOP_10_ITEM_ID, -1);
            String title = currentItemObject.optString(JSONFields.TOP_10_TITLE, "");
            int value = currentItemObject.optInt(JSONFields.TOP_10_SEVERITY, -1);
            currentTop10Item = new Top10Item(itemId, title, value);
            top10SeverityList.add(currentTop10Item);
        }

        return top10SeverityList;
    }

    private static List<Top10Item> getTop10CommentList(final JSONArray top10ComentArray)
            throws JSONException {
        List<Top10Item> top10ComentList = new ArrayList<>();
        Top10Item currentTop10Item;
        for (int i = 0; i < top10ComentArray.length(); i++) {
            JSONObject currentItemObject = top10ComentArray.getJSONObject(i);
            int itemId = currentItemObject.optInt(JSONFields.TOP_10_ITEM_ID, -1);
            String title = currentItemObject.optString(JSONFields.TOP_10_TITLE, "");
            int value = currentItemObject.optInt(JSONFields.TOP_10_COMMENTS, -1);
            currentTop10Item = new Top10Item(itemId, title, value);
            top10ComentList.add(currentTop10Item);
        }

        return top10ComentList;
    }

}
