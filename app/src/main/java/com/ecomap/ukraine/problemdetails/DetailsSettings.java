package com.ecomap.ukraine.problemdetails;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.problemposting.ChooseProblemLocationActivity;
import com.ecomap.ukraine.MainActivity;
import com.ecomap.ukraine.model.Problem;
import com.ecomap.ukraine.helper.Refresher;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class DetailsSettings {
    private static final float ANCHOR_POINT = 0.3f;
    private static final int ADD_PROBLEM_BUTTON = 1;
    private static final int REFRESH_BUTTON = 2;

    private static final float COLLAPSE_OFFSET = 0.9F;
    private static float currentTitleAlpha = 1;

    private Activity activity;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private LinearLayout layout;
    private FloatingActionButton fab;
    private TextView titleView;

    private Problem problem;

    private int fabState;
    private float currentOffset;
    private boolean isScrollDisable;

    public DetailsSettings(Activity activity, Problem problem) {
        this.activity = activity;

        slidingUpPanelLayout = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        scrollView = (ScrollView) activity.findViewById(R.id.panelScrollView);
        titleView = (TextView) activity.findViewById(R.id.details_title);
        fab = (FloatingActionButton) activity.findViewById(R.id.fab2);

        this.problem = problem;

        this.toolbar.setBackgroundColor(0xff004d40);

        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    public void perform() {
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            public static final int TRANSPARENT_WHITE_COLOR = 0x00ffffff;

            @Override
            public void onPanelSlide(View view, float v) {
                slidingUpPanelLayout.setCoveredFadeColor(TRANSPARENT_WHITE_COLOR);
                rescaleText(v);
                animateMenuIcon(v);
                newFabPosition(v);
                if (v > slidingUpPanelLayout.getAnchorPoint()) {
                    slidingUpPanelLayout.setCoveredFadeColor(0xff004d40);
                    backgroundResolver(v);
                }
                if (v > COLLAPSE_OFFSET) {
                    titleTransforming(v);
                }
                currentOffset = v;
            }

            @Override
            public void onPanelCollapsed(View view) {
                setToolbarInitialState();
                toolbar.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onPanelExpanded(View view) {
                toolbar.getMenu().findItem(R.id.action_find_location).setEnabled(false);
                scrollView.setVerticalScrollBarEnabled(true);
                isScrollDisable = false;
            }

            @Override
            public void onPanelAnchored(View view) {
                scrollView.setVerticalScrollBarEnabled(false);
                slidingUpPanelLayout.setCoveredFadeColor(0x00000000);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                setToolbarInitialState();
                isScrollDisable = true;
            }

            @Override
            public void onPanelHidden(View view) {
            }


            public void backgroundResolver(final float v) {
                float anchor = slidingUpPanelLayout.getAnchorPoint();
                int alpha = (int) (255 * Math.max((COLLAPSE_OFFSET - v)
                        / (COLLAPSE_OFFSET - anchor), 0));
                alpha = Math.min(alpha, 255);
                layout = (LinearLayout) activity.findViewById(R.id.sliding_linear_layout);
                toolbar.getBackground().setAlpha(alpha);
                layout.getBackground().setAlpha(alpha);
            }

            public void titleTransforming(final float v) {
                currentTitleAlpha = calculateAlpha(v);
                changeTitleTransparency(currentTitleAlpha);
                changeFilterIconTransparency(currentTitleAlpha);
            }

            private float calculateAlpha(final float v) {
                return Math.min((1 - v) / (1 - COLLAPSE_OFFSET), 1);
            }

            private void animateMenuIcon(final float v) {
                MainActivity mainActivity = (MainActivity) activity;
                ActionBarDrawerToggle drawerToggle = mainActivity.drawerToggle;
                drawerToggle.onDrawerSlide(null, 1 - calculateAlpha(v));
                if (v == 1) {
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onBackPressed();
                        }
                    });
                } else {
                    final DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }

            private void changeTitleTransparency(final float alpha) {
                int alphaChanel = (int) (alpha * 255) * 0x01000000;
                int newColorCode = TRANSPARENT_WHITE_COLOR | alphaChanel;
                toolbar.setTitleTextColor(newColorCode);
            }

            private void changeFilterIconTransparency(final float alpha) {
                MenuItem item = toolbar.getMenu().findItem(R.id.action_find_location);
                item.getIcon().setAlpha((int) (255 * alpha));
            }

            private void newFabPosition(final float v) {
                float edgeY = convertToPixels(1 - v) + toolbar.getHeight();
                float fabDefaultCenterY = fab.getY() + fab.getHeight() / 2 - fab.getTranslationY();
                if (edgeY < fabDefaultCenterY) {
                    if (fabState != REFRESH_BUTTON) {
                        morphToRefreshButton();
                    }
                    fab.setTranslationY(edgeY - fabDefaultCenterY);
                } else {
                    if (fabState != ADD_PROBLEM_BUTTON) {
                        morphToAddButton();
                    }
                }
            }

            private void morphToRefreshButton() {
                fab.setImageResource(R.drawable.ic_refresh_white_24dp);
                fab.setBackgroundTintList(ColorStateList.valueOf(0xff440044));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Refresher.setRefreshTask(activity, problem);
                    }
                });
            }

            private void morphToAddButton() {
                fab.setImageResource(R.drawable.ic_add_white_24dp);
                fab.setBackgroundTintList(ColorStateList.valueOf(0xff004d40));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(
                                new Intent(activity, ChooseProblemLocationActivity.class));
                    }
                });
                fab.setTranslationY(0);
            }

            private float getFabCenterY() {
                return fab.getY() + fab.getHeight() / 2;
            }


            private void setToolbarInitialState() {
                toolbar.setBackgroundColor(0xff004d40);
                if (currentTitleAlpha != 1) {
                    ValueAnimator animator = ValueAnimator.ofFloat(currentTitleAlpha, 1);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float alpha = (Float) animation.getAnimatedValue();
                            changeFilterIconTransparency(alpha);
                            changeTitleTransparency(alpha);
                            currentTitleAlpha = alpha;
                        }
                    });
                    animator.setInterpolator(new DecelerateInterpolator(1));
                    animator.start();
                }
                toolbar.getMenu().findItem(R.id.action_find_location).setEnabled(true);
            }

            private void rescaleText(float v) {
                float scale = (Math.max(v - 0.9F, 0) * 3);
                titleView.setScaleX(1 - scale);
                titleView.setScaleY(1 - scale);
            }

            private void moveToolbar(float v) {
                if ((v > slidingUpPanelLayout.getAnchorPoint())) {
                    setToolbarInitialState();
                    slidingUpPanelLayout.setCoveredFadeColor(0xff004d40);
                    if (convertToPixels(v) < (convertToPixels(slidingUpPanelLayout
                            .getAnchorPoint() + toolbar.getHeight()))) {
                        toolbar.setY(convertToPixels(slidingUpPanelLayout
                                .getAnchorPoint()) - convertToPixels(v));
                    } else {
                        toolbar.setY(-toolbar.getHeight());
                    }
                }
            }

            private int convertToPixels(float value) {
                return (int) (value * slidingUpPanelLayout.getHeight());
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        slidingUpPanelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        isScrollDisable = true;

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            float previousY;

            private boolean dragingDown(MotionEvent event) {
                if (previousY < event.getY()) {
                    previousY = event.getY();
                    return true;
                } else {
                    previousY = event.getY();
                    return false;
                }
            }

            private MotionEvent remapToParentLayout(MotionEvent event) {
                return MotionEvent.obtain(
                        event.getDownTime(),
                        event.getEventTime(),
                        event.getAction(),
                        event.getRawX(),
                        event.getRawY(),
                        event.getMetaState()
                );
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    previousY = event.getY();
                    return false;
                }
                if (((scrollView.getScrollY() == 0) && dragingDown(event))
                        || (slidingUpPanelLayout.getPanelState()
                        != SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    slidingUpPanelLayout.onTouchEvent(remapToParentLayout(event));
                    return true;
                }
                return isScrollDisable;
            }
        });

    }
}