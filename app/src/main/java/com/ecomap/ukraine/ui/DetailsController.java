package com.ecomap.ukraine.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.ui.activities.ChooseProblemLocationActivity;
import com.ecomap.ukraine.ui.activities.MainActivity;
import com.ecomap.ukraine.ui.fullinfo.DetailsContentContent;
import com.ecomap.ukraine.util.Keyboard;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class DetailsController {
    private static final float ANCHOR_POINT = 0.3f;
    private static final int ADD_PROBLEM_BUTTON = 1;
    private static final int REFRESH_BUTTON = 2;

    private static final float COLLAPSE_OFFSET = 0.9F;
    private static int MAIN_COLOR;
    private static final int MAX_ALPHA = 255;
    private static float currentTitleAlpha = 1;

    private Activity activity;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private LinearLayout layout;
    private FloatingActionButton fab;
    private TextView titleView;
    private boolean isKeyboardShown;

    private DetailsContentContent detailsContentContent;
    private Problem problem;

    private int fabState;
    private float currentOffset;
    private boolean isScrollDisable;

    public DetailsController(Activity activity, Problem problem, DetailsContentContent detailsContentContent) {
        this.activity = activity;
        this.detailsContentContent = detailsContentContent;
        this.problem = problem;

        detailsContentContent.setBaseInfo(problem);

        slidingUpPanelLayout = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        scrollView = (ScrollView) activity.findViewById(R.id.panelScrollView);
        titleView = (TextView) activity.findViewById(R.id.details_title);
        fab = (FloatingActionButton) activity.findViewById(R.id.fab2);

        MAIN_COLOR = activity.getResources().getColor(R.color.toolbar_teal);
        toolbar.setBackgroundColor(MAIN_COLOR);

        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        final View rootView = activity.findViewById(R.id.root_view);
        rootView.getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                    if (heightDiff > 100) {
                        if (!isKeyboardShown) {
                            isKeyboardShown = true;
                            newFabPosition(currentOffset);
                        }
                    } else if (isKeyboardShown) {
                        isKeyboardShown = false;
                        newFabPosition(currentOffset);
                    }
                }
            });

        setSlidingControl();
    }

    public void setSlidingControl() {
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            public static final int TRANSPARENT_WHITE_COLOR = 0x00ffffff;

            @Override
            public void onPanelSlide(View view, float v) {
                slidingUpPanelLayout.setCoveredFadeColor(TRANSPARENT_WHITE_COLOR);
                rescaleText(v);
                animateMenuIcon(v);
                newFabPosition(v);
                if (v > slidingUpPanelLayout.getAnchorPoint()) {
                    slidingUpPanelLayout.setCoveredFadeColor(MAIN_COLOR);
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
                slidingUpPanelLayout.setCoveredFadeColor(TRANSPARENT_WHITE_COLOR);
                scrollView.setScrollY(0);
                setToolbarInitialState();
                isScrollDisable = true;
            }

            @Override
            public void onPanelHidden(View view) {
            }

            public void backgroundResolver(final float v) {
                float anchor = slidingUpPanelLayout.getAnchorPoint();
                int alpha = (int) (MAX_ALPHA * Math.max((COLLAPSE_OFFSET - v)
                        / (COLLAPSE_OFFSET - anchor), 0));
                alpha = Math.min(alpha, MAX_ALPHA);
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
                int alphaChanel = (int) (alpha * MAX_ALPHA) * 0x01000000;
                int newColorCode = TRANSPARENT_WHITE_COLOR | alphaChanel;
                toolbar.setTitleTextColor(newColorCode);
            }

            private void changeFilterIconTransparency(final float alpha) {
                MenuItem item = toolbar.getMenu().findItem(R.id.action_find_location);
                item.getIcon().setAlpha((int) (MAX_ALPHA * alpha));
            }


            private void setToolbarInitialState() {
                toolbar.setBackgroundColor(MAIN_COLOR);
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
                float scale = Math.max(v - 0.9F, 0) * 3;
                titleView.setScaleX(1 - scale);
                titleView.setScaleY(1 - scale);
            }

        });

        isScrollDisable = true;

        scrollView.setScrollY(0);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            float previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    previousY = event.getY();
                    return false;
                }
                if (((scrollView.getScrollY() == 0) && draggingDown(event))
                        || (slidingUpPanelLayout.getPanelState()
                        != SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    if (isKeyboardShown) {
                        Keyboard.hideKeyboard(scrollView);
                        return false;
                    }
                    slidingUpPanelLayout.onTouchEvent(remapToParentLayout(event));
                    return true;
                }
                return isScrollDisable;
            }

            private boolean draggingDown(MotionEvent event) {
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

        });

    }

    private void newFabPosition(final float v) {
        float edgeY = convertToPixels(1 - v) + toolbar.getHeight();
        float fabDefaultCenterY = (fab.getY() + (fab.getHeight() / 2)) - fab.getTranslationY();
        if (edgeY < fabDefaultCenterY) {
            if (fabState != REFRESH_BUTTON) {
                morphToRefreshButton();
            }
            fab.setY(edgeY - (fab.getHeight() / 2));
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
                ObjectAnimator animator = ObjectAnimator.ofFloat(fab, "rotation", 360);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.setRotation(0);
                        DataManager.getInstance(activity).
                                refreshProblemDetails(problem.getProblemId());
                    }
                });
                animator.start();
            }
        });
    }

    private void morphToAddButton() {
        fab.setImageResource(R.drawable.ic_add_white_24dp);
        fab.setBackgroundTintList(ColorStateList.valueOf(MAIN_COLOR));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(
                        new Intent(activity, ChooseProblemLocationActivity.class));
            }
        });
        fab.setTranslationY(0);
    }

    private int convertToPixels(float value) {
        return (int) (value * slidingUpPanelLayout.getHeight());
    }
}
