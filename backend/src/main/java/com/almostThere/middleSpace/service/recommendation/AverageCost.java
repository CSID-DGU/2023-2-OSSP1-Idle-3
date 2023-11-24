package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.graph.node.MapNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AverageCost {
    private MapNode node;
    private Double cost;
    private Double sum;

    public AverageCost(MapNode node) {
        this.node = node;
        this.cost = Double.MAX_VALUE;
        this.sum = Double.MAX_VALUE;
    }
}
