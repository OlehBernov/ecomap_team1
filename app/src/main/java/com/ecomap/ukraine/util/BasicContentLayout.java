package com.ecomap.ukraine.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;


public class BasicContentLayout extends LinearLayout {

    private static final int DEFAULT_TOP_MARGIN = 0;
    private static final int DEFAULT_LEFT_MARGIN = 25;

    private int numberOfBlocks;
    private int currentLayoutHeight;
    private int currentLayoutWidth;
    private LinearLayout root;

    public BasicContentLayout(Context context) {
        this(context, null);
    }

    public BasicContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasicContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    //TODO: need to solve the problem
    public void setCrutch(LinearLayout pain) {
        root = pain;
    }

    public void addVerticalBlock(final View newView) {
        addVerticalBlock(newView, numberOfBlocks);
    }

    public void addVerticalBlock(final View newView, final int position) {
        root.addView(newView, position, getVerticalLayoutParams(DEFAULT_TOP_MARGIN));
        numberOfBlocks++;
    }

    public void addHorizontalBlock(final View newView) {
        addHorizontalBlock(newView, DEFAULT_LEFT_MARGIN);
    }

    public void addHorizontalBlock(final View view, final int margin) {
        addView(view, numberOfBlocks, getHorizontalLayoutParams(margin));
        numberOfBlocks++;
    }

    public void removeBlock(View block) {
        if (block != null) {
            root.removeView(block);
            numberOfBlocks--;
        }
    }

    public void removeAllBlocks() {
        root.removeAllViews();
        numberOfBlocks = 0;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    private void init() {
        setViewTreeObserver();
    }

    private void setViewTreeObserver() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                currentLayoutHeight = getMeasuredHeight();
                currentLayoutWidth = getMeasuredWidth();
            }
        });
    }

    private LinearLayout.LayoutParams getVerticalLayoutParams(final int topMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.topMargin = currentLayoutHeight + topMargin;

        return marginParams;
    }

    private LinearLayout.LayoutParams getHorizontalLayoutParams(final int leftMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.leftMargin = currentLayoutWidth + leftMargin;

        return marginParams;
    }

}

