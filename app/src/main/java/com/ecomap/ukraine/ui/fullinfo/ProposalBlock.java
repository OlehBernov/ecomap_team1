package com.ecomap.ukraine.ui.fullinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ms.square.android.expandabletextview.ExpandableTextView;


public class ProposalBlock extends LinearLayout {

    public ProposalBlock(Context context) {
        this(context, null);
    }

    public ProposalBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProposalBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setProposal(Details details) {
        ExpandableTextView proposalFiled = (ExpandableTextView) findViewById(R.id.proposal_field);
        proposalFiled.setText(details.getProposal());
    }

    /**
     * Initiates the view.
     */
    private void init(Context context) {
        inflate(context, R.layout.problem_proposal, this);
    }

}
