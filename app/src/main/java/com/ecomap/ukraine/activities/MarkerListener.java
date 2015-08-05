package com.ecomap.ukraine.activities;

import android.app.Activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;


public class MarkerListener implements ProblemListener {

    private static final float ANCHOR_POINT = 0.23f;

    private Problem problem;
    private Activity activity;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private RelativeLayout slidingUpLayout;
    private ImageView markerIcon;
    private TextView problemTitle;
    private TextView problemStatus;
    private TextView userInformation;
    private TextView votesNumber;

    private static int [] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star1,};

    private Toolbar toolbar;

    public MarkerListener(Activity activity , Problem problem) {
        this.activity = activity;
        this.toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        this.problem = problem;
        slidingUpPanelLayout = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        slidingUpLayout = (RelativeLayout) activity.findViewById(R.id.slidingUpLayout);
        markerIcon = (ImageView) activity.findViewById(R.id.markerIcon);
        problemTitle = (TextView) activity.findViewById(R.id.title_of_problem);
        problemStatus = (TextView) activity.findViewById(R.id.status_of_problem);
        userInformation = (TextView) activity.findViewById(R.id.user);
        votesNumber = (TextView) activity.findViewById(R.id.number_of_likes);

        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        toolbar.setTitle("");
        this.votesNumber.setText("");
        this.putBriefInformation();


        for(int i=0; i < 5; i++) {
            ImageView star = (ImageView) activity.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
        }



    }

    @Override
    public void updateAllProblems(List<Problem> problems) {
    }

    @Override
    public void updateProblemDetails(Details details) {
        //TODO implement
        if(details == null) {
            return;

        }
        this.votesNumber.setText(String.valueOf(details.getVotes()));
        Log.d("details", details.getTitle());

        for(int i=0; i < details.getSeverity(); i++) {
            ImageView star = (ImageView) activity.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_black_48dp);
        }
            String message = "";
            for(ProblemActivity problemActivity : details.getProblemActivities()) {

                if ((problemActivity.getActivityTypesId() == 1) &&
                        (problemActivity.getProblemId() == problem.getProblemId())) {
                    String userName = problemActivity.getFirstName();
                    String day = problemActivity.getDate().substring(8, 10);
                    String year = problemActivity.getDate().substring(0, 4);
                    String mounth = problemActivity.getDate().substring(5, 7);
                    String hour = problemActivity.getDate().substring(11, 13);
                    String minuts = problemActivity.getDate().substring(14, 16);
                    message = userName + " " + day + "/" + mounth + "/" + year + " " + hour + ":" + minuts;
                    userInformation.setText(message);
                    break;
                }
            }
            }

    public void putBriefInformation() {
        problemTitle.setText(problem.getTitle());
        this.setProblemStatus(problem.getStatusId());
        this.markerIcon.setImageResource(IconRenderer.getResourceIdForMarker(problem.getProblemTypesId()));

    }

    private void setProblemStatus (int problemStatusId) {
        if(problemStatusId==0) {
            this.problemStatus.setText("not resolved");
            this.problemStatus.setTextColor(Color.RED);
        }
        else {
            this.problemStatus.setText("resolved");
            this.problemStatus.setTextColor(Color.GREEN);
        }
    }
}
