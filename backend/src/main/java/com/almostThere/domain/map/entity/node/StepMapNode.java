package com.almostThere.domain.map.entity.node;

public class StepMapNode extends MapNode {
    boolean crosswalk;
    public StepMapNode(int map_id, double longitude, double latitude, boolean crosswalk) {
        super(map_id, longitude, latitude);
        this.crosswalk = crosswalk;
    }
}
