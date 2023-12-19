package com.almostThere.middleSpace.graph.edge;

import lombok.Getter;

@Getter
public class MapEdge {
    private final long map_start_id;
    private final long map_end_id;
    private final double cost;

    public MapEdge(long map_start_id, long map_end_id, double cost) {
        this.map_start_id = map_start_id;
        this.map_end_id = map_end_id;
        this.cost = cost;
    }
}
