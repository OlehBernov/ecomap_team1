package com.ecomap.ukraine.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

public class DetailsContentContent {

    private Context context;
    private BasicContentLayout basicContentLayout;
    private LayoutInflater layoutInflater;
    private View loadingView;
    private View errorView;
    private HeaderBlock headerBlock;
    private Problem problem;

    public DetailsContentContent(BasicContentLayout basicContentLayout, Context context) {
        this.basicContentLayout = basicContentLayout;
        this.context = context;

        clear();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBaseInfo(Problem problem) {
        this.problem = problem;

        headerBlock = new HeaderBlock(context);
        headerBlock.setHeaderBaseInfo(problem);
        basicContentLayout.addVerticalBlock(headerBlock);

        setDetailsLoadingScreen();
    }

    public void setProblemDetails(Details details) {
        basicContentLayout.removeBlock(loadingView);

        if (details == null) {
            setErrorScreen();
            return;
        } else {
            basicContentLayout.removeBlock(errorView);
            //TODO: how to find out is view exist inside another view or not? delete assignment
            errorView = null;
        }

        headerBlock.setHeaderDetailInfo(details);

        DescriptionBlock descriptionBlock = new DescriptionBlock(context);
        descriptionBlock.setDescription(details);
        basicContentLayout.addVerticalBlock(descriptionBlock);

        ProposalBlock proposalBlock = new ProposalBlock(context);
        proposalBlock.setProposal(details);
        basicContentLayout.addVerticalBlock(proposalBlock);

        PhotoBlock photoBlock = new PhotoBlock(context);
        List<Photo> photos = details.getPhotos();
        if (photos != null) {
            photoBlock.setPhotos(photos);
            basicContentLayout.addVerticalBlock(photoBlock);
        }

        CommentBlock commentBlock = new CommentBlock(context);
        commentBlock.setProblemId(problem.getProblemId());
        basicContentLayout.addVerticalBlock(commentBlock);

        ActivitiesBlock activitiesBlock = new ActivitiesBlock(context);
        activitiesBlock.setActivities(details);
        basicContentLayout.addVerticalBlock(activitiesBlock);
    }

    public void prepareToRefresh() {
        clear();
        setBaseInfo(problem);
    }

    private void clear() {
        if (basicContentLayout.getNumberOfBlocks() > 0) {
            basicContentLayout.removeAllBlocks();
        }

        if (isViewHaveChild(basicContentLayout)) {
            basicContentLayout.removeAllViews();
        }
    }

    /**
     * Checks is view have children.
     *
     * @param view view, which need to check.
     * @return whether the view have children.
     */
    private boolean isViewHaveChild(final ViewGroup view) {
        return (view != null) && (view.getChildCount() > 0);
    }

    /**
     * Sets on screen details loading view.
     */
    private void setDetailsLoadingScreen() {
        loadingView = layoutInflater.inflate(R.layout.problem_details_loading_view, null, false);
        basicContentLayout.addVerticalBlock(loadingView);
    }

    /**
     * Sets on screen server connection error view.
     */
    private void setErrorScreen() {
        errorView = layoutInflater.inflate(R.layout.internet_connection_error_view, null, false);
        basicContentLayout.addVerticalBlock(errorView);
    }

}
