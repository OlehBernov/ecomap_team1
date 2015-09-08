package com.ecomap.ukraine.filtration;


public interface FilterListener {

    /**
     * Send to listeners list of all problems.
     *
     * @param filterState state of filter
     */
    void updateFilterState(final FilterState filterState);

    void setRenderer();

}
