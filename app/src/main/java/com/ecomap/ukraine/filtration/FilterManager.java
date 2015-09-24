package com.ecomap.ukraine.filtration;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ecomap.ukraine.util.ExtraFieldNames;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class FilterManager implements FilterListenersNotifier {

    private static final String DATE_TEMPLATE = "dd-MM-yyyy";

    /**
     * Holds the Singleton global instance of FilterManager.
     */
    private static FilterManager instance;

    protected final String TAG = getClass().getSimpleName();

    /**
     * Set of problem listeners.
     */
    private Set<FilterListener> filterListeners = new HashSet<>();

    /**
     * Activity which callback filter-manager
     */
    private Context context;

    /**
     * The latest version of filter settings.
     */
    private FilterState currentFilterState;

    /**
     * Filter manager constructor.
     */
    private FilterManager(final Context context) {
        this.context = context;
    }

    /**
     * Returns Singleton instance of FilterManger
     */
    public static FilterManager getInstance(final Context context) {
        if (instance == null) {
            synchronized (FilterManager.class) {
                if (instance == null) {
                    instance = new FilterManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Adds the specified listener to the set of filterListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the FilterListener to add.
     */
    public void registerFilterListener(final FilterListener listener) {
        filterListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of problemListeners.
     *
     * @param listener the FilterListener to remove.
     */
    public void removeFilterListener(final FilterListener listener) {
        filterListeners.remove(listener);
    }

    /**
     * This method use to send to listeners filter state
     *
     * @param filterState this filterState sends to all listeners
     */
    @Override
    public void sendFilterState(final FilterState filterState) {
        currentFilterState = filterState;
        for (FilterListener listener : filterListeners) {
            listener.updateFilterState(filterState);
        }
    }


    /**
     * This method use to get  filter state for filter manager
     *
     * @param filterState this filterState gets for filter manager
     */
    public void updateFilterState(final FilterState filterState) {
        sendFilterState(filterState);
    }

    /**
     * Returns the latest saved FilterState.
     *
     * @return filter state instance.
     */
    public FilterState getCurrentFilterState() {
        if (currentFilterState != null) {
            return currentFilterState;
        } else {
            return getFilterStateFromPreference();
        }
    }

    /**
     * Gets filter state form SharedPreferences
     *
     * @return filter state
     */
    private FilterState getFilterStateFromPreference() {
        SharedPreferences settings = context
                .getSharedPreferences(ExtraFieldNames.FILTERS_STATE, Context.MODE_PRIVATE);
        Set<String> filterStateSet = settings.getStringSet(ExtraFieldNames.FILTERS_STATE_SET,
                null);
        FilterState filterState;
        if (filterStateSet != null) {
            filterState = new FilterState(convertToCheckBoxesState(filterStateSet),
                    getDateFromPreference(settings, FilterContract.DATE_FROM),
                    getDateFromPreference(settings, FilterContract.DATE_TO));
        } else {
            filterState = null;
            Log.e(TAG, "filter null");
        }
        return filterState;
    }

    /**
     * Gets filtration date from SharedPreferences.
     *
     * @param settings SharedPreferences.
     * @param key      filtration date type ("from" or "to").
     * @return filtration date saved to SharedPreferences.
     */
    private Calendar getDateFromPreference(final SharedPreferences settings, final String key) {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);
        String dateFromString = settings.getString(key, String.valueOf(System.currentTimeMillis()));
        try {
            date.setTime(dateFormat.parse(dateFromString));
        } catch (ParseException e) {
            date.setTime(new Date(System.currentTimeMillis()));
        }

        return date;
    }

    /**
     * Converts JSON to FilterState object.
     *
     * @param filterStateSet saved filter state in JSON format.
     * @return object of the filter state.
     */
    private Map<String, Boolean> convertToCheckBoxesState(final Set<String> filterStateSet) {
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
