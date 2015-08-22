package com.ecomap.ukraine.settings;


public enum MapType {
    FIRST (0),
    SECOND (1);

    private int id;

    MapType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
