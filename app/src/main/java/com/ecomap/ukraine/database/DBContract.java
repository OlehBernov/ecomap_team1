package com.ecomap.ukraine.database;

import android.provider.BaseColumns;

/**
 * This class provides information about names of tables and columns in
 * SQLite database.
 */
public class DBContract {

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

    public static abstract class Top implements BaseColumns {

        public static final String PROBLEM_ID = "Problem_ID";

        public static final String PROBLEM_TITLE = "Problem_Title";
    }

    public static abstract class TopByVotes implements BaseColumns {

        public static final String TABLE_NAME = "Top_problems_by_votes";

        public static final String PROBLEM_VOTES = "Problem_Votes";
    }

    public static abstract class TopByComments implements BaseColumns {

        public static final String TABLE_NAME = "Top_problems_by_comments";

        public static final String PROBLEM_COMMENTS = "Problem_Comments";
    }

    public static abstract class TopBySeverity implements BaseColumns {

        public static final String TABLE_NAME = "Top_problems_by_severity";

        public static final String PROBLEM_SEVERITY = "Problem_Severity";
    }

    public static abstract class Statistics implements BaseColumns {

        public static final String TABLE_NAME = "Statistics";

        public static final String PROBLEM_TYPE_ID = "Problem_type_ID";

        public static final String DAILY_VALUE = "Daily_value";

        public static final String WEEKLY_VALUE = "Weekly_value";

        public static final String MONTH_VALUE = "Month_value";

        public static final String ANNUAL_VALUE = "Annual_value";

        public static final String VALUE_FOR_ALL_TIME = "Value_for_all_time";
    }

}
