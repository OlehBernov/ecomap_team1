package com.ecomap.ukraine.gui.elements;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Andriy on 17.07.2015.
 */
public class Problem {

    private int problemId;
    private int statusId;
    private int problemTypesId;

    private String title;
    private String date;

    private LatLng position;


    public int getProblemId() {
        return problemId;
    }

    public int getStatusId() {
        return statusId;
    }

    public int getProblemTypesId() {
        return problemTypesId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public LatLng getPosition() {
        return position;
    }




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
