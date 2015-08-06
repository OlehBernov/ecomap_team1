package com.ecomap.ukraine.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BasicContentLayout {

    private static final int DEFAULT_TOP_MARGIN = 0;
    private static final int DEFAULT_LEFT_MARGIN = 0;

    private LinearLayout rootLayout;
    private Context context;
    private int numberOfBlocks;
    private int currentLayoutHeight;
    private int getCurrentLayoutWidth;

    public BasicContentLayout(LinearLayout rootLayout, Context context) {
        this.rootLayout = rootLayout;
        this.context = context;
        numberOfBlocks = rootLayout.getChildCount();
        setViewTreeObserver();
    }

    public void addVerticalBlock(View newView) {
        addVerticalBlock(newView, numberOfBlocks);
    }

    public void addVerticalBlock(View newView, int position) {
        rootLayout.addView(newView, position, getVerticalLayoutParams(DEFAULT_TOP_MARGIN));
        numberOfBlocks++;
    }

    public void addHorizontallBlock(View newView) {
        addHorizontalBlock(newView, DEFAULT_LEFT_MARGIN);
    }

    public void addHorizontalBlock(View view, int margin) {
        rootLayout.addView(view, numberOfBlocks, getHorizontalLayoutParams(margin));
        numberOfBlocks++;
    }

    public void addText(String text) {
        addText(text, numberOfBlocks);
    }

    public void addText(String text, int position) {
        TextView newTextView = new TextView(context);
        newTextView.append(text);
        LinearLayout newView = new LinearLayout(context);
        newView.addView(newTextView);
        addVerticalBlock(newView, position);
    }

    public View getResultView() {
        return rootLayout;
    }

    private void setViewTreeObserver() {
        ViewTreeObserver vto = rootLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentLayoutHeight = rootLayout.getMeasuredHeight();
                getCurrentLayoutWidth = rootLayout.getMeasuredWidth();

            }
        });
    }

    private LinearLayout.LayoutParams getVerticalLayoutParams(int topMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.topMargin = currentLayoutHeight + topMargin;

        return marginParams;
    }

    private LinearLayout.LayoutParams getHorizontalLayoutParams(int leftMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.leftMargin = getCurrentLayoutWidth + leftMargin;

        return marginParams;
    }

}

