package com.ecomap.ukraine.models.Types;


public enum ProblemStatus {
    UNSOLVED (0),
    RESOLVED (1);

    private int statusId;

    ProblemStatus(int statusId) {
     this.statusId = statusId;
    }

    public int getId() {
        return statusId;
    }

    public static ProblemStatus getProblemStatus (int statusId) {
        for (ProblemStatus status: ProblemStatus.values()) {
            if (status.statusId == statusId) {
                return status;
            }
        }
        return null;
    }
}
