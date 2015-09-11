package com.ecomap.ukraine.filtration;

import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemStatus;
import com.ecomap.ukraine.models.ProblemType;

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
     *
     * @param problems    all problems
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
            return filteredProblem;
        } else {
            return problems;
        }
    }

    /**
     * Converts problem type to appropriate filter criteria.
     *
     * @param type problem type.
     * @return appropriate filter criteria.
     */
    public String getFilterCriteria(final ProblemType type) {
        switch (type) {
            case FOREST_DESTRUCTION:
                return FilterContract.FOREST_DESTRUCTION;
            case RUBBISH_DUMP:
                return FilterContract.RUBBISH_DUMP;
            case ILLEGAL_BUILDING:
                return FilterContract.ILLEGAL_BUILDING;
            case WATER_POLLUTION:
                return FilterContract.WATER_POLLUTION;
            case THREAD_TO_BIODIVERSITY:
                return FilterContract.THREAD_TO_BIODIVERSITY;
            case POACHING:
                return FilterContract.POACHING;
            case OTHER:
                return FilterContract.OTHER;
            default:
                return FilterContract.OTHER;
        }
    }

    private boolean filtration(final FilterState filterState, final Problem problem) {
        if (filterState.isFilterOff(getFilterCriteria(problem.getProblemType()))) {
            if (showProblemBySolvedFilter(filterState, problem)) {
                if (showActualProblem(filterState, problem)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Filtration by solved state
     *
     * @param filterState rules of filtration
     * @param problem     problem under filtration
     * @return access to show problem
     */
    private boolean showProblemBySolvedFilter(final FilterState filterState,
                                              final Problem problem) {
        return showResolvedProblem(filterState, problem)
                || showUnsolvedProblem(filterState, problem);

    }

    /**
     * Filtration problem by date
     *
     * @param filterState rules of filtration
     * @param problem     problem under filtration
     * @return access to show problem
     */
    private boolean showActualProblem(final FilterState filterState, final Problem problem) {
        int day = Integer.parseInt(problem.getDate().substring(8, 10));
        int year = Integer.parseInt(problem.getDate().substring(0, 4));
        int month = Integer.parseInt(problem.getDate().substring(5, 7)) - 1;
        Calendar creatingProblemDate = new GregorianCalendar(year, month, day);
        return creatingProblemDate.after(filterState.getDateFrom())
                && filterState.getDateTo().after(creatingProblemDate);
    }

    /**
     * Show problem if it is resolved and filter allows to show resolved problem
     *
     * @param filterState rules of filtration
     * @param problem     problem under filtration
     * @return access to show problem
     */
    private boolean showResolvedProblem(final FilterState filterState, final Problem problem) {
        return filterState.isFilterOff(FilterContract.RESOLVED)
                && (problem.getStatus() == ProblemStatus.RESOLVED);
    }

    /**
     * Show problem if it is unsolved and filter allows to show unsolved problem
     *
     * @param filterState rules of filtration
     * @param problem     problem under filtration
     * @return access to show problem
     */
    private boolean showUnsolvedProblem(final FilterState filterState, final Problem problem) {
        return filterState.isFilterOff(FilterContract.UNSOLVED)
                && (problem.getStatus() == ProblemStatus.UNSOLVED);

    }

}
