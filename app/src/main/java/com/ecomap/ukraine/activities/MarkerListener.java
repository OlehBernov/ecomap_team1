package com.ecomap.ukraine.activities;

import android.app.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class MarkerListener {

    private static final float ANCHOR_POINT = 0.3f;

    private static final String DEFAULT_DESCRIPTION = "Description is missing";
    private static final String DEFAULT_PROPOSAL = "Proposal is missing";
    private static final String ADD_COMMENT_HINT = "Add comment";
    private static final String RESOLVED = "resolved";
    private static final String UNSOLVED = "unsolved";
    private static final String ECOMAP_UKRAINE= "Ecomap Ukraine";

    private static final int STAR_NUMBER = 5;
    private static final int DEFAULT_PHOTO_MARGIN = 25;

    private static final int ACTIVITY_ICON_WIDTH = 75;
    private static final int ACTIVITY_ICON_HEIGHT = 75;

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

    private TextView titleView;

    Context context;

    private static int[] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

    private Toolbar toolbar;

    /**
     * Constructor
     * @param activity callback activity
     * @param problem clicked problem
     */
    public MarkerListener(final Activity activity, Problem problem) {
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

        titleView = (TextView)activity.findViewById(R.id.details_title);
        toolbar.setBackgroundColor(0xff004d40);

        slidingUpPanelLayout = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View view, float v) {
                rescaleText(v);
                moveToolbar(v);
            }

            @Override
            public void onPanelCollapsed(View view) {
                setToolbarInitialState();
                toolbar.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2)).start();
            }

            private void setToolbarInitialState() {
                toolbar.setTitle(ECOMAP_UKRAINE);
                toolbar.setBackgroundColor(0xff004d40);
            }

            @Override
            public void onPanelExpanded(View view) {
                setToolbarTransparent();
            }

            private void setToolbarTransparent() {
                toolbar.setBackgroundColor(0x00000000);
                toolbar.setTitle("");
                toolbar.setTranslationY(0);
            }

            @Override
            public void onPanelAnchored(View view) {
                slidingUpPanelLayout.setCoveredFadeColor(0x00000000);
            }

            @Override
            public void onPanelHidden(View view) {
            }


            private void rescaleText(float v) {
                float scale = (Math.max(v - 0.9F, 0) * 3);
                titleView.setScaleX(1 - scale);
                titleView.setScaleY(1 - scale);
            }


            private void moveToolbar(float v) {
                if ((v > slidingUpPanelLayout.getAnchorPoint())) {
                    setToolbarInitialState();
                    slidingUpPanelLayout.setCoveredFadeColor(0xff004d40);
                    if (convertToPixels(v) < (convertToPixels(slidingUpPanelLayout
                            .getAnchorPoint() + toolbar.getHeight()))) {
                        toolbar.setY(convertToPixels(slidingUpPanelLayout
                                .getAnchorPoint()) - convertToPixels(v));
                    } else {
                        toolbar.setY(-toolbar.getHeight());
                    }
                }
            }

            private int convertToPixels(float value) {
                int displayHeightInPixels = activity.getResources()
                        .getDisplayMetrics().heightPixels;
                return (int) (value * displayHeightInPixels);
            }
        });

        this.votesNumber.setText("");
        this.putBriefInformation();

    }

    /**
     * Clear details panel
     */
    private void clearDetailsPanel() {
        for (int i = 0; i < STAR_NUMBER; i++) {
            ImageView star = (ImageView) activity.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
        }

        descriptionFiled.setText(DEFAULT_DESCRIPTION);
        proposalFiled.setText(DEFAULT_PROPOSAL);
        addComment.setText("");
        addComment.setHint(ADD_COMMENT_HINT);

        if (isPhotoContainerHavePhotos()) {
            photoContainer.removeViews(0, photoContainer.getChildCount());
        }

        if (isActivityLayoutHaveChild()) {
            activitiesLayout.removeAllViews();
        }
    }

    private boolean isPhotoContainerHavePhotos() {
        return  photoContainer != null && (photoContainer.getChildCount() > 0);
    }

    private boolean isActivityLayoutHaveChild() {
        return activitiesLayout != null && (activitiesLayout.getChildCount() > 0);
    }


    public void setProblemDetails(Details details) {
        if (details == null) {
            return;
        }

        clearDetailsPanel();
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

    /**
     * Gets information about post from details
     * @param problemActivity current problemActivity
     * @return information about post
     */
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

    /**
     * Sets post information
     * @param problemActivity current problemActivity
     */
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

    /**
     * Builds activity icon
     * @param problemActivity current problemActivity
     * @return icon
     */
    private ImageView buildActivityIcon(ProblemActivity problemActivity) {
        TableRow.LayoutParams imageParams = new TableRow.LayoutParams(ACTIVITY_ICON_WIDTH, ACTIVITY_ICON_HEIGHT);
        imageParams.topMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        imageParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        ImageView activityTypeIcon = new ImageView(context);
        activityTypeIcon.setImageDrawable(getActivityIcon(problemActivity.getActivityTypesId()));
        activityTypeIcon.setLayoutParams(imageParams);

        return  activityTypeIcon;
    }

    /**
     * Builds activity context
     * @param problemActivity current problemActivity
     * @return activity context
     */
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

    /**
     * Gets icon resource for activity by activityType
     * @param activityType activityType
     * @return icon resource
     */
    private Drawable getActivityIcon(ActivityType activityType) {
        switch (activityType) {
            case CREATE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.create, null);
            case UNKNOWN:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.type1, null);
            case LIKE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.like4, null);
            case PHOTO:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.add_photo, null);
            case COMMENT:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.comment3, null);
            case UNKNOWN2:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.type1, null);
            default:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.type1, null);
        }

    }

    /**
     * Puts details on sliding panel
     * @param details details of problem
     */
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
            this.problemStatus.setText(UNSOLVED);
            this.problemStatus.setTextColor(Color.RED);
        } else {
            this.problemStatus.setText(RESOLVED);
            this.problemStatus.setTextColor(Color.GREEN);
        }
    }

    /**
     * Adds photos to sliding panel
     * @param details details of problem
     */
    private void addPhotos (final Details details) {
        Map<Photo, Bitmap> photos = details.getPhotos();

        if (photos == null) {
            hidePhotosTitle();
            return;
        }

        BasicContentLayout photoLayout = new BasicContentLayout(photoContainer, context);

        for (Photo photo: photos.keySet()) {
            ImageView imagePhoto = new ImageView(context);
            imagePhoto.setImageBitmap(photos.get(photo));

          /*  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            params.width = 10;
            params.height = 10;
            imagePhoto.setLayoutParams(params);*/

            photoLayout.addHorizontalBlock(imagePhoto, DEFAULT_PHOTO_MARGIN);
        }
    }

    private void hidePhotosTitle() {
        TextView photosTitle = (TextView) activity.findViewById(R.id.photo);
        photosTitle.setText("");
        photosTitle.setPadding(0, 0, 0, 0);
        photosTitle.setTextSize(0.0f);
    }

}
