package com.ecomap.ukraine.filter;

import java.util.Calendar;
import java.util.Map;


/**
* Contains state of the filter.
*/
public class FilterState {

    private Map<String, Boolean> state;
    private Calendar DateFrom;
    private Calendar DateTo;

	/**
	* Constructor of the current filter state.
	*
	* @params state contains flags which describes the state of filter checkboxes.		
	* @params DateFrom start date of filtration.
	* @params DateTo finish date of filtration.
	*/
    public FilterState(final Map<String, Boolean> state, final Calendar DateFrom,
                       final Calendar DateTo) {
        this.state = state;
        this.DateFrom = DateFrom;
        this.DateTo = DateTo;
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

	/**
	* Returns the start date of filtration.
	*
	* @return start date of filtration.
	*/
    public Calendar getDateFrom() {
        return DateFrom;
    }

	/**
	* Returns the finish date of filtration.
	*
	* @return finish date of filtration.
	*/
    public Calendar getDateTo() {
        return DateTo;
    }
}
