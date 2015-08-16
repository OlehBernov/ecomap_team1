package com.ecomap.ukraine.activities;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Types.ActivityType;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.Types.ProblemStatus;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InformationPanel {

    private static final float ANCHOR_POINT = 0.3f;

    private static final String DEFAULT_DESCRIPTION = "Description is missing";
    private static final String DEFAULT_PROPOSAL = "Proposal is missing";
    private static final String ADD_COMMENT_HINT = "Add comment";
    private static final String RESOLVED = "resolved";
    private static final String UNSOLVED = "unsolved";
    private static final String ECOMAP_UKRAINE = "Ecomap Ukraine";
    private static final String PHOTOS_PATH = "http://ecomap.org/photos/large/";
    private static final String TRANSFORMATION = "transformation";

    private static final int STAR_NUMBER = 5;

    private static final int ACTIVITY_ICON_WIDTH = 75;
    private static final int ACTIVITY_ICON_HEIGHT = 75;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ScrollView scrollView;

    private Problem problem;
    private Activity activity;

    private ImageView markerIcon;
    private TextView problemTitle;
    private TextView problemStatus;
    private TextView userInformation;
    private TextView votesNumber;

    private TextView descriptionFiled;
    private TextView proposalFiled;
    private TableLayout activitiesLayout;
    private EditText addComment;
    private LinearLayout photoContainer;

    private TextView titleView;
    private Toolbar toolbar;

    private Context context;

    private boolean isScrollDisable;

    private static int[] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

    /**
     * Constructor
     *
     * @param activity callback activity
     * @param problem  clicked problem
     */
    public InformationPanel(final Activity activity, Problem problem) {

        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        this.problem = problem;

        slidingUpPanelLayout = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        scrollView = (ScrollView) activity.findViewById(R.id.panelScrollView);

        markerIcon = (ImageView) activity.findViewById(R.id.markerIcon);
        problemTitle = (TextView) activity.findViewById(R.id.title_of_problem);
        problemStatus = (TextView) activity.findViewById(R.id.status_of_problem);
        userInformation = (TextView) activity.findViewById(R.id.user);
        votesNumber = (TextView) activity.findViewById(R.id.number_of_likes);

        descriptionFiled = (TextView) activity.findViewById(R.id.description_field);
        proposalFiled = (TextView) activity.findViewById(R.id.proposal_field);
        activitiesLayout = (TableLayout) activity.findViewById(R.id.activities);
        addComment = (EditText) activity.findViewById(R.id.add_comment);
        photoContainer = (LinearLayout) activity.findViewById(R.id.small_photo_conteiner);

        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

        titleView = (TextView) activity.findViewById(R.id.details_title);
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
                scrollView.setVerticalScrollBarEnabled(true);
                isScrollDisable = false;
                setToolbarTransparent();
            }

            private void setToolbarTransparent() {
                toolbar.setBackgroundColor(0x00000000);
                toolbar.setTitle("");
                toolbar.setTranslationY(0);
            }

            @Override
            public void onPanelAnchored(View view) {
                scrollView.setVerticalScrollBarEnabled(false);
                slidingUpPanelLayout.setCoveredFadeColor(0x00000000);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                isScrollDisable = true;
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

        isScrollDisable = true;
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isScrollDisable;
            }
        });
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

        if (isActivityLayoutHaveChild()) {
            activitiesLayout.removeAllViews();
        }
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
                if ((problemActivity.getActivityType() == ActivityType.CREATE) &&
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
     *
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
        for (int i = problemActivities.size() - 1; i >= 0; i--) {
            setPostInformation(problemActivities.get(i));

            TableRow activityRow = new TableRow(context);
            activityRow.addView(buildActivityIcon(problemActivities.get(i)));
            activityRow.addView(buildActivityMessage(problemActivities.get(i)));
            activitiesLayout.addView(activityRow);
        }
    }

    /**
     * Sets post information
     *
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
     *
     * @param problemActivity current problemActivity
     * @return icon
     */
    private ImageView buildActivityIcon(ProblemActivity problemActivity) {
        TableRow.LayoutParams imageParams = new TableRow.LayoutParams(ACTIVITY_ICON_WIDTH, ACTIVITY_ICON_HEIGHT);
        imageParams.topMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        imageParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        ImageView activityTypeIcon = new ImageView(context);
        activityTypeIcon.setImageDrawable(getActivityIcon(problemActivity.getActivityType()));
        activityTypeIcon.setLayoutParams(imageParams);

        return activityTypeIcon;
    }

    /**
     * Builds activity context
     *
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
     *
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
     *
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
        this.setProblemStatus(problem.getStatus());
        this.markerIcon.setImageResource(IconRenderer.getResourceIdForMarker(problem.getProblemType()));
    }

    private void setProblemStatus(ProblemStatus problemStatus) {
        if (problemStatus.equals(ProblemStatus.UNSOLVED)) {
            this.problemStatus.setText(UNSOLVED);
            this.problemStatus.setTextColor(Color.RED);
        } else {
            this.problemStatus.setText(RESOLVED);
            this.problemStatus.setTextColor(Color.GREEN);
        }
    }

    /**
     * Adds photos to sliding panel
     *
     * @param details details of problem
     */
    private void addPhotos(final Details details) {
        Map<Photo, Bitmap> photos = details.getPhotos();
        if (photos == null) {
            hidePhotosTitle();
            return;
        }
        if (isPhotoContainerHaveChild()) {
            photoContainer.removeAllViews();
        }
        BasicContentLayout photoPreview = new BasicContentLayout(photoContainer, context);
        final List<String> urls = new ArrayList<>();
        final List<String> descriptions = new ArrayList<>();
        int position = 0;
        for (final Photo photo : photos.keySet()) {
            urls.add(PHOTOS_PATH + photo.getLink());
            descriptions.add(photo.getDescription());
            ImageView photoView = new ImageView(context);
            photoPreview.addHorizontalBlock(photoView);
            photoView.setId(position++);
            loadPhotoToView(photo, photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhotoSlidePager(v.getId(), urls, descriptions);
                }
            });
        }
    }

    private void loadPhotoToView(Photo photo, ImageView photoView) {
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                BitmapResizer bitmapResizer = new BitmapResizer(context);
                int photoSize = (int) context.getResources().getDimension(R.dimen.small_photo_size);
                Bitmap resizedBitmap = bitmapResizer.resizeBitmap(source, photoSize);
                if (resizedBitmap != source) {
                    source.recycle();
                }
                return resizedBitmap;
            }
            @Override
            public String key() {
                return TRANSFORMATION;
            }
        };

        Picasso.with(context)
                .load(PHOTOS_PATH + photo.getLink())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.photo_error1)
                .transform(transformation)
                .into(photoView);
    }

    private boolean isPhotoContainerHaveChild() {
        return photoContainer != null && (photoContainer.getChildCount() > 0);
    }

    private void openPhotoSlidePager(int position, List<String> urls, List<String> description) {
        ProblemPhotoSlidePager.setContent(urls, description);
        Intent intent = new Intent(activity, ProblemPhotoSlidePager.class);
        intent.putExtra("position", position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void hidePhotosTitle() {
        TextView photosTitle = (TextView) activity.findViewById(R.id.photo);
        photosTitle.setText("");
        photosTitle.setPadding(0, 0, 0, 0);
        photosTitle.setTextSize(0.0f);
    }

}
