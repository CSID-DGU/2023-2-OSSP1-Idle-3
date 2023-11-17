package com.almostThere.middleSpace.graph.edge;

import lombok.Getter;

@Getter
public class OwnEdge implements Comparable<OwnEdge>{
    private final int index;
    private final double cost;
    private final String line;

    public OwnEdge(int index, double cost, String line) {
        this.index = index;
        this.cost = cost;
        this.line = line;
    }

    @Override
    public int compareTo(OwnEdge o) {
        return Double.compare(this.cost, o.cost);
    }
}
