package com.ecomap.ukraine.ui.fullinfo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.map.IconRenderer;
import com.ecomap.ukraine.models.ActivityType;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.ProblemStatus;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.problemdetails.manager.DetailsListener;
import com.ecomap.ukraine.problemdetails.manager.DetailsManager;
import com.ecomap.ukraine.problemupdate.manager.DataManager;

import java.util.List;

/**
 * Block for full problem information layout which contains basic information about problem.
 */
public class HeaderBlock extends LinearLayout implements DetailsListener {

    private static final String RESOLVED = "resolved";
    private static final String UNSOLVED = "unsolved";
    private static int[] STARS_ID = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

    private TextView problemTitle;
    private TextView problemStatus;
    private ImageView markerIcon;
    private Context context;
    private Problem problem;
    private ImageView voteIcon;

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
     * Performs when vote was successfully sent to server.
     */
    @Override
    public void onVoteAdded() {
        DataManager.getInstance(context).refreshProblemDetails(problem.getProblemId());
        startLikeAnimation();
        voteIcon.setEnabled(false);
    }

    private void startLikeAnimation() {
        final FrameLayout baseInfo = (FrameLayout) findViewById(R.id.details_base_info_layout);
        RelativeLayout innerLayout = (RelativeLayout) findViewById(R.id.frameLayout);
        final ImageView likeImage = new ImageView(getContext());
        likeImage.setImageResource(R.drawable.liked_icon);
        baseInfo.addView(likeImage, voteIcon.getWidth(), voteIcon.getHeight());
        likeImage.setX(voteIcon.getX() + innerLayout.getX());
        likeImage.setY(voteIcon.getY() + innerLayout.getY());
        ObjectAnimator animateX = ObjectAnimator.ofFloat(likeImage, "translationX", -baseInfo.getWidth());
        ObjectAnimator animateY = ObjectAnimator.ofFloat(likeImage, "translationY",
                -TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                        getResources().getDisplayMetrics()));
        animateX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private int count;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if ((count % 4) == 0) {
                    final ImageView image = new ImageView(getContext());
                    baseInfo.addView(image, likeImage.getWidth(), likeImage.getHeight());
                    image.setImageResource(R.drawable.liked_icon);
                    image.setX(likeImage.getX());
                    image.setY(likeImage.getY());
                    image.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            baseInfo.removeView(image);
                        }
                    }).start();
                }
                count++;
            }
        });
        animateY.setInterpolator(PathInterpolatorCompat.create(1F, 1));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animateX, animateY);
        animatorSet.setDuration(1500);
        animatorSet.start();
    }

    @Override
    public void onCommentAdded() {

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
            User user = AccountManager.getInstance(context).getUserFromPreference();
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
        setProblemStatus(problem.getStatus());
        markerIcon.setImageResource(IconRenderer.getResourceIdForMarker(problem.getProblemType()));

        final User user = AccountManager.getInstance(context).getUserFromPreference();
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
                DetailsManager.getInstance(context).registerDetailsListener(HeaderBlock.this);
                DetailsManager.getInstance(context).postVote(problemID, userId, userName, userSurname);
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
                && (problemActivity.getActivityType().getId() == ActivityType.LIKE.getId());
    }

    /**
     * Initiates the view.
     */
    private void init() {
        inflate(context, R.layout.details_base_info, this);
        markerIcon = (ImageView) findViewById(R.id.markerIcon);
        problemTitle = (TextView) findViewById(R.id.title_of_problem);
        problemStatus = (TextView) findViewById(R.id.status_of_problem);
    }

    /**
     * Shows on screen information about topicality of current problem.
     *
     * @param problemStatus status of the current problem.
     */
    private void setProblemStatus(final ProblemStatus problemStatus) {
        if (problemStatus == ProblemStatus.UNSOLVED) {
            this.problemStatus.setText(UNSOLVED);
            this.problemStatus.setTextColor(Color.RED);
        } else {
            this.problemStatus.setText(RESOLVED);
            this.problemStatus.setTextColor(Color.GREEN);
        }
    }

}
