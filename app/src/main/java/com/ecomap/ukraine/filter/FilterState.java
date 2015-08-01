package com.ecomap.ukraine.filter;

import java.util.Calendar;
import java.util.Date;

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

    private Date DateFrom;
    private Date DateTo;

    public FilterState(boolean showProblemType1, boolean showProblemType2,
                       boolean showProblemType3, boolean showProblemType4,
                       boolean showProblemType5, boolean showProblemType6,
                       boolean showProblemType7, boolean showResolvedProblem,
                       boolean showUnsolvedProblem, Date DateFrom,
                       Date DateTo) {

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

    public boolean isShowResolvedProblem() {
        return showResolvedProblem;
    }

    public boolean isShowUnsolvedProblem() {
        return showUnsolvedProblem;
    }

    public Date getDateFrom() {
        return DateFrom;
    }

    public Date getDateTo() {
        return DateTo;
    }
}
