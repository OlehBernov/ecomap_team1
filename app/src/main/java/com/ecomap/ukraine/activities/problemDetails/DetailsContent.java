package com.ecomap.ukraine.activities.problemDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.activities.BitmapResizer;
import com.ecomap.ukraine.activities.main.IconRenderer;
import com.ecomap.ukraine.details.manager.DetailsListener;
import com.ecomap.ukraine.details.manager.DetailsManager;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.Types.ActivityType;
import com.ecomap.ukraine.models.Types.ProblemStatus;
import com.ecomap.ukraine.models.User;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class DetailsContent implements DetailsListener {

    private static final String RESOLVED = "resolved";
    private static final String UNSOLVED = "unsolved";
    private static final String TRANSFORMATION = "transformation";
    private static final String POSITION = "position";
    private static final String WRONG_TEXT = "null";
    private static final String PHOTOS_TITLE = "Photo:";

    private static final int STAR_NUMBER = 5;

    private static int[] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};
    private Problem problem;
    private Activity activity;
    private ImageView markerIcon;
    private TextView problemTitle;
    private TextView problemStatus;
    private TextView userInformation;
    private ImageView voteIcon;
    private TextView votesNumber;
    private EditText commentText;
    private ExpandableTextView descriptionFiled;
    private ExpandableTextView proposalFiled;
    private TableLayout activitiesLayout;
    private EditText addComment;
    private LinearLayout photoContainer;
    private Context context;
    private DetailsManager detailsManager;
    private View view;
    private View detailsContentView;
    private View detailsBaseInfoView;

    private LayoutInflater layoutInflater;

    public DetailsContent(LinearLayout rootView, final Problem problem, final Activity activity,
                          DetailsSettings detailsSettings) {

        this.problem = problem;
        this.activity = activity;

        layoutInflater = activity.getLayoutInflater();
        detailsContentView = layoutInflater.inflate(R.layout.details_content_layout, null, false);
        detailsBaseInfoView = layoutInflater.inflate(R.layout.details_base_info, null, false);

        context = activity.getApplicationContext();
        detailsManager = DetailsManager.getInstance(context);
        detailsManager.removeAllDetailsListener();
        detailsManager.registerDetailsListener(this);

        markerIcon = (ImageView) detailsBaseInfoView.findViewById(R.id.markerIcon);
        problemTitle = (TextView) detailsBaseInfoView.findViewById(R.id.title_of_problem);
        problemStatus = (TextView) detailsBaseInfoView.findViewById(R.id.status_of_problem);
        userInformation = (TextView) detailsBaseInfoView.findViewById(R.id.user);
        voteIcon = (ImageView) detailsBaseInfoView.findViewById(R.id.like);
        votesNumber = (TextView) detailsBaseInfoView.findViewById(R.id.number_of_likes);

        if (isViewHaveChild(rootView)) {
            rootView.removeAllViews();
        }
        rootView.addView(detailsBaseInfoView);

        activitiesLayout = (TableLayout) detailsContentView.findViewById(R.id.activities);
        addComment = (EditText) detailsContentView.findViewById(R.id.add_comment);
        commentText = (EditText) detailsContentView.findViewById(R.id.add_comment);

        photoContainer = (LinearLayout) detailsContentView.findViewById(R.id.small_photo_conteiner);
        descriptionFiled = (ExpandableTextView) detailsContentView.findViewById(R.id.description_field);
        proposalFiled = (ExpandableTextView) detailsContentView.findViewById(R.id.proposal_field);

        if (detailsSettings != null) {
            detailsSettings.perform();
        }

        voteIcon.setEnabled(true);
        voteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                String problemID = String.valueOf(problem.getProblemId());
                String userId = String.valueOf(user.getId());
                String userName = user.getName();
                String userSurname = user.getSurname();
                detailsManager.postVote(problemID, userId, userName, userSurname);
            }
        });

        Button sendComment = (Button) detailsContentView.findViewById(R.id.send_comment);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                int problemID = problem.getProblemId();
                String userId = String.valueOf(user.getId());
                String content = "Test comment";
                String userName = user.getName();
                String userSurname = user.getSurname();
                detailsManager.postComment(problemID, userId, userName,
                        userSurname, content);
            }
        });

        clearDetailsPanel();
        setErrorScreen();
        votesNumber.setText("");
        putBriefInformation();
    }

    @Override
    public void onVoteAdded() {
        new Refresher().setRefreshTask(activity, problem);
        voteIcon.setEnabled(false);
    }

    @Override
    public void onCommentAdded() {
        new Refresher().setRefreshTask(activity, problem);
        commentText.setText("");
    }

    public void setProblemDetails(final Details details) {
        if (details == null) {
            setErrorScreen();
            return;
        }

        view = detailsContentView;
        putDetailsOnPanel(details);

        if (details.getProblemActivities() != null) {
            for (ProblemActivity problemActivity : details.getProblemActivities()) {
                if (isCreationActivity(problemActivity)) {
                    userInformation.setText(getPostInformation(problemActivity));
                    break;
                }
            }
            addActivitiesInfo(details);
        }
        addPhotos(details);
    }

    public void putBriefInformation() {
        problemTitle.setText(problem.getTitle());
        setProblemStatus(problem.getStatus());
        markerIcon.setImageResource(IconRenderer.getResourceIdForMarker(problem.getProblemType()));
    }

    public void showDetailsView() {
        LinearLayout detailsView = (LinearLayout) activity.findViewById(R.id.details_content_root);
        if (isViewHaveChild(detailsView)) {
            detailsView.removeAllViews();
        }
        detailsView.addView(view);
    }

    private void setErrorScreen() {
        view = layoutInflater.inflate(R.layout.internet_connection_error_fragment, null, false);
        showDetailsView();
    }

    private boolean isCreationActivity(ProblemActivity problemActivity) {
        return (problemActivity.getActivityType() == ActivityType.CREATE)
                && (problemActivity.getProblemId() == problem.getProblemId());
    }

    /**
     * Clear details panel
     */
    private void clearDetailsPanel() {
        for (int i = 0; i < STAR_NUMBER; i++) {
            ImageView star = (ImageView) detailsBaseInfoView.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    /**
     * Gets information about post from details
     *
     * @param problemActivity current problemActivity
     * @return information about post
     */
    private String getPostInformation(final ProblemActivity problemActivity) {
        String userName = problemActivity.getFirstName();
        String day = problemActivity.getDate().substring(8, 10);
        String year = problemActivity.getDate().substring(0, 4);
        String month = problemActivity.getDate().substring(5, 7);
        String hour = problemActivity.getDate().substring(11, 13);
        String minutes = problemActivity.getDate().substring(14, 16);
        return userName + " " + day + "/" + month + "/" + year + " " + hour + ":" + minutes;
    }

    private void addActivitiesInfo(final com.ecomap.ukraine.models.Details details) {
        List<ProblemActivity> problemActivities = details.getProblemActivities();
        if (problemActivities == null) {
            return;
        }
        User user = User.getInstance();
        for (int i = problemActivities.size() - 1; i >= 0; i--) {
            if (isProblemLikedBefore(problemActivities.get(i), user)) {
                voteIcon.setEnabled(false);
            }
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
    private void setPostInformation(final ProblemActivity problemActivity) {
        TableRow activityRow = new TableRow(context);
        activityRow.addView(new ImageView(context));
        activityRow.setGravity(Gravity.START);
        TextView postInformation = new TextView(context);
        postInformation.setText(getPostInformation(problemActivity));
        postInformation.setGravity(Gravity.START);
        activityRow.addView(postInformation);
        activitiesLayout.addView(activityRow);
    }

    /**
     * Builds activity icon
     *
     * @param problemActivity current problemActivity
     * @return icon
     */
    private ImageView buildActivityIcon(final ProblemActivity problemActivity) {
        TableRow.LayoutParams imageParams = new TableRow.LayoutParams();

        imageParams.topMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        imageParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.slide_panel_items_margin);
        imageParams.height = (int) context.getResources().getDimension(R.dimen.slide_panel_image_margin);
        imageParams.width = (int) context.getResources().getDimension(R.dimen.slide_panel_image_margin);

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
    private TextView buildActivityMessage(final ProblemActivity problemActivity) {
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
    private Drawable getActivityIcon(final ActivityType activityType) {
        switch (activityType) {
            case CREATE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_done_black_36dp, null);
            case UNKNOWN:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.type1, null);
            case LIKE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.like_iconq, null);
            case PHOTO:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_photo_camera_black_36dp, null);
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
    private void putDetailsOnPanel(final Details details) {
        votesNumber.setText(String.valueOf(details.getVotes()));
        for (int i = 0; i < details.getSeverity(); i++) {
            ImageView star = (ImageView) detailsBaseInfoView.findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_black_48dp);
        }
        String description = details.getContent();
        if (isTextValid(description)) {
            descriptionFiled.setText(description);
        }
        String proposal = details.getProposal();
        if (isTextValid(proposal)) {
            proposalFiled.setText(proposal);
        }
    }

    private boolean isTextValid(String text) {
        return !text.equals(WRONG_TEXT);
    }

    private void setProblemStatus(final ProblemStatus problemStatus) {
        if (problemStatus == ProblemStatus.UNSOLVED) {
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
    private void addPhotos(final com.ecomap.ukraine.models.Details details) {
        final List<Photo> photos = details.getPhotos();
        if (photos == null) {
            hidePhotosBlock();
            return;
        }
        setPhotosTitle();
        if (isViewHaveChild(photoContainer)) {
            photoContainer.removeAllViews();
        }
        BasicContentLayout photoPreview = new BasicContentLayout(photoContainer, context);
        int position = 0;
        for (final Photo photo : photos) {
            ImageView photoView = new ImageView(context);
            photoPreview.addHorizontalBlock(photoView);
            photoView.setId(position++);
            loadPhotoToView(photo, photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhotoSlidePager(v.getId(), photos);
                }
            });
        }
    }

    private void setPhotosTitle() {
        TextView photosTitle = (TextView) detailsContentView.findViewById(R.id.photo);
        photosTitle.setText(PHOTOS_TITLE);
    }

    private void loadPhotoToView(final Photo photo, final ImageView photoView) {
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
                .load(photo.getLink())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.photo_error1)
                .transform(transformation)
                .into(photoView);
    }

    private boolean isViewHaveChild(ViewGroup view) {
        return (view != null) && (view.getChildCount() > 0);
    }

    private void openPhotoSlidePager(final int position, final List<Photo> photos) {
        ProblemPhotoSlidePager.setContent(photos);
        Intent intent = new Intent(activity, ProblemPhotoSlidePager.class);
        intent.putExtra(POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void hidePhotosBlock() {
        TextView photosTitle = (TextView) detailsContentView.findViewById(R.id.photo);
        photosTitle.setText("");
        photoContainer.removeAllViews();
    }

    private boolean isProblemLikedBefore(ProblemActivity problemActivity, User user) {
        return (problemActivity.getUserId() == user.getId())
                && (problemActivity.getActivityType().getId() == ActivityType.LIKE.getId());
    }

}
