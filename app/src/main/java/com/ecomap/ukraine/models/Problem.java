package com.ecomap.ukraine.models;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;


/**
 * Class Problem represent general information about problem, enough to show the problem on the map.
 * Created by Andriy on 17.07.2015.
 */
public class Problem implements ClusterItem, Serializable {

    /**
     * id of current problem
     */
    private int problemId;

    /**
     * represent status of current problem resolved (1) or not resolved (0)
     */
    private ProblemStatus statusId;

    /**
     * represent type of current problem
     */
    private ProblemType problemType;

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
     * @param statusId    represend status of current problem resolved (1) or not resolved (0)
     * @param problemType represend type of current problem
     * @param title       contains title of current problem
     * @param date        contains adding date of current problem
     * @param latitude    contains latitude of coordinate current problem
     * @param longitude   contains longitude of coordinate current problem
     */

    public Problem(int problemId, ProblemStatus statusId, ProblemType problemType,
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
    public ProblemStatus getStatus() {
        return statusId;
    }

    /**
     * provides access to problemType
     */
    public ProblemType getProblemType() {
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
