package com.almostThere.middleSpace.constant;

import lombok.Getter;

/**
 * 노드, 엣지 데이터 파일의 이름을 관리하는 enum 객체
 * 나중에 in memory db를 사용한다면 바꿀 수 있음
 */
@Getter
public enum FILENAME {
    STEP_EDGE("step_link.json"),
    BUS_STEP_EDGE("bus_step_edge.json"),
    BUS_ROUTER_EDGE("bus_router_edge_with_transfer.json"),
    GATE_STEP_EDGE("station_gate_nearest_step_link.json"),
    SUBWAY_GATE_EDGE("station_gate_edge.json"),
    SUBWAY_EDGE("subway_edge_id.json"),
    STEP_NODE("step_node.json"),
    BUS_STOP_NODE("bus_stop_node_with_transfer.json"),
    SUBWAY_NODE("subway_node.json"),
    STATION_GATE_NODE("station_gate_node.json");
    private final String name;
    FILENAME(String name) {
        this.name = name;
    }
}
