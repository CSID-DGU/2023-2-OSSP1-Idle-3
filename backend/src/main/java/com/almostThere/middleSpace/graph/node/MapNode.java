package com.almostThere.middleSpace.graph.node;

import lombok.Getter;

@Getter
public class MapNode {
    long map_id;
    double longitude;
    double latitude;

    String name;

    public MapNode(long map_id, double longitude, double latitude, String name) {
        this.map_id = map_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        if (name == null) {
            this.name = "walk";
        }
    }
}
