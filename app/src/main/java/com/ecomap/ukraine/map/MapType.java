package com.ecomap.ukraine.map;

/**
* Possible types of the map.
*/
public enum MapType {
	
	/**
	* Normal map type.
	*/
    MAP_TYPE_NORMAL (0),
	
	/**
	* Hybrid map type.
	*/
    MAP_TYPE_HYBRID (1);

    private int id;

	/**
	* Constructor of map type.
	*
	* @param id id of the map type.
	*/
    MapType(int id) {
        this.id = id;
    }

	/**
	* Returns id of the current map type.
	*
	* @return id of the map type.
	*/
    public int getId() {
        return id;
    }

}
