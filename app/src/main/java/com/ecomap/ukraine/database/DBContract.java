package com.ecomap.ukraine.database;

import android.provider.BaseColumns;

/**
 * Created by Oleh on 7/24/2015.
 */
public class DBContract {
    public static final String CREATE_PROBLEMS_TABLE =
            "CREATE TABLE ";
    public static final String CREATE_DETAILS_TABLE = "";
    public static final String CREATE_PHOTO_TABLE = "";
    public static final String CREATE_ACTIVITY_TABLE = "";
    public static final String DELETE_ALL_TABLES = "";

    public static abstract class Problems implements BaseColumns {
        public static final String TABLE_NAME = "Problems";

        public static final String ID = "Id";

        public static final String PROBLEM_STATUS = "Status";

        public static final String PROBLEM_TYPES_ID = "ProblemTypes_Id";

        public static final String PROBLEM_DATE = "Date";

        public static final String LATITUDE = "Latitude";

        public static final String LONGITUDE = "Longitude";
    }

    public static abstract class Details implements BaseColumns {
        public static final String TABLE_NAME = "Details";

        public static final String PROBLEM_ID = "Problem_ID";

        public static final String PROBLEM_CONTENT = "Content";

        public static final String PROPOSAL = "Proposal";

        public static final String MODERATION = "Moderation";

        public static final String SEVERITY = "Severity";

        public static final String VOTES = "Votes";
    }

    public static abstract class Photos implements BaseColumns {
        public static final String TABLE_NAME = "Photos";


    }

    public static abstract class ProblemActivity implements BaseColumns {
        public static final String TABLE_NAME = "Activities";

        public static final String PROBLEM_ID = "Problem_ID";

        public static final String ACTIVITY_TYPES_ID = "ActivityTypes_Id";

        public static final String COMMENT_USERS_ID = "Users_Id";

        public static final String PROBLEM_ACTIVITY_DATE = "Date";

        public static final String PROBLEM_ACTIVITY_CONTENT = "Content";

        public static final String USER_NAME = "userName";
    }

}
