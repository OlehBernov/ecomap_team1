package com.ecomap.ukraine.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.filtration.FilterContract;
import com.ecomap.ukraine.filtration.FilterManager;
import com.ecomap.ukraine.filtration.FilterState;
import com.ecomap.ukraine.util.ExtraFieldNames;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class FilterFragment extends android.support.v4.app.Fragment {

    private static final String DATE_FROM_TAG = "Date from tag";
    private static final String DATE_TO_TAG = "Date to tag";
    private static final String DATE_TEMPLATE = "dd-MM-yyyy";
    private static final String DEFAULT_DATE_FROM = "01-01-1990";
    private static final String DEFAULT_DATE_TO = "31-12-2030";
    private static final int MIDNIGHT_TIME = 0;

    private FilterManager filterManager;
    private View layoutView;
    private Calendar calendarDateFrom;
    private Calendar calendarDateTo;
    private CalendarDatePickerDialog dialogDateFrom;
    private CalendarDatePickerDialog dialogDateTo;

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.filter_content, container, false);

        filterManager = FilterManager.getInstance(getActivity().getApplicationContext());
        FilterState filterState = filterManager.getCurrentFilterState();
        setFiltersState(filterState);
        createDateFromPickerDialog();
        createDateToPickerDialog();

        return layoutView;
    }

    /**
     * Changed checkButton state on filter layout.
     *
     * @param view view of concrete checkBox.
     */
    public void switchCheckButtonState(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        filterManager.updateFilterState(buildFiltersState());
    }

    /**
     * Opens "date from" choosing window on filter layout.
     */
    public void dateFromChoosing(FragmentManager fragmentManager) {
        dialogDateFrom.show(fragmentManager, DATE_FROM_TAG);
    }

    /**
     * Opens "date to" choosing window on filter layout.
     */
    public void dateToChoosing(FragmentManager fragmentManager) {
        dialogDateTo.show(fragmentManager, DATE_TO_TAG);
    }

    /**
     * Prepares dates to displaying on screen.
     */
    public void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);
        try {
            if (calendarDateFrom == null) {
                calendarDateFrom = Calendar.getInstance();
                calendarDateFrom.setTime(dateFormat.parse(DEFAULT_DATE_FROM));
            }
            if (calendarDateTo == null) {
                calendarDateTo = Calendar.getInstance();
                calendarDateTo.setTime(dateFormat.parse(DEFAULT_DATE_TO));
            }
        } catch (ParseException e) {
            Log.e("parseException", "setDate to");
            return;
        }

        setDateOnScreen(calendarDateFrom, calendarDateTo);
    }

    /**
     * Saves current state of filter fields to SharedPreferences.
     */
    public void saveState(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(ExtraFieldNames.FILTERS_STATE, 0);
        SharedPreferences.Editor editor = settings.edit();
        FilterState filterState = buildFiltersState();
        editor.putStringSet(ExtraFieldNames.FILTERS_STATE_SET, convertToStringSet(filterState));

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);

        Calendar dateFrom = filterState.getDateFrom();
        String formattedDateFrom = dateFormat.format(dateFrom.getTime());
        editor.putString(FilterContract.DATE_FROM, formattedDateFrom);

        Calendar dateTo = filterState.getDateTo();
        String formattedDateTo = dateFormat.format(dateTo.getTime());
        editor.putString(FilterContract.DATE_TO, formattedDateTo);

        editor.apply();
    }

    /**
     * Creates FilterState object which describes current state of the filter.
     * Information about filter state it gets from checkBoxes.
     *
     * @return object with current state of filter.
     */
    private FilterState buildFiltersState() {
        Map<String, Boolean> filterStateValues = new HashMap<>();
        filterStateValues.put(FilterContract.FOREST_DESTRUCTION,
                getFilterState(R.id.forestry_issues));
        filterStateValues.put(FilterContract.RUBBISH_DUMP,
                getFilterState(R.id.rubbish_dump));
        filterStateValues.put(FilterContract.ILLEGAL_BUILDING,
                getFilterState(R.id.illegal_building));
        filterStateValues.put(FilterContract.WATER_POLLUTION,
                getFilterState(R.id.water_pollution));
        filterStateValues.put(FilterContract.THREAD_TO_BIODIVERSITY,
                getFilterState(R.id.biodiversity));
        filterStateValues.put(FilterContract.POACHING,
                getFilterState(R.id.poaching));
        filterStateValues.put(FilterContract.OTHER,
                getFilterState(R.id.other));
        filterStateValues.put(FilterContract.RESOLVED,
                getFilterState(R.id.ButtonResolved));
        filterStateValues.put(FilterContract.UNSOLVED,
                getFilterState(R.id.ButtonUnsolved));

        return new FilterState(filterStateValues, calendarDateFrom, calendarDateTo);
    }

    /**
     * Returns state of concrete checkBox.
     *
     * @param id checkbox id.
     * @return checkBox is on or off.
     */
    private boolean getFilterState(int id) {
        Checkable checkBox = (CheckBox) layoutView.findViewById(id);
        return checkBox.isChecked();
    }

    /**
     * CPerforms date fields validation. Does not allows set "date to" less than "date from".
     *
     * @param dateFrom start date for filtration set by user.
     * @param dateTo   finish date for filtration set by user.
     * @return current date interval is possible or not.
     */
    private boolean isValidDates(Calendar dateFrom, Calendar dateTo) {
        return dateTo.after(dateFrom);
    }

    /**
     * Creates "date from" choosing dialog.
     */
    private void createDateFromPickerDialog() {
        CalendarDatePickerDialog.OnDateSetListener dateFromListener =
                new CalendarDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialog dialog, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date.set(Calendar.HOUR_OF_DAY, MIDNIGHT_TIME);
                        date.set(Calendar.MINUTE, MIDNIGHT_TIME);
                        date.set(Calendar.SECOND, MIDNIGHT_TIME);

                        if (isValidDates(date, calendarDateTo)) {
                            calendarDateFrom = date;
                            setDate();
                            filterManager.updateFilterState(buildFiltersState());
                        }
                    }
                };

        dialogDateFrom = new CalendarDatePickerDialog();
        dialogDateFrom.setOnDateSetListener(dateFromListener);
    }

    /**
     *Creates "date to" choosing dialog.
     */
    private void createDateToPickerDialog() {
        CalendarDatePickerDialog.OnDateSetListener dateToListener =
                new CalendarDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialog dialog, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (isValidDates(calendarDateFrom, date)) {
                            calendarDateTo = date;
                            setDate();
                            filterManager.updateFilterState(buildFiltersState());
                        }
                    }
                };

        dialogDateTo = new CalendarDatePickerDialog();
        dialogDateTo.setOnDateSetListener(dateToListener);
    }

    /**
     * Sets filter fields to right state according to saved filter state.
     *
     * @param filtersState state of the filter.
     */
    private void setFiltersState(FilterState filtersState) {
        if (filtersState != null) {
            if (filtersState.isFilterOn(FilterContract.FOREST_DESTRUCTION)) {
                setFilterOn(layoutView.findViewById(R.id.forestry_issues));
            }
            if (filtersState.isFilterOn(FilterContract.RUBBISH_DUMP)) {
                setFilterOn(layoutView.findViewById(R.id.rubbish_dump));
            }
            if (filtersState.isFilterOn(FilterContract.ILLEGAL_BUILDING)) {
                setFilterOn(layoutView.findViewById(R.id.illegal_building));
            }
            if (filtersState.isFilterOn(FilterContract.WATER_POLLUTION)) {
                setFilterOn(layoutView.findViewById(R.id.water_pollution));
            }
            if (filtersState.isFilterOn(FilterContract.THREAD_TO_BIODIVERSITY)) {
                setFilterOn(layoutView.findViewById(R.id.biodiversity));
            }
            if (filtersState.isFilterOn(FilterContract.POACHING)) {
                setFilterOn(layoutView.findViewById(R.id.poaching));
            }
            if (filtersState.isFilterOn(FilterContract.OTHER)) {
                setFilterOn(layoutView.findViewById(R.id.other));
            }
            if (filtersState.isFilterOn(FilterContract.RESOLVED)) {
                setFilterOn(layoutView.findViewById(R.id.ButtonResolved));
            }
            if (filtersState.isFilterOn(FilterContract.UNSOLVED)) {
                setFilterOn(layoutView.findViewById(R.id.ButtonUnsolved));
            }
            calendarDateFrom = filtersState.getDateFrom();
            calendarDateTo = filtersState.getDateTo();
        }
        setDate();
    }

    /**
     * Allows filter perform filtration of relevant to checkBox kind of problems.
     *
     * @param view view of concrete checkbox.
     */
    private void setFilterOn(View view) {
        CheckBox checkBox = (CheckBox) view;
        checkBox.setChecked(false);
    }

    /**
     * Write actual date to relevant textFields on filter layout.
     *
     * @param dateFrom start date for filtration set by user.
     * @param dateTo   finish date for filtration set by user.
     */
    private void setDateOnScreen(Calendar dateFrom, Calendar dateTo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);

        TextView dateFromView = (TextView) layoutView.findViewById(R.id.date_from);
        String formattedDate = dateFormat.format(dateFrom.getTime());
        dateFromView.setText(formattedDate);

        TextView dateToView = (TextView) layoutView.findViewById(R.id.date_to);
        formattedDate = dateFormat.format(dateTo.getTime());
        dateToView.setText(formattedDate);
    }

    /**
     * Converts filter state to JSON format.
     *
     * @return filter state in JSON format.
     * @params filterState state of the filters.
     */
    private Set<String> convertToStringSet(final FilterState filterState) {
        Set<String> filterStateSet = new HashSet<>();
        for (String filterItem: filterState.getState().keySet()) {
            if (filterState.isFilterOff(filterItem)) {
                filterStateSet.add(filterItem);
            }
        }

        return filterStateSet;
    }

}
