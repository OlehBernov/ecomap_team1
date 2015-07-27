package com.ecomap.ukraine.database;

import android.provider.BaseColumns;

/**
 * This class provides information about names of tables and columns in
 * SQLite database.
 */
class DBContract {

    public static abstract class Problems implements BaseColumns {
        public static final String TABLE_NAME = "Problems";

        public static final String ID = "Id";

        public static final String PROBLEM_STATUS = "Status";

        public static final String PROBLEM_TYPES_ID = "ProblemTypes_Id";

        public static final String PROBLEM_TITLE = "Title";

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

        public static final String LAST_UPDATE = "Date";

        public static final String TITLE = "Title";
    }

    public static abstract class Photos implements BaseColumns {
        public static final String TABLE_NAME = "Photos";

        public static final String PROBLEM_ID = "Problem_ID";

        public static final String PHOTO_ID = "Photo_ID";

        public static final String PHOTO_USERS_ID = "Users_Id";

        public static final String PHOTO_STATUS = "Status";

        public static final String LINK = "Link";

        public static final String PHOTO_DESCRIPTION = "Description";
    }

    public static abstract class ProblemActivity implements BaseColumns {
        public static final String TABLE_NAME = "Activities";
        public static final String PROBLEM_ID = "Problem_ID";
        public static final String ACTIVITY_TYPES_ID = "ActivityTypes_Id";
        public static final String ACTIVITY_USERS_ID = "Users_Id";
        public static final String PROBLEM_ACTIVITY_DATE = "Date";
        public static final String PROBLEM_ACTIVITY_ID = "Problem_Activity_id";
        public static final String PROBLEM_ACTIVITY_CONTENT = "Content";
        public static final String USER_NAME = "userName";
    }

}
