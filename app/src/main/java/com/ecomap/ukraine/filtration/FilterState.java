package com.ecomap.ukraine.filtration;

import java.util.Calendar;
import java.util.Map;


/**
 * Contains state of the filter.
 */
public class FilterState {

    //TODO: sparse array
    private Map<String, Boolean> state;
    private Calendar dateFrom;
    private Calendar dateTo;

    /**
     * Constructor of the current filter state.
     *
     * @params state contains flags which describes the state of filter checkboxes.
     * @params dateFrom start date of filtration.
     * @params dateTo finish date of filtration.
     */
    public FilterState(final Map<String, Boolean> state, final Calendar dateFrom,
                       final Calendar dateTo) {
        this.state = state;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    /**
     * Returns true if filter is off.
     *
     * @return is filter off.
     */
    public boolean isFilterOff(final String filterType) {
        return state.get(filterType);
    }

    /**
     * Returns true if filter on.
     *
     * @return is filter on.
     */
    public boolean isFilterOn(final String filterType) {
        return !state.get(filterType);
    }

    public Map<String, Boolean> getState() {
        return state;
    }

    public Calendar getDateFrom() {
        return dateFrom;
    }

    public Calendar getDateTo() {
        return dateTo;
    }

}
