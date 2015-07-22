package com.ecomap.ukraine.models;

import com.google.android.gms.maps.model.LatLng;


/**
 * Class Problem represent general information about problem, enough to show the problem on the map.
 * Created by Andriy on 17.07.2015.
 */
public class Problem {

    //id of current problem
    private int problemId;
    //represend status of current problem resolved (1) or not resolved (0)
    private int statusId;
    //represend type of current problem
    private int problemTypesId;
    //contains title of current problem
    private String title;
    //contains adding date of current problem
    private String date;
    //contains coordinate of current problem
    private LatLng position;


    /**
     * provides access to problemId
     */
    public int getProblemId() {
        return problemId;
    }

    /**
     * provides access to statusId
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * provides access to problemTypesId
     */
    public int getProblemTypesId() {
        return problemTypesId;
    }

    /**
     * provides access to title
     */
    public String getTitle() {
        return title;
    }

    /**
     * provides access to date
     */
    public String getDate() {
        return date;
    }

    /**
     * provides access to position
     */
    public LatLng getPosition() {
        return position;
    }


    /**
     * Constructor of class
     * @param problemId id of current problem
     * @param statusId  represend status of current problem resolved (1) or not resolved (0)
     * @param problemTypesId represend type of current problem
     * @param title contains title of current problem
     * @param date contains adding date of current problem
     * @param latitude contains latitude of coordinate current problem
     * @param longtitude contains longtitude of coordinate current problem
     */

    public Problem(int problemId, int statusId, int problemTypesId,
                   String title, String date, double latitude, double longtitude) {
        this.problemId = problemId;
        this.statusId = statusId;
        this.problemTypesId = problemTypesId;
        this.title = title;
        this.date = date;
        this.position = new LatLng(latitude, longtitude);
    }
}
