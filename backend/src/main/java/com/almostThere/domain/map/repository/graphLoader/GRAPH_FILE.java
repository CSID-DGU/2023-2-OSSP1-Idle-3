package com.almostThere.domain.map.repository.graphLoader;

import lombok.Getter;

@Getter
public enum GRAPH_FILE {
    STEP_EDGE("step_link.json"),
    BUS_STEP_EDGE("bus_step_edge.json"),
    BUS_ROUTER_EDGE("bus_router_edge_with_transfer.json"),
    GATE_STEP_EDGE("station_gate_nearest_step_link.json"),
    SUBWAY_GATE_EDGE("subway_gate_edge.json"),
    SUBWAY_EDGE("subway_edge_id.json"),
    STEP_NODE("step_node.json"),
    BUS_STOP_NODE("bus_stop_node_with_transfer.json"),
    SUBWAY_NODE("subway_node.json"),
    STATION_GATE_NODE("station_gate_node.json");
    private final String name;
    GRAPH_FILE(String name) {
        this.name = name;
    }
}
