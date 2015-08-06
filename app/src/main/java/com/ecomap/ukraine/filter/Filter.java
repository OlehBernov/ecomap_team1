package com.ecomap.ukraine.filter;

import com.ecomap.ukraine.models.Problem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Class which is responsible for filtration
 */
public class Filter {

    /**
     * Filter all problem
     * @param problems all problems
     * @param filterState rules of filtration
     * @return ufiltered problems
     */
    public List<Problem> filterProblem(final List<Problem> problems,
                                       final FilterState filterState) {

    public List<Problem> filterProblem(List<Problem> problems, FilterState filterState) {
        if (filterState != null) {
            List<Problem> filteredProblem = new ArrayList<>();
            for (Problem problem : problems) {
                if (filtration(filterState, problem)) {
                    filteredProblem.add(problem);
                }
            }
            return  filteredProblem;
        } else {
            return problems;
        }
    }

    /**
    * Filtration process
    * @param filterState rules of filtration
    * @param problem
    * @return
    */    
    private boolean filtration(final FilterState filterState, final Problem problem) {

    private boolean filtration(FilterState filterState, Problem problem) {
        if (filterState.isShowProblemType(problem.getProblemTypesId())) {
            if (showProblemBySolvedFilter(filterState, problem)) {
                if (showActualProblem(filterState, problem)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Filtration by solwed state
     * @param filterState rules of filtration
     * @param problem problem under filtration
     * @return access to show problem
     */
    private boolean showProblemBySolvedFilter (FilterState filterState, Problem problem) {
        if ((showResolwedProblem(filterState, problem)) || (showUnsolwedProblem(filterState, problem))) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Show problem if it is unsolwed and filter allows to show unsolwed problem
     * @param filterState rules of filtration
     * @param problem problem under filtration
     * @return access to show problem
     */
    private  boolean showUnsolwedProblem (FilterState filterState, Problem problem) {
        if ((filterState.isShowUnsolvedProblem()) && (problem.getStatusId() == 0)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Show problem if it is resolwed and filter allows to show resolwed problem
     * @param filterState rules of filtration
     * @param problem problem under filtration
     * @return access to show problem
     */
    private  boolean showResolwedProblem (FilterState filterState, Problem problem) {
        if ((filterState.isShowResolvedProblem()) && (problem.getStatusId() == 1)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Filtration problem by date
     * @param filterState rules of filtration
     * @param problem problem under filtration
     * @return access to show problem
     */
    private boolean showActualProblem (FilterState filterState, Problem problem) {
        int day = Integer.parseInt(problem.getDate().substring(8, 10));
        int year = Integer.parseInt(problem.getDate().substring(0, 4));
        int month = Integer.parseInt(problem.getDate().substring(5, 7)) - 1;
        Calendar creatingProblemDate = new GregorianCalendar(year, month, day);
        if ((creatingProblemDate.after(filterState.getDateFrom())) &&
                (filterState.getDateTo().after(creatingProblemDate))) {
            return true;
        }
        else return false;
    }

}
