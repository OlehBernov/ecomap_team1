package com.ecomap.ukraine.models;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;


/**
 * Class Problem represent general information about problem, enough to show the problem on the map.
 */
public class Problem implements ClusterItem, Serializable {

    /**
     * Unsolved problems.
     */
    public static final int UNSOLVED = 0;

    /**
     * Resolved problems.
     */
    public static final int RESOLVED = 1;

    public static final int FOREST_DESTRUCTION = 1;
    public static final int RUBBISH_DUMP = 2;
    public static final int ILLEGAL_BUILDING = 3;
    public static final int WATER_POLLUTION = 4;
    public static final int THREAD_TO_BIODIVERSITY = 5;
    public static final int POACHING = 6;
    public static final int OTHER = 7;

    /**
     * id of current problem
     */
    private int problemId;

    /**
     * represent status of current problem resolved (1) or not resolved (0)
     */
    private int statusId;

    /**
     * represent type of current problem
     */
    private int problemType;

    /**
     * contains title of current problem
     */
    private String title;
    /**
     * contains adding date of current problem
     */
    private String date;

    private double latitude;

    private double longitude;

    /**
     * Constructor of class
     *
     * @param problemId   id of current problem
     * @param statusId    represent status of current problem resolved (1) or not resolved (0)
     * @param problemType represent type of current problem
     * @param title       contains title of current problem
     * @param date        contains adding date of current problem
     * @param latitude    contains latitude of coordinate current problem
     * @param longitude   contains longitude of coordinate current problem
     */

    public Problem(int problemId, int statusId, int problemType,
                   String title, String date, double latitude, double longitude) {
        this.problemId = problemId;
        this.statusId = statusId;
        this.problemType = problemType;
        this.title = title;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * provides access to problemId
     */
    public int getProblemId() {
        return problemId;
    }

    /**
     * provides access to statusId
     */
    public int getStatus() {
        return statusId;
    }

    /**
     * provides access to problemType
     */
    public int getProblemType() {
        return problemType;
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
     * provides access to problem latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * provides acces to problem longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * provides access to position
     */
    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }


}
