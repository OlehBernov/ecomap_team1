package com.ecomap.ukraine.filtration;


public interface FilterListener {

    /**
     * Send to listeners list of all problems.
     *
     * @param filterState state of filter
     */
    void updateFilterState(FilterState filterState);

    //TODO: renderer
    void setRenderer();

}
