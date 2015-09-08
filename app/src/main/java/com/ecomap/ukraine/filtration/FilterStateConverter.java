package com.ecomap.ukraine.filtration;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Performs convertion of the filter state to JSON format for saving to shared preferences,
 * converts JSON to FilterState object.
 */
public class FilterStateConverter {

    private static final String DATE_TEMPLATE = "dd-MM-yyyy";

    /**
     * Converts filter state to JSON format.
     *
     * @return filter state in JSON format.
     * @params filterState state of the filters.
     */
    public static String convertToJson(final FilterState filterState) throws JSONException {

        JSONObject filterStateJson = new JSONObject();
        filterStateJson.put(FilterContract.FOREST_DESTRUCTION,
                filterState.isFilterOff(FilterContract.FOREST_DESTRUCTION));
        filterStateJson.put(FilterContract.RUBBISH_DUMP,
                filterState.isFilterOff(FilterContract.RUBBISH_DUMP));
        filterStateJson.put(FilterContract.ILLEGAL_BUILDING,
                filterState.isFilterOff(FilterContract.ILLEGAL_BUILDING));
        filterStateJson.put(FilterContract.WATER_POLLUTION,
                filterState.isFilterOff(FilterContract.WATER_POLLUTION));
        filterStateJson.put(FilterContract.THREAD_TO_BIODIVERSITY,
                filterState.isFilterOff(FilterContract.THREAD_TO_BIODIVERSITY));
        filterStateJson.put(FilterContract.POACHING,
                filterState.isFilterOff(FilterContract.POACHING));
        filterStateJson.put(FilterContract.OTHER,
                filterState.isFilterOff(FilterContract.OTHER));
        filterStateJson.put(FilterContract.RESOLVED,
                filterState.isFilterOff(FilterContract.RESOLVED));
        filterStateJson.put(FilterContract.UNSOLVED,
                filterState.isFilterOff(FilterContract.UNSOLVED));

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);

        Calendar dateFrom = filterState.getDateFrom();
        String formattedDateFrom = dateFormat.format(dateFrom.getTime());
        filterStateJson.put(FilterContract.DATE_FROM, formattedDateFrom);

        Calendar dateTo = filterState.getDateTo();
        String formattedDateTo = dateFormat.format(dateTo.getTime());
        filterStateJson.put(FilterContract.DATE_TO, formattedDateTo);

        return filterStateJson.toString();
    }

    /**
     * Converts JSON to FilterState object.
     *
     * @return object of the filter state.
     * @params filterStateJson saved filter state in JSON format.
     */
    public static FilterState convertToFilterState(final String filterStateJson)
            throws JSONException {

        JSONObject filterStateJsonObject = new JSONObject(filterStateJson);

        Calendar dateFrom = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);
        String dateFromString = filterStateJsonObject.getString(FilterContract.DATE_FROM);
        try {
            dateFrom.setTime(dateFormat.parse(dateFromString));
        } catch (ParseException e) {
            dateFrom.setTime(new Date(System.currentTimeMillis()));
            Log.e("parseException", "convertToFilterState from");
        }

        Calendar dateTo = Calendar.getInstance();
        String dateToString = filterStateJsonObject.getString(FilterContract.DATE_TO);
        try {
            dateTo.setTime(dateFormat.parse(dateToString));
        } catch (ParseException e) {
            dateTo.setTime(new Date(System.currentTimeMillis()));
            Log.e("parseException", "convertToFilterState to");
        }

        Map<String, Boolean> filterStateValues = new HashMap<>();
        filterStateValues.put(FilterContract.FOREST_DESTRUCTION,
                filterStateJsonObject.getBoolean(FilterContract.FOREST_DESTRUCTION));
        filterStateValues.put(FilterContract.RUBBISH_DUMP,
                filterStateJsonObject.getBoolean(FilterContract.RUBBISH_DUMP));
        filterStateValues.put(FilterContract.ILLEGAL_BUILDING,
                filterStateJsonObject.getBoolean(FilterContract.ILLEGAL_BUILDING));
        filterStateValues.put(FilterContract.WATER_POLLUTION,
                filterStateJsonObject.getBoolean(FilterContract.WATER_POLLUTION));
        filterStateValues.put(FilterContract.THREAD_TO_BIODIVERSITY,
                filterStateJsonObject.getBoolean(FilterContract.THREAD_TO_BIODIVERSITY));
        filterStateValues.put(FilterContract.POACHING,
                filterStateJsonObject.getBoolean(FilterContract.POACHING));
        filterStateValues.put(FilterContract.OTHER,
                filterStateJsonObject.getBoolean(FilterContract.OTHER));
        filterStateValues.put(FilterContract.RESOLVED,
                filterStateJsonObject.getBoolean(FilterContract.RESOLVED));
        filterStateValues.put(FilterContract.UNSOLVED,
                filterStateJsonObject.getBoolean(FilterContract.UNSOLVED));

        return new FilterState(filterStateValues, dateFrom, dateTo);
    }

}
