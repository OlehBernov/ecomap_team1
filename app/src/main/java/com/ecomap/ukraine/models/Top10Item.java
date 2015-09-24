package com.ecomap.ukraine.models;

/**
 * Created by Andriy on 10.09.2015.
 */
public class Top10Item {

    public static final int TOP_LIKE_FRAGMENT_ID = 0;
    public static final int TOP_VOTE_FRAGMENT_ID = 1;
    public static final int TOP_SEVERITY_FRAGMENT_ID = 2;

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
