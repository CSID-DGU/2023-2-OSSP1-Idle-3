package com.almostThere.middleSpace.graph.edge;

import lombok.Getter;

@Getter
public class MapEdge {
    private final long map_start_id;
    private final long map_end_id;
    private final double cost;
    private final String line;

    public MapEdge(long map_start_id, long map_end_id, double cost, String line) {
        this.map_start_id = map_start_id;
        this.map_end_id = map_end_id;
        this.cost = cost;
        if (line == null)
            this.line = "walk";
        else
            this.line = line;
    }
}
