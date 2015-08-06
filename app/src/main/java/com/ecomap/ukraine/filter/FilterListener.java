package com.ecomap.ukraine.filter;

/**
 * Created by Andriy on 01.08.2015.
 */
public interface FilterListener {

    /**
     * Send to listeners list of all problems.
     *
     * @param filterState state of filter
     */
    void updateFilterState(final FilterState filterState);

}
