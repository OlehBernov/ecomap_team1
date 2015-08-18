package com.ecomap.ukraine.models.Types;

public enum ActivityType {

    CREATE(1),
    UNKNOWN(2),
    LIKE(3),
    PHOTO(4),
    COMMENT(5),
    UNKNOWN2(6),
    UNKNOWN_TYPE(7);

    private int id;

    ActivityType(int id) {
        this.id = id;
    }

    public static ActivityType getActivityType(int id) {
        for (ActivityType type : ActivityType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return UNKNOWN_TYPE;
    }

    public int getId() {
        return id;
    }

}
