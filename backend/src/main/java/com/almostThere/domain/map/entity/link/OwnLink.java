package com.almostThere.domain.map.entity.link;

import lombok.Getter;

@Getter
public class OwnLink implements Comparable<OwnLink>{
    private final int index;
    private final int cost;

    public OwnLink(int index, int cost) {
        this.index = index;
        this.cost = cost;
    }

    @Override
    public int compareTo(OwnLink o) {
        return Integer.compare(this.cost, o.cost);
    }
}
