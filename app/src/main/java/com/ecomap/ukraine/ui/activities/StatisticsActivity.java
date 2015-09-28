package com.ecomap.ukraine.ui.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Statistics;
import com.ecomap.ukraine.ui.SlidingTabLayout;
import com.ecomap.ukraine.ui.adapters.StatisticsPagerAdapter;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;

/**
 * Activity for displaying statistics information about problem posting.
 */
public class StatisticsActivity extends AppCompatActivity {

    /**
     * Number of periods for statistics.
     */
    private static final int NUMBER_OF_TUBS = 5;

    /**
     * Creates activity and sets necessary adapter and layout for displaying statistics.
     *
     * @param savedInstanceState saved state of activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        View progressView = findViewById(R.id.progress_view_on_statistics);

        setupToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String[] titles = getResources().getStringArray(R.array.tabs_in_statistic);
        StatisticsPagerAdapter adapter =
                new StatisticsPagerAdapter(getSupportFragmentManager(), titles, NUMBER_OF_TUBS,
                                           getApplicationContext(), progressView);
        ViewPager pager = (ViewPager) findViewById(R.id.statistic_pager);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.statistic_tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });

        tabs.setViewPager(pager);
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setClickable(true);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();

        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}