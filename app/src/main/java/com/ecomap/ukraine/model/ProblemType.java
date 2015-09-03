package com.ecomap.ukraine.model;

/**
 * Possible problem types.
 */
public enum ProblemType {

    FOREST_DESTRUCTION(1),
    RUBBISH_DUMP(2),
    ILLEGAL_BUILDING(3),
    WATER_POLLUTION(4),
    THREAD_TO_BIODIVERSITY(5),
    POACHING(6),
    OTHER(7);

    private int id;

    /**
     * Constructor.
     *
     * @param id problem type id.
     */
    ProblemType(int id) {
        this.id = id;
    }

    /**
     * Return problem type according to problem type is.
     * @param id problem type id.
     * @return problem type.
     */
    public static ProblemType getProblemType(int id) {
        for (ProblemType type : ProblemType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return OTHER;
    }

    /**
     * Returns id of the problem type.
     *
     * @return problem type id.
     */
    public int getId() {
        return id;
    }

}
