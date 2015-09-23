package com.ecomap.ukraine.ui.fullinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.problemupdate.manager.DataListenerAdapter;
import com.ecomap.ukraine.problemupdate.manager.DataManager;

/**
 * Block for posting comments.
 */
public class CommentBlock extends LinearLayout {

    private Context context;
    private int problemId;
    private EditText commentText;

    public CommentBlock(Context context) {
        this(context, null);
    }

    public CommentBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    /**
     * Sets problem id.
     *
     * @param problemId problem id.
     */
    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }



    /**
     * Initiates the view.
     */
    private void init(final Context context) {
        inflate(context, R.layout.problem_comment, this);
        commentText = (EditText) findViewById(R.id.add_comment);

        Button sendComment = (Button) findViewById(R.id.send_comment);
        sendComment.setOnClickListener(new OnClickListener() {
            /**
             * Adds comment to the current problem. Prepares data for posting to server.
             *
             * @param v "send comment" button.
             */
            @Override
            public void onClick(View v) {
                int problemID = problemId;
                User user = AccountManager.getInstance(context).getUserFromPreference();
                String userId = String.valueOf(user.getId());
                String content = commentText.getText().toString();
                String userName = user.getName();
                String userSurname = user.getSurname();
                if (AccountManager.getInstance(v.getContext()).isAnonymousUser()) {
                    Toast.makeText(context,
                            R.string.anonym_post_comment, Toast.LENGTH_LONG).show();
                    return;
                }
                if (content.isEmpty()) {
                    Toast.makeText(context,
                            R.string.empty_post_comment, Toast.LENGTH_LONG).show();
                } else {
                    DataManager.getInstance(context).registerProblemListener(new DataListenerAdapter() {
                        /**
                         * Performs when comment was successfully sent to server.
                         */
                        @Override
                        public void onCommentAdded() {
                            DataManager.getInstance(context).refreshProblemDetails(problemId);
                            commentText.setText("");
                        }
                    });
                    DataManager.getInstance(context).postComment(problemID, userId, userName,
                            userSurname, content);
                }
            }
        });
    }
}
