package com.ecomap.ukraine.models.Types;


public enum ProblemType {

    FOREST_DESTRUCTION(1),
    RUBBISH_DUMP(2),
    ILLEGAL_BUILDING(3),
    WATER_POLLUTION(4),
    THREAD_TO_BIODIVERSITY(5),
    POACHING(6),
    OTHER(7);

    private int id;

    ProblemType(int id) {
        this.id = id;
    }

    public static ProblemType getProblemType(int id) {
        for (ProblemType type : ProblemType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return OTHER;
    }

    public int getId() {
        return id;
    }

}
