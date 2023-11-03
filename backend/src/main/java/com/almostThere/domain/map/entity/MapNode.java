package com.almostThere.domain.map.entity;

public class MapNode implements Comparable<MapNode>{
    int index;
    int cost;

    public MapNode(int index, int cost) {
        this.index = index;
        this.cost = cost;
    }

    @Override
    public int compareTo(MapNode o) {
        return Integer.compare(this.cost, o.cost);
    }
}
