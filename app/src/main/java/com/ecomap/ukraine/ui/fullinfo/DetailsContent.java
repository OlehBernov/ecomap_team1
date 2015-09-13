package com.ecomap.ukraine.ui.fullinfo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.util.BasicContentLayout;

import java.util.List;

/**
 * Builds and sets required blocks to the basic layout for describing
 * basic information about problem.
 */
public class DetailsContent {

    private Context context;
    private BasicContentLayout basicContentLayout;
    private LayoutInflater layoutInflater;
    private View loadingView;
    private View errorView;
    private HeaderBlock headerBlock;
    private Problem problem;

    public DetailsContent(BasicContentLayout basicContentLayout, Context context) {
        this.basicContentLayout = basicContentLayout;
        this.context = context;
        clearLayout();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Builds and sets required blocks to the basic layout for describing basic problem
     * information.
     *
     * @param problem problem.
     */
    public void setBaseInfo(Problem problem) {
        this.problem = problem;

        headerBlock = new HeaderBlock(context);
        headerBlock.setHeaderBaseInfo(problem);
        basicContentLayout.addVerticalBlock(headerBlock);

        setDetailsLoadingScreen();
    }

    /**
     * Builds and sets required blocks to the basic layout for describing problem details.
     *
     * @param details problem details.
     */
    public void setProblemDetails(Details details) {
        basicContentLayout.removeBlock(loadingView);

        if (details == null) {
            setErrorScreen();
            return;
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

    /**
     * Prepare view to refresh problem details.
     */
    public void prepareToRefresh() {
        clearLayout();
        setBaseInfo(problem);
    }

    /**
     * Delete all blocks in layout.
     */
    private void clearLayout() {
        if (basicContentLayout.getNumberOfBlocks() > 0) {
            basicContentLayout.removeAllBlocks();
        }
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
