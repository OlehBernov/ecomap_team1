package com.ecomap.ukraine.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Layout for full problem information which allows easy to add new blocks or
 * swap existing blocks.
 */
public class BasicContentLayout {

    private static final int DEFAULT_TOP_MARGIN = 0;
    private static final int DEFAULT_LEFT_MARGIN = 25;

    private ViewGroup root;

    public BasicContentLayout(ViewGroup root) {
        this.root = root;
    }

    /**
     * Adds vertical block to the bottom of the layout.
     *
     * @param newView new block.
     */
    public void addVerticalBlock(final View newView) {
        addVerticalBlock(newView, root.getChildCount());
    }

    /**
     * Adds vertical block to the concrete position of the layout.
     *
     * @param newView  new block.
     * @param position new block position.
     */
    public void addVerticalBlock(final View newView, final int position) {
        root.addView(newView, position, getVerticalLayoutParams(DEFAULT_TOP_MARGIN));
    }

    /**
     * Adds horizontal block to the bottom of the layout.
     *
     * @param newView new block.
     */
    public void addHorizontalBlock(final View newView) {
        addHorizontalBlock(newView, DEFAULT_LEFT_MARGIN);
    }

    /**
     * Adds horizontal block to the bottom of the layout and sets the concrete margin to the
     * top of this block.
     *
     * @param newView new block.
     * @param margin  new block margin.
     */
    public void addHorizontalBlock(final View newView, final int margin) {
        root.addView(newView, root.getChildCount(), getHorizontalLayoutParams(margin));
    }

    /**
     * Removes block from the layout.
     *
     * @param block block which will removed.
     */
    public void removeBlock(View block) {
        if (block != null) {
            root.removeView(block);
        }
    }

    /**
     * Removes all blocks from the layout.
     */
    public void removeAllBlocks() {
        root.removeAllViews();
    }

    /**
     * Returns the number of blocks in layout.
     *
     * @return number of blocks.
     */
    public int getNumberOfBlocks() {
        return root.getChildCount();
    }

    /**
     * Creates layout params for vertical blocks according to topMargin.
     *
     * @param topMargin top margin.
     * @return layout params for vertical block.
     */
    private LinearLayout.LayoutParams getVerticalLayoutParams(final int topMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.topMargin = topMargin;

        return marginParams;
    }

    /**
     * Creates layout params for horizontal blocks according to topMargin.
     *
     * @param leftMargin left margin.
     * @return layout params for vertical block.
     */
    private LinearLayout.LayoutParams getHorizontalLayoutParams(final int leftMargin) {
        LinearLayout.LayoutParams marginParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginParams.leftMargin = leftMargin;

        return marginParams;
    }

}

