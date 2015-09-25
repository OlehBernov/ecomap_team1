package com.ecomap.ukraine.ui.fullinfo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.map.IconRenderer;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Block for full problem information layout which contains basic information about problem.
 */
public class HeaderBlock extends LinearLayout {

    private static final String DATE_TEMPLATE = "yyyy-MM-dd'T'HH:mm:ss'.000Z'";
    private static final String DATE_TEMPLATE_FOR_SHOW = "dd-MM-yyyy";
    private static final String RESOLVED = "resolved";
    private static final String UNSOLVED = "unsolved";
    private static int[] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

    private Problem problem;

    private TextView problemTitle;
    private TextView problemStatus;
    private ImageView markerIcon;
    private Context context;
    private ImageView voteIcon;
    private TextView dateOfPostingAndAuthor;

    public HeaderBlock(Context context) {
        this(context, null);
    }

    public HeaderBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * Complements basic information about problem.
     *
     * @param details problem details.
     */
    public void setHeaderDetailInfo(Details details) {
        voteIcon.setEnabled(true);

        List<ProblemActivity> problemActivities = details.getProblemActivities();
        if (problemActivities != null) {
            setDateOfPostingAndAuthor(problemActivities.get(0));
            User user = AccountManager.getInstance(context).getUser();
            for (int i = problemActivities.size() - 1; i >= 0; i--) {
                if (isProblemLikedBefore(problemActivities.get(i), user)) {
                    voteIcon.setEnabled(false);
                    break;
                }
            }
        }

        TextView votesNumber = (TextView) findViewById(R.id.number_of_likes);
        votesNumber.setText(String.valueOf(details.getVotes()));
        for (int i = 0; i < details.getSeverity(); i++) {
            ImageView star = (ImageView) findViewById(STARS_ID[i]);
            star.setBackgroundResource(R.drawable.ic_star_black_48dp);
        }
    }

    /**
     * Sets brief problem information on DetailsContent view header.
     */
    public void setHeaderBaseInfo(final Problem problem) {
        this.problem = problem;

        problemTitle.setText(problem.getTitle());
        dateOfPostingAndAuthor.setText(problem.getDate());
        setProblemStatus(problem.getStatus());
        markerIcon.setImageResource(IconRenderer.getResourceIdForMarker(problem.getProblemType()));

        final User user = AccountManager.getInstance(context).getUser();
        voteIcon = (ImageView) findViewById(R.id.like);
        voteIcon.setEnabled(false);
        voteIcon.setOnClickListener(new OnClickListener() {
            /**
             * Adds like to the current problem. Prepares data for posting to server.
             *
             * @param v "like" button.
             */
            @Override
            public void onClick(View v) {
                String problemID = String.valueOf(problem.getProblemId());
                String userId = String.valueOf(user.getId());
                String userName = user.getName();
                String userSurname = user.getSurname();
                DataManager.getInstance(context).registerProblemListener(new DataListenerAdapter() {
                    /**
                     * Performs when vote was successfully sent to server.
                     */
                    @Override
                    public void onVoteAdded() {
                        DataManager.getInstance(context).refreshProblemDetails(problem.getProblemId());
                        voteIcon.setEnabled(false);
                    }
                });
                DataManager.getInstance(context).postVote(problemID, userId, userName, userSurname);
            }
        });
    }

    /**
     * Checks is this problem was liked by current user.
     *
     * @param problemActivity problem activity which may contain information about like.
     * @param user            current logged in user.
     * @return whether current problem was liked by this user.
     */
    private boolean isProblemLikedBefore(final ProblemActivity problemActivity, final User user) {
        return (problemActivity.getUserId() == user.getId())
                && (problemActivity.getActivityType() == ProblemActivity.LIKE);
    }

    /**
     * Initiates the view.
     */
    private void init() {
        inflate(context, R.layout.details_base_info, this);
        markerIcon = (ImageView) findViewById(R.id.markerIcon);
        problemTitle = (TextView) findViewById(R.id.title_of_problem);
        problemStatus = (TextView) findViewById(R.id.status_of_problem);
        dateOfPostingAndAuthor = (TextView) findViewById(R.id.date_of_posting);
    }

    /**
     * Shows on screen information about topicality of current problem.
     *
     * @param problemStatus status of the current problem.
     */
    private void setProblemStatus(final int problemStatus) {
        if (problemStatus == Problem.UNSOLVED) {
            this.problemStatus.setText(UNSOLVED);
            this.problemStatus.setTextColor(Color.RED);
        } else {
            this.problemStatus.setText(RESOLVED);
            this.problemStatus.setTextColor(Color.GREEN);
        }
    }

    /**
     * Sets the creator name and date of posting to the relevant place on view.
     *
     * @param creatorActivity activity which contains information about creator of the problem.
     */
    private void setDateOfPostingAndAuthor(ProblemActivity creatorActivity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);
        Calendar postingDate = Calendar.getInstance();
        try {
            postingDate.setTime(dateFormat.parse(problem.getDate()));
        } catch (ParseException e) {
            postingDate.setTime(new Date(System.currentTimeMillis()));
        }

        dateFormat = new SimpleDateFormat(DATE_TEMPLATE_FOR_SHOW, Locale.ENGLISH);
        dateOfPostingAndAuthor.setText(creatorActivity.getFirstName()
                                       + " " + dateFormat.format(postingDate.getTime()));
    }

}
