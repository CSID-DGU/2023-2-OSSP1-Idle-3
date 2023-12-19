package com.almostThere.middleSpace.graph.edge;

import lombok.Getter;

@Getter
public class OwnEdge implements Comparable<OwnEdge>{
    private final int index;
    private final double cost;

    public OwnEdge(int index, double cost) {
        this.index = index;
        this.cost = cost;
    }

    @Override
    public int compareTo(OwnEdge o) {
        return Double.compare(this.cost, o.cost);
    }
}
