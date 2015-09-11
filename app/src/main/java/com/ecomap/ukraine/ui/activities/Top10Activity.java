package com.ecomap.ukraine.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Top10Item;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.problemupdate.manager.ProblemListener;
import com.ecomap.ukraine.ui.SlidingTabLayout;
import com.ecomap.ukraine.ui.adapters.ViewPagerAdapter;
import com.ecomap.ukraine.ui.fragments.Top10ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriy on 09.09.2015.
 */
public class Top10Activity extends AppCompatActivity implements ProblemListener {

    private static final int NUMBER_OF_TABS = 3;

    private ViewPager pager;
    private AllTop10Items allTop10Items;
    private List<Problem> problems;
    SlidingTabLayout tabs;
    String[] titles;


    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataManager dataManager = DataManager.getInstance(this);

        dataManager.registerProblemListener(this);
        dataManager.getTop10Items();
        dataManager.getAllProblems();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setupToolbar();

        titles = getResources().getStringArray(R.array.tabs_in_top10);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        Top10ViewPagerAdapter adapter = new Top10ViewPagerAdapter(getSupportFragmentManager(), titles,
                NUMBER_OF_TABS);

        pager.setAdapter(adapter);
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
     * Inflate the menu, this adds items to the action bar if it is present.
     *
     * @param menu activity menu
     * @return result of action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_problem_photo_slide_pager, menu);
        return true;
    }

    /**
     * Called when the user selects an item from the options menu
     *
     * @param item menu item
     * @return result of action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void updateAllProblems(List<Problem> problems) {
        this.problems = problems;
    }

    @Override
    public void updateProblemDetails(Details details) {
    }

    @Override
    public void updateTop10(AllTop10Items allTop10Items) {
        this.allTop10Items = allTop10Items;
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(getString(R.string.TOP_10));
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


    private class Top10ViewPagerAdapter extends ViewPagerAdapter {

        /**
         * Constructor
         */
        public Top10ViewPagerAdapter(FragmentManager fragmentManager, String[] titles, int numbOfTabs) {
            super(fragmentManager, titles, numbOfTabs);
        }

        /**
         * Get tab activity by position
         *
         * @param position position of tab
         * @return tab activity
         */
        @Override
        public Fragment getItem(int position) {
            Top10ListFragment top10ListFragment = new Top10ListFragment();
            top10ListFragment.setProblems(problems);
            if(allTop10Items == null) {
                List<Top10Item> top10ItemList = new ArrayList<>();
                allTop10Items = new AllTop10Items(
                        top10ItemList, top10ItemList, top10ItemList);
                Toast.makeText(getApplicationContext(), "Error of loading try again.", Toast.LENGTH_LONG)
                        .show();
            }
            switch (position) {
                case 0:
                    top10ListFragment.setIconID(R.drawable.like_iconq);
                    top10ListFragment.setTop10ItemList(allTop10Items.getMostLikedProblems());
                    return top10ListFragment;
                case 1:
                    top10ListFragment.setIconID(R.drawable.comment3);
                    top10ListFragment.setTop10ItemList(allTop10Items.getMostPopularProblems());
                    return top10ListFragment;
                case 2:
                    top10ListFragment.setIconID(R.drawable.ic_star_black_48dp);
                    top10ListFragment.setTop10ItemList(allTop10Items.getMostImportantProblems());
                    return top10ListFragment;
            }
            return null;
        }
    }
}

