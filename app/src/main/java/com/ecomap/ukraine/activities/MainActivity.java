package com.ecomap.ukraine.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.LogOutListener;
import com.ecomap.ukraine.filter.FilterState;
import com.ecomap.ukraine.models.User;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andriy on 01.07.2015.
 * <p/>
 * Main activity, represent GUI and provides access to all functional
 */
public class MainActivity extends AppCompatActivity implements LogOutListener {

    /**
     * Name of the filter window.
     */
    private static final String FILTER = "Filter";

    /**
     * Name of map window.
     */
    private static final String ECOMAP = "Ecomap Ukraine";

    private static final String DATE_FROM_TAG = "Date from tag";

    private static final String DATE_TO_TAG = "Date to tag";

    private static final String FILTERS_STATE = "Filters state";

    /**
     * Drawer toggle.
     */
    private ActionBarDrawerToggle drawerToggle;

    /**
     * Filter layout.
     */
    private DrawerLayout filterLayout;

    /**
     * Application toolbar.
     */
    private Toolbar toolbar;

    private CalendarDatePickerDialog dialogDateFrom;

    private CalendarDatePickerDialog dialogDateTo;

    private Date calendarDateFrom;

    private Date calendarDateTo;

    private DataManager dataManager;

    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = DataManager.getInstance(getApplicationContext());
        dataManager.registerLogOutListener(this);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setupToolbar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);

        User user = (User) getIntent().getSerializableExtra("User");
        setUserInformation(user);

        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        filterLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.addMapFragment();

        createDateFromPickerDialog();
        createDateToPickerDialog();
        setStartDateOnScreen();
    }

    /**
     * Sync the toggle state after change application configuration.
     *
     * @param newConfig new application configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Handle action bar items.
     *
     * @param item menu item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Inflate the menu, this adds items to the action bar if it is present.
     *
     * @param menu activity menu
     * @return result of action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Controls the position of the filter7 window on the screen.
     */
    public void showFilter(MenuItem item) {
        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        if (!filterLayout.isDrawerOpen(GravityCompat.END)) {
            filterLayout.openDrawer(GravityCompat.END);
            toolbar.setTitle(FILTER);
        } else {
            buildFiltersState();
            filterLayout.closeDrawer(GravityCompat.END);
            toolbar.setTitle(ECOMAP);
        }
    }

    public void switchCheckButtonState(View view) {
        Button button = (Button) view;
        ColorDrawable buttonColor = (ColorDrawable) view.getBackground();

        if (isFilterOff(buttonColor)) {
            view.setBackgroundColor(getResources().getColor(R.color.filter_on));
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_off, 0);
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.filter_off));
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_on2, 0);
        }
    }

    public void dateFromChoosing(View view) {
        dialogDateFrom.show(getSupportFragmentManager(), DATE_FROM_TAG);
    }

    public void dateToChoosing(View view) {
        dialogDateTo.show(getSupportFragmentManager(), DATE_TO_TAG);
    }

    private void setUserInformation(User user) {
        TextView userName = (TextView) findViewById(R.id.navigation_user_name);
        TextView email = (TextView) findViewById(R.id.navigation_email);
        if (user != null) {
            userName.setText(user.getName() + " " + user.getSurname());
            email.setText(user.getEmail());
        } else {
            Log.e("logout", "here");
            userName.setText("Anonym");
            email.setText("secret@mail.com");
        }
    }

    private void setStartDateOnScreen() {
        //TODO from pref
    }

    private void setDateOnScreen(Date date) {
        //TODO
    }

    //TODO JSON
    //
   /*   @Override
    protected void onDestroy() {
      SharedPreferences settings = getApplicationContext().getSharedPreferences(FILTERS_STATE, 0);
        SharedPreferences.Editor editor = settings.edit();
        FilterState filterState = buildFiltersState();
        saveProblemsTypeFilters(filterState, editor);
        editor.putBoolean(FilterContract.RESOLVED, filterState.isShowResolvedProblem());
        editor.putBoolean(FilterContract.UNSOLVED, filterState.isShowUnsolvedProblem());

        editor.commit();
    }

 /*   private void saveDateFilters(FilterState filterState, SharedPreferences.Editor editor) {
       // editor.putInt(FilterContract.DATE_FROM_YEAR, filterState.getDateFrom().);

    }

    private void saveProblemsTypeFilters(FilterState filterState, SharedPreferences.Editor editor) {
        editor.putBoolean(FilterContract.TYPE_1, filterState.isShowProblemType1());
        editor.putBoolean(FilterContract.TYPE_2, filterState.isShowProblemType2());
        editor.putBoolean(FilterContract.TYPE_3, filterState.isShowProblemType3());
        editor.putBoolean(FilterContract.TYPE_4, filterState.isShowProblemType4());
        editor.putBoolean(FilterContract.TYPE_5, filterState.isShowProblemType5());
        editor.putBoolean(FilterContract.TYPE_6, filterState.isShowProblemType6());
        editor.putBoolean(FilterContract.TYPE_7, filterState.isShowProblemType7());
    } */

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

                calendarDateFrom = new Date(date.getTimeInMillis());
            }
        };

        dialogDateFrom = new CalendarDatePickerDialog();
        dialogDateFrom.setOnDateSetListener(dateFromListener);
    }

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

                calendarDateTo = new Date(date.getTimeInMillis());
            }
        };

        dialogDateTo = new CalendarDatePickerDialog();
        dialogDateTo.setOnDateSetListener(dateToListener);
    }

    /**
     * Sync the toggle state after onRestoreInstanceState has occurred.
     *
     * @param savedInstanceState saved application state.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private FilterState buildFiltersState() {

        boolean stateType1 = getFilterState(R.id.type1);
        boolean stateType2 = getFilterState(R.id.type2);
        boolean stateType3 = getFilterState(R.id.type3);
        boolean stateType4 = getFilterState(R.id.type4);
        boolean stateType5 = getFilterState(R.id.type5);
        boolean stateType6 = getFilterState(R.id.type6);
        boolean stateType7 = getFilterState(R.id.type7);

        boolean stateResolved = getFilterState(R.id.ButtonResolved);
        boolean stateUnsolved = getFilterState(R.id.ButtonUnsolved);

        FilterState filterState = new FilterState(stateType1, stateType2,
                stateType3, stateType4, stateType5, stateType6, stateType7,
                stateResolved, stateUnsolved, calendarDateFrom, calendarDateTo);

        return filterState;
    }

    private boolean getFilterState(int id) {
        Button button;
        ColorDrawable buttonColor;

        button = (Button) findViewById(id);
        buttonColor = (ColorDrawable) button.getBackground();

        return isFilterOff(buttonColor);
    }

    private boolean isFilterOff(ColorDrawable buttonColor) {
        return (buttonColor.getColor() ==
                getResources().getColor(R.color.filter_off));
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, FragmentEcoMap.newInstance())
                .commit();
    }

    @Override
    public void setLogOutResult(boolean success) {
        Log.e("logout", "here2" + success);
        if (success) {
            setUserInformation(null);
        }
    }

    public void logOut(MenuItem item) {
        Log.e("logout", "here");
        dataManager.logOutUser();
    }
}
