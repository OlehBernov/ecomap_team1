package com.ecomap.ukraine.filter;

import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Types.ProblemStatus;

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
     * @return unfiltered problems
     */
    public List<Problem> filterProblem(final List<Problem> problems,
                                       final FilterState filterState) {
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
        if (filterState.isShowProblemType(problem.getProblemType())) {
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
        return showResolvedProblem(filterState, problem) || showUnsolvedProblem(filterState, problem);

    }

    /**
     * Show problem if it is unsolwed and filter allows to show unsolwed problem
     * @param filterState rules of filtration
     * @param problem problem under filtration
     * @return access to show problem
     */
    private  boolean showUnsolvedProblem(FilterState filterState, Problem problem) {
        return (filterState.isShowUnsolvedProblem()) && (problem.getStatus().equals(ProblemStatus.UNSOLVED));

    }

    /**
     * Show problem if it is resolved and filter allows to show resolved problem
     * @param filterState rules of filtration
     * @param problem problem under filtration
     * @return access to show problem
     */
    private  boolean showResolvedProblem(FilterState filterState, Problem problem) {
        return (filterState.isShowResolvedProblem()) && (problem.getStatus().equals(ProblemStatus.RESOLVED));
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
        return creatingProblemDate.after(filterState.getDateFrom())
               && filterState.getDateTo().after(creatingProblemDate);
    }

}
