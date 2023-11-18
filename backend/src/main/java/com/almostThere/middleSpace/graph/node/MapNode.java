package com.almostThere.middleSpace.graph.node;

import lombok.Getter;

@Getter
public class MapNode {
    long map_id;
    // 추가: 경도 반환 메서드
    @Getter
    double longitude;
    // 추가: 위도 반환 메서드
    @Getter
    double latitude;

    String name;

    public MapNode(long map_id, double longitude, double latitude, String name) {
        this.map_id = map_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = (name == null) ? "walk" : name;
    }
}
