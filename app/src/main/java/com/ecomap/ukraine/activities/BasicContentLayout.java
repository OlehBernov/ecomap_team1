package com.ecomap.ukraine.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BasicContentLayout {

    private static final int DEFAULT_TOP_MARGIN = 0;

    private LinearLayout rootLayout;
    private Context context;
    private int numberOfBlocks;
    private int currentLayoutHeight;

    public BasicContentLayout(LinearLayout rootLayout, Context context) {
        this.rootLayout = rootLayout;
        this.context = context;
        numberOfBlocks = rootLayout.getChildCount();
        setViewTreeObserver();
    }

    public void addBlock(int newViewId) {
        addBlock(newViewId, numberOfBlocks);
    }

    public void addBlock(int newViewId, int position) {
        View newView = View.inflate(context, newViewId, null);
        addBlock(newView, position);
    }

    public void addBlock(View newView) {
        addBlock(newView, numberOfBlocks);
    }

    public void addBlock(View newView, int position) {
        rootLayout.addView(newView, position, getLayoutParams(DEFAULT_TOP_MARGIN));
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
        addBlock(newView, position);
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
            }
        });
    }

    private LinearLayout.LayoutParams getLayoutParams(int topMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.topMargin = currentLayoutHeight + topMargin;

        return marginParams;
    }

}

