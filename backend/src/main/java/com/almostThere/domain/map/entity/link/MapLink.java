package com.almostThere.domain.map.entity.link;

public class MapLink {
    int map_start_id;
    int map_end_id;
    int cost;

    public MapLink(int map_start_id, int map_end_id, int cost) {
        this.map_start_id = map_start_id;
        this.map_end_id = map_end_id;
        this.cost = cost;
    }
}
