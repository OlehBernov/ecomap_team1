package com.ecomap.ukraine.settings;


public enum MapType {
    MAP_TYPE_NORMAL (0),
    MAP_TYPE_HYBRID (1);

    private int id;

    MapType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
