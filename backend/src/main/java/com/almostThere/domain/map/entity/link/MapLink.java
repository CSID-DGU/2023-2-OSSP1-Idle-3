package com.almostThere.domain.map.entity.link;

import lombok.Getter;

@Getter
public class MapLink {
    private final int map_start_id;
    private final int map_end_id;
    private final int cost;

    public MapLink(int map_start_id, int map_end_id, int cost) {
        this.map_start_id = map_start_id;
        this.map_end_id = map_end_id;
        this.cost = cost;
    }
}
