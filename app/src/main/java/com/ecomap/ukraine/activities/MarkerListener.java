package com.ecomap.ukraine.activities;

import android.app.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.ActivityType;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;
import java.util.Map;


public class MarkerListener implements ProblemListener {

    private static final float ANCHOR_POINT = 0.23f;

    private static final String DEFAULT_DESCRIPTION = "Description is missing";
    private static final String DEFAULT_PROPOSAL = "Proposal is missing";
    private static final String ADD_COMMENT_HINT = "Add comment";

    private static final int STAR_NUMBER = 5;
    private static final int DEFAULT_PHOTO_MARGIN = 25;

    private Problem problem;
    private Activity activity;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private RelativeLayout slidingUpLayout;
    private ImageView markerIcon;
    private TextView problemTitle;
    private TextView problemStatus;
    private TextView userInformation;
    private TextView votesNumber;

    private TextView descriptionFiled;
    private TextView proposalFiled;
    private LinearLayout photoContainer;
    private TableLayout activitiesLayout;
    private EditText addComment;

    Context context;

    private static int[] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star1,};

    private Toolbar toolbar;

    public MarkerListener(Activity activity, Problem problem) {
        this.context = activity.getApplicationContext();
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

        descriptionFiled = (TextView) activity.findViewById(R.id.description_field);
        proposalFiled = (TextView) activity.findViewById(R.id.proposal_field);
        photoContainer = (LinearLayout) activity.findViewById(R.id.photo_container);
        activitiesLayout = (TableLayout) activity.findViewById(R.id.activities);
        addComment = (EditText) activity.findViewById(R.id.add_comment);

        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        toolbar.setTitle("");
        this.votesNumber.setText("");
        this.putBriefInformation();

        clearDetailsPanel();

    }

    private void clearDetailsPanel() {
        for (int i = 0; i < STAR_NUMBER; i++) {
            ImageView star = (ImageView) activity.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
        }

        descriptionFiled.setText(DEFAULT_DESCRIPTION);
        proposalFiled.setText(DEFAULT_PROPOSAL);
        addComment.setText(ADD_COMMENT_HINT);

        if (isPhotoContainerHavePhotos()) {
            photoContainer.removeViews(1, photoContainer.getChildCount() - 1);
        }

        if (isActivityLayoutHaveChild()) {
            activitiesLayout.removeAllViews();
        }
    }

    private boolean isPhotoContainerHavePhotos() {
        return  photoContainer != null && (photoContainer.getChildCount() > 1);
    }

    private boolean isActivityLayoutHaveChild() {
        return activitiesLayout != null && (activitiesLayout.getChildCount() > 0);
    }

    @Override
    public void updateAllProblems(List<Problem> problems) {
    }

    @Override
    public void updateProblemDetails(Details details) {
        if (details == null) {
            return;

        }

        putDetailsOnPanel(details);

        if (details.getProblemActivities() != null) {
            for (ProblemActivity problemActivity : details.getProblemActivities()) {
                if ((problemActivity.getActivityTypesId() == ActivityType.CREATE) &&
                        (problemActivity.getProblemId() == problem.getProblemId())) {
                    userInformation.setText(getPostInformation(problemActivity));
                    break;
                }
            }
            addActivitiesInfo(details);
        }
        addPhotos(details);
    }

    private String getPostInformation(ProblemActivity problemActivity) {
        String userName = problemActivity.getFirstName();
        String day = problemActivity.getDate().substring(8, 10);
        String year = problemActivity.getDate().substring(0, 4);
        String month = problemActivity.getDate().substring(5, 7);
        String hour = problemActivity.getDate().substring(11, 13);
        String minutes = problemActivity.getDate().substring(14, 16);
        return userName + " " + day + "/" + month + "/" + year + " " + hour + ":" + minutes;
    }

    private void addActivitiesInfo(final Details details) {
        List<ProblemActivity> problemActivities = details.getProblemActivities();
        if (problemActivities == null) {
            return;
        }
        for (ProblemActivity problemActivity: problemActivities) {
            setPostInformation(problemActivity);

            TableRow activityRow = new TableRow(context);
            activityRow.addView(buildActivityIcon(problemActivity));
            activityRow.addView(buildActivityMessage(problemActivity));
            activitiesLayout.addView(activityRow);
        }
    }

    private void setPostInformation(ProblemActivity problemActivity) {
        TableRow activityRow = new TableRow(context);
        activityRow.addView(new ImageView(context));
        activityRow.setGravity(Gravity.LEFT);
        TextView postInformation = new TextView(context);
        postInformation.setText(getPostInformation(problemActivity));
        postInformation.setGravity(Gravity.LEFT);
        activityRow.addView(postInformation);
        activitiesLayout.addView(activityRow);
    }

    private ImageView buildActivityIcon(ProblemActivity problemActivity) {
        TableRow.LayoutParams imageParams = new TableRow.LayoutParams(100, 100);
        imageParams.topMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        imageParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        ImageView activityTypeIcon = new ImageView(context);
        activityTypeIcon.setImageDrawable(getActivityIcon(problemActivity.getActivityTypesId()));
        activityTypeIcon.setLayoutParams(imageParams);

        return  activityTypeIcon;
    }

    private TextView buildActivityMessage(ProblemActivity problemActivity) {
        TextView activityMessage = new TextView(context);
        TableRow.LayoutParams params =
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.topMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        params.bottomMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        params.leftMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        params.rightMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);

        activityMessage.setText(problemActivity.getContent());
        activityMessage.setTextColor(context.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        activityMessage.setTextSize(context.getResources().getDimension(R.dimen.comment_text_size));
        activityMessage.setLayoutParams(params);

        return activityMessage;
    }

    private Drawable getActivityIcon(ActivityType activityType) {
        switch (activityType) {
            case CREATE:
                return context.getResources().getDrawable(R.drawable.create);
            case UNKNOWN:
                return context.getResources().getDrawable(R.drawable.type1);
            case LIKE:
                return context.getResources().getDrawable(R.drawable.like4);
            case PHOTO:
                return context.getResources().getDrawable(R.drawable.add_photo);
            case COMMENT:
                return context.getResources().getDrawable(R.drawable.comment3);
            case UNKNOWN2:
                return context.getResources().getDrawable(R.drawable.type1);
            default:
                return context.getResources().getDrawable(R.drawable.type1);
        }
    }

    private void putDetailsOnPanel(Details details) {
        this.votesNumber.setText(String.valueOf(details.getVotes()));
        for (int i = 0; i < details.getSeverity(); i++) {
            ImageView star = (ImageView) activity.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_black_48dp);
        }
        descriptionFiled.setText(details.getContent());
        proposalFiled.setText(details.getProposal());
    }

    public void putBriefInformation() {
        problemTitle.setText(problem.getTitle());
        this.setProblemStatus(problem.getStatusId());
        this.markerIcon.setImageResource(IconRenderer.getResourceIdForMarker(problem.getProblemTypesId()));
    }

    private void setProblemStatus(int problemStatusId) {
        if (problemStatusId == 0) {
            this.problemStatus.setText("not resolved");
            this.problemStatus.setTextColor(Color.RED);
        } else {
            this.problemStatus.setText("resolved");
            this.problemStatus.setTextColor(Color.GREEN);
        }
    }

    private void addPhotos (final Details details) {
        final Map<Photo, Bitmap> photos = details.getPhotos();

        if (photos == null) {
            return;
        }

        BasicContentLayout bcl = new BasicContentLayout(photoContainer,
                                                        activity.getApplicationContext());

        for (final Photo photo: photos.keySet()) {
            final  ImageView imagePhoto = new ImageView(context);
            imagePhoto.setImageBitmap(photos.get(photo));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-100, -100);
            params.width = 10;
            params.height = 10;
            imagePhoto.setLayoutParams(params);

    /*      imagePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater
                            = (LayoutInflater) context
                            .getSystemService(context.LAYOUT_INFLATER_SERVICE);

                    View popupView = layoutInflater.inflate(R.layout.popup_window, null);

                    ImageView im = (ImageView) popupView.findViewById(R.id.imageView3);
                    im.setImageBitmap(photos.get(photo));

                    final PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    popupWindow.showAsDropDown(imagePhoto, 100, 100);
                    popupWindow.setAnimationStyle(R.anim.abc_popup_enter);

                    im.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                }
            }); */
            bcl.addHorizontalBlock(imagePhoto, DEFAULT_PHOTO_MARGIN);
        }

    }
}
