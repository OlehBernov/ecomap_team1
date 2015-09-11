package com.ecomap.ukraine.models;

/**
 * Created by Andriy on 10.09.2015.
 */
public class Top10Item {
    private int problemID;
    private String title;
    private int value;

    public Top10Item(int problemID, String title, int value) {
        this.problemID = problemID;
        this.title = title;
        this.value = value;
    }

    public int getProblemID() {
        return problemID;
    }

    public String getTitle() {
        return title;
    }

    public int getValue() {
        return value;
    }
}
