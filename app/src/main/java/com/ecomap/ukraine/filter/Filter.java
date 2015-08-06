package com.ecomap.ukraine.filter;

import com.ecomap.ukraine.models.Problem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class Filter {

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

    private boolean filtration(final FilterState filterState, final Problem problem) {
        if (filterState.isShowProblemType(problem.getProblemTypesId())) {
            if (((filterState.isShowResolvedProblem()) && (problem.getStatusId() == 1))
                    || (((filterState.isShowUnsolvedProblem()) && (problem.getStatusId() == 0)))) {
                int day = Integer.parseInt(problem.getDate().substring(8, 10));
                int year = Integer.parseInt(problem.getDate().substring(0, 4));
                int month = Integer.parseInt(problem.getDate().substring(5, 7)) - 1;
                Calendar creatingProblemDate = new GregorianCalendar(year, month, day);
                if ((creatingProblemDate.after(filterState.getDateFrom())) &&
                        (filterState.getDateTo().after(creatingProblemDate))) {
                    return true;
                }
            }
        }
        return false;
    }
}
