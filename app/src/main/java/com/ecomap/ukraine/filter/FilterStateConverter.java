package com.ecomap.ukraine.filter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FilterStateConverter {

    private static final String DATE_TEMPLATE = "dd-MM-yyyy";

    public String convertToJson(final FilterState filterState) throws JSONException {

        JSONObject filterStateJson = new JSONObject();
        filterStateJson.put(FilterContract.TYPE_1, filterState.isShowProblemType1());
        filterStateJson.put(FilterContract.TYPE_2, filterState.isShowProblemType2());
        filterStateJson.put(FilterContract.TYPE_3, filterState.isShowProblemType3());
        filterStateJson.put(FilterContract.TYPE_4, filterState.isShowProblemType4());
        filterStateJson.put(FilterContract.TYPE_5, filterState.isShowProblemType5());
        filterStateJson.put(FilterContract.TYPE_6, filterState.isShowProblemType6());
        filterStateJson.put(FilterContract.TYPE_7, filterState.isShowProblemType7());
        filterStateJson.put(FilterContract.RESOLVED, filterState.isShowResolvedProblem());
        filterStateJson.put(FilterContract.UNSOLVED, filterState.isShowUnsolvedProblem());

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);

        Calendar dateFrom = filterState.getDateFrom();
        String formattedDateFrom = dateFormat.format(dateFrom.getTime());
        filterStateJson.put(FilterContract.DATE_FROM, formattedDateFrom);

        Calendar dateTo = filterState.getDateTo();
        String formattedDateTo = dateFormat.format(dateTo.getTime());
        filterStateJson.put(FilterContract.DATE_TO, formattedDateTo);

        return filterStateJson.toString();
    }

    public FilterState convertToFilterState(final String filterStateJson)
            throws JSONException {

        JSONObject filterStateJsonObject = new JSONObject(filterStateJson);

        Calendar dateFrom = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);
        String dateFromString = filterStateJsonObject.getString(FilterContract.DATE_FROM);
        try {
            dateFrom.setTime(dateFormat.parse(dateFromString));
        } catch (ParseException e) {
            //TODO
            Log.e("parseException", "convertToFilterState from");
            e.printStackTrace();
        }

        Calendar dateTo = Calendar.getInstance();
        String dateToString = filterStateJsonObject.getString(FilterContract.DATE_TO);
        try {
            dateTo.setTime(dateFormat.parse(dateToString));
        } catch (ParseException e) {
            //TODO
            Log.e("parseException", "convertToFilterState to");
            e.printStackTrace();
        }

        FilterState filterState = new FilterState(
                filterStateJsonObject.getBoolean(FilterContract.TYPE_1),
                filterStateJsonObject.getBoolean(FilterContract.TYPE_2),
                filterStateJsonObject.getBoolean(FilterContract.TYPE_3),
                filterStateJsonObject.getBoolean(FilterContract.TYPE_4),
                filterStateJsonObject.getBoolean(FilterContract.TYPE_5),
                filterStateJsonObject.getBoolean(FilterContract.TYPE_6),
                filterStateJsonObject.getBoolean(FilterContract.TYPE_7),
                filterStateJsonObject.getBoolean(FilterContract.RESOLVED),
                filterStateJsonObject.getBoolean(FilterContract.UNSOLVED),
                dateFrom,
                dateTo
        );

        return filterState;
    }
}
