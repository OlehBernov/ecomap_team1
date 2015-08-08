package com.ecomap.ukraine.filter;

import com.ecomap.ukraine.models.Types.ProblemType;

import java.util.Calendar;

/**
 * to be continued...
 */
public class FilterState {

    private boolean showProblemType1;
    private boolean showProblemType2;
    private boolean showProblemType3;
    private boolean showProblemType4;
    private boolean showProblemType5;
    private boolean showProblemType6;
    private boolean showProblemType7;

    private boolean showResolvedProblem;
    private boolean showUnsolvedProblem;

    private Calendar DateFrom;
    private Calendar DateTo;

    public FilterState(final boolean showProblemType1, final boolean showProblemType2,
                       final boolean showProblemType3, final boolean showProblemType4,
                       final boolean showProblemType5, final boolean showProblemType6,
                       final boolean showProblemType7, final boolean showResolvedProblem,
                       final boolean showUnsolvedProblem, final Calendar DateFrom,
                       final Calendar DateTo) {

        this.showProblemType1 = showProblemType1;
        this.showProblemType2 = showProblemType2;
        this.showProblemType3 = showProblemType3;
        this.showProblemType4 = showProblemType4;
        this.showProblemType5 = showProblemType5;
        this.showProblemType6 = showProblemType6;
        this.showProblemType7 = showProblemType7;
        this.showResolvedProblem = showResolvedProblem;
        this.showUnsolvedProblem = showUnsolvedProblem;
        this.DateFrom = DateFrom;
        this.DateTo = DateTo;
    }


    public boolean isShowProblemType1() {
        return showProblemType1;
    }

    public boolean isShowProblemType2() {
        return showProblemType2;
    }

    public boolean isShowProblemType3() {
        return showProblemType3;
    }

    public boolean isShowProblemType4() {
        return showProblemType4;
    }

    public boolean isShowProblemType5() {
        return showProblemType5;
    }

    public boolean isShowProblemType6() {
        return showProblemType6;
    }

    public boolean isShowProblemType7() {
        return showProblemType7;
    }


    public boolean isShowProblemType (final ProblemType type) {
        switch (type) {
            case FOREST_DESTRUCTION:
                return isShowProblemType1();
            case RUBBISH_DUMP:
                return isShowProblemType2();
            case ILLEGAL_BUILDING:
                return isShowProblemType3();
            case WATER_POLLUTION:
                return isShowProblemType4();
            case THREAD_TO_BIODIVERSITY:
                return isShowProblemType5();
            case POACHING:
                return isShowProblemType6();
            case OTHER:
                return isShowProblemType7();
        }
        return false;
    }

    public boolean isShowResolvedProblem() {
        return showResolvedProblem;
    }

    public boolean isShowUnsolvedProblem() {
        return showUnsolvedProblem;
    }

    public Calendar getDateFrom() {
        return DateFrom;
    }

    public Calendar getDateTo() {
        return DateTo;
    }
}
