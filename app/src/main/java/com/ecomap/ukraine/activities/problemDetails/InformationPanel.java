package com.ecomap.ukraine.activities.problemDetails;

import android.app.Activity;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

public class InformationPanel {

    private static InformationPanel instance;

    private DetailsContent detailsContent;

    private Activity activity;

    /**
     * Constructor
     *
     * @param activity callback activity
     */
    private InformationPanel(final Activity activity) {
        detailsContent = (DetailsContent) activity.findViewById(R.id.panel_details_content);
        this.activity = activity;
    }

    public static InformationPanel getInstance(Activity activity) {
        if (instance == null) {
            instance = new InformationPanel(activity);
        }
        return instance;
    }

    public void setBaseProblem(final Problem problem) {
        DetailsSettings detailsSettings = new DetailsSettings(activity, problem);
        detailsContent.setProblemContent(problem, detailsSettings);
    }

    public void setProblemDetails(Details details) {
        detailsContent.setProblemDetails(details);
    }

}
