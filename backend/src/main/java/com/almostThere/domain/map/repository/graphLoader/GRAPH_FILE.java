package com.almostThere.domain.map.repository.graphLoader;

import lombok.Getter;

@Getter
public enum GRAPH_FILE {
    BUS_ROUTER_EDGE("bus_router_edge.json"),
    BUS_STOP_NODE("bus_stop_node.json"),
    STEP_EDGE("step_link.json"),
    STEP_NODE("step_node.json"),
    SUBWAY_EDGE("subway_edge.json"),
    SUBWAY_NODE("subway_node.json"),
    SUBWAY_STEP_EDGE("subway_step_link.json"),
    BUS_STEP_EDGE("bus_step_edge.json");

    private final String name;

    GRAPH_FILE(String name) {
        this.name = name;
    }
}
