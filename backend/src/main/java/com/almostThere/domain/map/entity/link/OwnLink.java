package com.almostThere.domain.map.entity.link;

import lombok.Getter;

@Getter
public class OwnLink implements Comparable<OwnLink>{
    private final int index;
    private final double cost;
    private final String line;

    public OwnLink(int index, double cost, String line) {
        this.index = index;
        this.cost = cost;
        this.line = line;
    }

    @Override
    public int compareTo(OwnLink o) {
        return Double.compare(this.cost, o.cost);
    }
}
