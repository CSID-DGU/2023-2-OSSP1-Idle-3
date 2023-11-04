package com.almostThere.domain.map.entity.node;

import lombok.Getter;

@Getter
public class MapNode {
    long map_id;
    double longitude;
    double latitude;

    public MapNode(long map_id, double longitude, double latitude) {
        this.map_id = map_id;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
