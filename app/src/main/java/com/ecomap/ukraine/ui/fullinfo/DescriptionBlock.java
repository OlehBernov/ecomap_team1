package com.ecomap.ukraine.ui.fullinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ms.square.android.expandabletextview.ExpandableTextView;


public class DescriptionBlock extends LinearLayout {

    public DescriptionBlock(Context context) {
        this(context, null);
    }

    public DescriptionBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DescriptionBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setDescription(Details details) {
        ExpandableTextView descriptionFiled = (ExpandableTextView) findViewById(R.id.description_field);
        descriptionFiled.setText(details.getContent());
    }

    /**
     * Initiates the view.
     */
    private void init(Context context) {
        inflate(context, R.layout.problem_description, this);
    }

}
