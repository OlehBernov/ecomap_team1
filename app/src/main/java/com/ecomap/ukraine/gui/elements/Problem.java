package com.ecomap.ukraine.gui.elements;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Andriy on 17.07.2015.
 */
public class Problem {

    int problemId;
    int statusId;
    int problemTypesId;

    String title;
    String date;

    LatLng position;
    Details details;


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

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
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
