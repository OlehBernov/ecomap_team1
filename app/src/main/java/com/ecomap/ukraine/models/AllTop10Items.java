package com.ecomap.ukraine.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andriy on 10.09.2015.
 */
public class AllTop10Items {
    private List <Top10Item> mostPopularProblems;
    private List <Top10Item> mostImportantProblems;
    private List <Top10Item> mostLikedProblems;

    public AllTop10Items( List <Top10Item> mostPopularProblems,
                          List <Top10Item> mostImportantProblems,
                          List <Top10Item> mostLikedProblems ) {
        this.mostPopularProblems = mostPopularProblems;
        this.mostImportantProblems = mostImportantProblems;
        this.mostLikedProblems = mostLikedProblems;
    }

    public List<Top10Item> getMostPopularProblems() {
        return mostPopularProblems;
    }

    public List<Top10Item> getMostImportantProblems() {
        return mostImportantProblems;
    }

    public List<Top10Item> getMostLikedProblems() {
        return mostLikedProblems;
    }
}
