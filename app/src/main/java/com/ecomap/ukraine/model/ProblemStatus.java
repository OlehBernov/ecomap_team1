package com.ecomap.ukraine.model;

/**
 * Possible problem statuses.
 */
public enum ProblemStatus {

    /**
     * Unsolved problems.
     */
    UNSOLVED(0),

    /**
     * Resolved problems.
     */
    RESOLVED(1);

    private int statusId;

    /**
     * Constructor of problem status.
     *
     * @param statusId problem status id.
     */
    ProblemStatus(int statusId) {
        this.statusId = statusId;
    }

    /**
     * Returns problems status according to status id.
     *
     * @param statusId problem status id.
     * @return problem status.
     */
    public static ProblemStatus getProblemStatus(int statusId) {
        for (ProblemStatus status : ProblemStatus.values()) {
            if (status.statusId == statusId) {
                return status;
            }
        }
        return null; //TODO change to default enum constant
    }

    /**
     * Returns problem status id.
     *
     * @return problem status id.
     */
    public int getId() {
        return statusId;
    }

}
