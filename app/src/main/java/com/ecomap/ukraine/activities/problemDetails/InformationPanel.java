package com.ecomap.ukraine.activities.problemDetails;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

public class InformationPanel {

    private DetailsContent detailsContent;

    /**
     * Constructor
     *
     * @param activity callback activity
     * @param problem  clicked problem
     */
    public InformationPanel(final Activity activity, final Problem problem) {
        LinearLayout rootView = (LinearLayout) activity.findViewById(R.id.details_linear_layout_root);
        DetailsSettings detailsSettings = new DetailsSettings(activity, problem);
        detailsContent = new DetailsContent(rootView, problem, activity, detailsSettings);
    }

    public void setProblemDetails(Details details) {
        detailsContent.setProblemDetails(details);
    }

    public void showDetailsView() {
        detailsContent.showDetailsView();
    }

}
