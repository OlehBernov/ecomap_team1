package com.ecomap.ukraine.filter;

import java.util.Calendar;
import java.util.Map;


public class FilterState {

    private Map<String, Boolean> state;
    private Calendar DateFrom;
    private Calendar DateTo;

    public FilterState(final Map<String, Boolean> state, final Calendar DateFrom,
                       final Calendar DateTo) {
        this.state = state;
        this.DateFrom = DateFrom;
        this.DateTo = DateTo;
    }

    public boolean isFilterOff(final String filterType) {
        return state.get(filterType);
    }

    public boolean isFilterOn(final String filterType) {
        return !state.get(filterType);
    }
    
    public Calendar getDateFrom() {
        return DateFrom;
    }

    public Calendar getDateTo() {
        return DateTo;
    }
}
