package com.ecomap.ukraine.filtration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Performs convertion of the filter state to JSON format for saving to shared preferences.
 */
public class FilterStateConverter {

    private FilterStateConverter() {
    }

    /**
     * Converts filter state to string set.
     *
     * @param filterState state of the filters.
     * @return string set.
     */
    public static Set<String> convertToStringSet(final FilterState filterState) {
        Set<String> filterStateSet = new HashSet<>();
        for (String filterItem : filterState.getState().keySet()) {
            if (filterState.isFilterOff(filterItem)) {
                filterStateSet.add(filterItem);
            }
        }

        return filterStateSet;
    }

    /**
     * Maps the specified key to the specified value.
     *
     * @param filterStateSet saved filter state in string set.
     * @return check boxes state.
     */
    public static Map<String, Boolean> convertToCheckBoxesState(final Set<String> filterStateSet) {
        Map<String, Boolean> filterStateValues = new HashMap<>();
        filterStateValues.put(FilterContract.FOREST_DESTRUCTION,
                filterStateSet.contains(FilterContract.FOREST_DESTRUCTION));
        filterStateValues.put(FilterContract.RUBBISH_DUMP,
                filterStateSet.contains(FilterContract.RUBBISH_DUMP));
        filterStateValues.put(FilterContract.ILLEGAL_BUILDING,
                filterStateSet.contains(FilterContract.ILLEGAL_BUILDING));
        filterStateValues.put(FilterContract.WATER_POLLUTION,
                filterStateSet.contains(FilterContract.WATER_POLLUTION));
        filterStateValues.put(FilterContract.THREAD_TO_BIODIVERSITY,
                filterStateSet.contains(FilterContract.THREAD_TO_BIODIVERSITY));
        filterStateValues.put(FilterContract.POACHING,
                filterStateSet.contains(FilterContract.POACHING));
        filterStateValues.put(FilterContract.OTHER,
                filterStateSet.contains(FilterContract.OTHER));
        filterStateValues.put(FilterContract.RESOLVED,
                filterStateSet.contains(FilterContract.RESOLVED));
        filterStateValues.put(FilterContract.UNSOLVED,
                filterStateSet.contains(FilterContract.UNSOLVED));

        return filterStateValues;
    }

}
