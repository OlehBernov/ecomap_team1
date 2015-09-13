package com.ecomap.ukraine.ui.fullinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.ActivityType;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.ProblemActivity;

import java.util.List;

/**
 * Block for problem activities (comments, likes, ect.).
 */
public class ActivitiesBlock extends LinearLayout {

    private Context context;
    private TableLayout activitiesLayout;

    public ActivitiesBlock(Context context) {
        this(context, null);
    }

    public ActivitiesBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivitiesBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * Puts activities information on DetailContent view.
     *
     * @param details problem details instance.
     */
    public void setActivities(final Details details) {
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
     * Initiates the view.
     */
    private void init() {
        inflate(context, R.layout.problem_activities, this);
        activitiesLayout = (TableLayout) findViewById(R.id.activities);
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
            case LIKE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.like_iconq, null);
            case PHOTO:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_photo_camera_black_36dp, null);
            case COMMENT:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.comment3, null);
            default:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.type1, null);
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

}
