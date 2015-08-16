package com.ecomap.ukraine.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.PathInterpolatorCompat;
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
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.filter.Filter;
import com.ecomap.ukraine.filter.FilterContract;
import com.ecomap.ukraine.filter.FilterManager;
import com.ecomap.ukraine.filter.FilterState;
import com.ecomap.ukraine.filter.FilterStateConverter;
import com.ecomap.ukraine.models.User;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Andriy on 01.07.2015.
 * <p/>
 * Main activity, represent GUI and provides access to all functional
 */
public class MainActivity extends AppCompatActivity {

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

    private static final String ANONYM_USER_NAME = "Anonym";

    private static final String ANONYM_USER_EMAIL = "secret@gmail.com";

    private static final String DATE_TEMPLATE = "dd-MM-yyyy";

    private static final String DEFAULT_DATE_FROM = "01-01-1990";

    private static final String DEFAULT_DATE_TO = "31-12-2030";

    private static final String USER = "User";


    final private String alertMessage =
            "To append a problem you must first be authorized. Authorize?";

    final static   String OK = "OK";
    final static   String CANCEL = "Cancel";

    /**
     * Drawer toggle.
     */
    public ActionBarDrawerToggle drawerToggle;

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

    private Calendar calendarDateFrom;

    private Calendar calendarDateTo;

    private DataManager dataManager;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private  User user;

    private Menu menu;

    private FloatingActionButton fab;

    private Activity activity = this;

    /**
     * Filter manager instance
     */
    private FilterManager filterManager;
    private CharSequence previousTitle;

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

        fab = (FloatingActionButton)findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openChooseProblemLocationActivity(v);
                animateButton(fab);
            }

        });
        filterManager = FilterManager.getInstance(this);
        setupToolbar();
        setUpDrawerLayout();
        setupFilter();

        if(user == null) {
            user = (User) getIntent().getSerializableExtra(USER);
        }
        setUserInformation(user);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setAnchorPoint(0.5f);
        slidingUpPanelLayout.setDragView(R.id.sliding_linear_layout);

        this.addMapFragment();

        createDateFromPickerDialog();
        createDateToPickerDialog();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        this.menu = menu;
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences settings = getApplicationContext().getSharedPreferences(FILTERS_STATE, 0);
        SharedPreferences.Editor editor = settings.edit();
        FilterState filterState = buildFiltersState();
        try {
            editor.putString(FILTERS_STATE, new FilterStateConverter().convertToJson(filterState));
        } catch (JSONException e) {
            Log.e("JSONException", "onDestroy");
        }
        editor.apply();
    }

    public void logOut(MenuItem item) {
        //TODO
    }

    @Override
    public void onBackPressed() {
        if (filterLayout.isDrawerOpen(GravityCompat.END)) {
            menu.findItem(R.id.action_find_location)
                    .setIcon(R.drawable.filter8);
            filterLayout.closeDrawer(GravityCompat.END);
            toolbar.setTitle(previousTitle);
        } else if (slidingUpPanelLayout.getPanelState()
                == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout
                    .PanelState.ANCHORED);
        } else if (slidingUpPanelLayout.getPanelState()
                != SlidingUpPanelLayout.PanelState.COLLAPSED ) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout
                    .PanelState.HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    private void animateButton(final FloatingActionButton fab) {
        Interpolator curveInterpolator = PathInterpolatorCompat.create(1, 0);
        AnimatorSet animator = new AnimatorSet();
        ObjectAnimator movementX
                = ObjectAnimator.ofFloat(fab, "x", fab.getX() - 120);
        ObjectAnimator movementY
                = ObjectAnimator.ofFloat(fab, "y", fab.getY() + 20);
        movementX.setInterpolator(curveInterpolator);
        animator.playTogether(movementX, movementY);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animateReavel();
                fab.animate().alpha(0).start();
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fab.setClickable(false);
            }
        });
        animator.start();
    }

    private void animateReavel() {
        final View myView = findViewById(R.id.ll_reveal);
        int cy = (myView.getTop() + myView.getBottom()) / 2;
        int cx = 260 + fab.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                myView.setVisibility(View.VISIBLE);
                myView.setClickable(true);
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {}

            @Override
            public void onAnimationRepeat() {}
        });
        animator.start();
    }

    public void refresh(MenuItem item) {
        DataManager.getInstance(this).refreshAllProblems();
    }

    public void reverseAnimateReavel(View v) {
        final View myView = findViewById(R.id.ll_reveal);

        int cy = (myView.getTop() + myView.getBottom()) / 2;
        int cx = (int)fab.getX() + fab.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, fab.getHeight(), finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);
        SupportAnimator reversedAnimator = animator.reverse();
        reversedAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                fab.animate().alpha(1).setDuration(50).start();
                myView.setClickable(false);

            }

            @Override
            public void onAnimationEnd() {
                fab.setVisibility(View.VISIBLE);
                fab.animate().alpha(1).start();
                myView.setVisibility(View.INVISIBLE);
                showButton(fab);
            }

            @Override
            public void onAnimationCancel() {}

            @Override
            public void onAnimationRepeat() {}
        });
        reversedAnimator.start();

    }

    private void showButton(final FloatingActionButton fab) {
        Interpolator curveInterpolator = PathInterpolatorCompat.create(0, 1);
        AnimatorSet animator = new AnimatorSet();
        ObjectAnimator movementX
                = ObjectAnimator.ofFloat(fab, "x", fab.getX() + 120);
        ObjectAnimator movementY
                = ObjectAnimator.ofFloat(fab, "y", fab.getY() - 20);
        movementX.setInterpolator(curveInterpolator);
        animator.playTogether(movementX, movementY);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fab.setClickable(true);
            }
        });
        animator.start();
    }


    /**
     * Controls the position of the filter7 window on the screen.
     */
    public void showFilter(MenuItem item) {
        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        if (!filterLayout.isDrawerOpen(GravityCompat.END)) {
            item.setIcon(R.drawable.filter_back);
            setDate();
            filterLayout.openDrawer(GravityCompat.END);
            previousTitle = toolbar.getTitle();
            toolbar.setTitle(FILTER);
        } else {
            item.setIcon(R.drawable.filter8);
            filterLayout.closeDrawer(GravityCompat.END);
            toolbar.setTitle(previousTitle);
        }
    }

    public void switchCheckButtonState(View view) {
        ColorDrawable buttonColor = (ColorDrawable) view.getBackground();
        if (isFilterOff(buttonColor)) {
            setFilterOn(view);
        } else {
            setFilterOff(view);
        }
        filterManager.getFilterState(buildFiltersState());
    }

    public void dateFromChoosing(View view) {
        dialogDateFrom.show(getSupportFragmentManager(), DATE_FROM_TAG);
    }

    public void dateToChoosing(View view) {
        dialogDateTo.show(getSupportFragmentManager(), DATE_TO_TAG);
    }

    private void setDateOnScreen(Calendar dateFrom, Calendar dateTo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);

        TextView dateFromView = (TextView) findViewById(R.id.date_from);
        String formattedDate = dateFormat.format(dateFrom.getTime());
        dateFromView.setText(formattedDate);

        TextView dateToView = (TextView) findViewById(R.id.date_to);
        formattedDate = dateFormat.format(dateTo.getTime());
        dateToView.setText(formattedDate);
    }

    private void setFilterOn(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.filter_on));
        ((Button) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.off, 0);
    }

    private void setFilterOff(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.filter_off));
        ((Button) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.on, 0);
    }

    private void setUserInformation(User user) {
        TextView userName = (TextView) findViewById(R.id.navigation_user_name);
        TextView email = (TextView) findViewById(R.id.navigation_email);
        if (user != null) {
            userName.setText(user.getName() + " " + user.getSurname());
            email.setText(user.getEmail());
        } else {
            userName.setText(ANONYM_USER_NAME);
            email.setText(ANONYM_USER_EMAIL);
        }
    }

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

                calendarDateFrom = date;
                setDate();

                filterManager.getFilterState(buildFiltersState());
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
                calendarDateTo = date;
                setDate();

                filterManager.getFilterState(buildFiltersState());
            }
        };

        dialogDateTo = new CalendarDatePickerDialog();
        dialogDateTo.setOnDateSetListener(dateToListener);
    }

    private FilterState buildFiltersState() {
        Map<String, Boolean> filterStateValues = new HashMap<>();
        filterStateValues.put(FilterContract.FOREST_DESTRUCTION,
                getFilterState(R.id.type1));
        filterStateValues.put(FilterContract.RUBBISH_DUMP,
                getFilterState(R.id.type2));
        filterStateValues.put(FilterContract.ILLEGAL_BUILDING,
                getFilterState(R.id.type3));
        filterStateValues.put(FilterContract.WATER_POLLUTION,
                getFilterState(R.id.type4));
        filterStateValues.put(FilterContract.THREAD_TO_BIODIVERSITY,
                getFilterState(R.id.type5));
        filterStateValues.put(FilterContract.POACHING,
                getFilterState(R.id.type6));
        filterStateValues.put(FilterContract.OTHER,
                getFilterState(R.id.type7));
        filterStateValues.put(FilterContract.RESOLVED,
                getFilterState(R.id.ButtonResolved));
        filterStateValues.put(FilterContract.UNSOLVED,
                getFilterState(R.id.ButtonUnsolved));

        return new FilterState(filterStateValues, calendarDateFrom, calendarDateTo);
    }

    private boolean isFilterOff(ColorDrawable buttonColor) {
        return (buttonColor.getColor() ==
                getResources().getColor(R.color.filter_off));
    }

    /**
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, FragmentEcoMap.newInstance(this, fragmentManager))
                .commit();
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

    private void setUpDrawerLayout() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
    }

    private boolean getFilterState(int id) {
        Button button;
        ColorDrawable buttonColor;

        button = (Button) findViewById(id);
        buttonColor = (ColorDrawable) button.getBackground();

        return isFilterOff(buttonColor);
    }

    /**
     * Adds google map
     */
    private void setupFilter() {
        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        filterLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        FilterState filterState = filterManager.getFilterStateFromPreference();
        setFiltersState(filterState);
    }

    private void setDate() {
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
            //TODO
            Log.e("parseException", "setDate to");
            return;
        }

        setDateOnScreen(calendarDateFrom, calendarDateTo);
    }

    private void setFiltersState(FilterState filtersState) {
        if (filtersState != null) {
            if (!filtersState.isFilterOff(FilterContract.FOREST_DESTRUCTION)) {
                setFilterOn(findViewById(R.id.type1));
            }
            if (!filtersState.isFilterOff(FilterContract.RUBBISH_DUMP)) {
                setFilterOn(findViewById(R.id.type2));
            }
            if (!filtersState.isFilterOff(FilterContract.ILLEGAL_BUILDING)) {
                setFilterOn(findViewById(R.id.type3));
            }
            if (!filtersState.isFilterOff(FilterContract.WATER_POLLUTION)) {
                setFilterOn(findViewById(R.id.type4));
            }
            if (!filtersState.isFilterOff(FilterContract.THREAD_TO_BIODIVERSITY)) {
                setFilterOn(findViewById(R.id.type5));
            }
            if (!filtersState.isFilterOff(FilterContract.POACHING)) {
                setFilterOn(findViewById(R.id.type6));
            }
            if (!filtersState.isFilterOff(FilterContract.OTHER)) {
                setFilterOn(findViewById(R.id.type7));
            }
            if (!filtersState.isFilterOff(FilterContract.RESOLVED)) {
                setFilterOn(findViewById(R.id.ButtonResolved));
            }
            if (!filtersState.isFilterOff(FilterContract.UNSOLVED)) {
                setFilterOn(findViewById(R.id.ButtonUnsolved));
            }
            calendarDateFrom = filtersState.getDateFrom();
            calendarDateTo = filtersState.getDateTo();
        }
        setDate();
    }

    public void openChooseProblemLocationActivity(View view) {

        if(user == null) {
            setNotAutorizeDialog();
        }
        else {
            Intent intent = new Intent(this, ChooseProblemLocationActivity.class);
            intent.putExtra("User", user);
            startActivity(intent);
            finish();
        }

    }


    public void setNotAutorizeDialog() {
        final boolean result = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.Caution);
        builder.setMessage(alertMessage);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder.setPositiveButton(OK,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        Intent mainIntent = new Intent(activity, LoginScreen.class);
                        startActivity(mainIntent);
                        finish();
                    }
                });
        builder.setNegativeButton(CANCEL,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
