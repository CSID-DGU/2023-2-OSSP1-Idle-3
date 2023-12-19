package com.almostThere.middleSpace.domain.routetable.impl;

import com.almostThere.middleSpace.domain.routetable.RouteInfo;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import lombok.Getter;

@Getter
public class DiRouteTable implements RouteTable, Cloneable{
    private int startNodeIndex;
    private RouteInfo[] dist;
    private final MapGraph mapGraph;

    public DiRouteTable(int startNodeIndex, RouteInfo[] dist, MapGraph mapGraph) {
        this.startNodeIndex = startNodeIndex;
        this.dist = dist;
        this.mapGraph = mapGraph;
    }
    @Override
    public DiRouteTable clone() {
        try {
            DiRouteTable cloned = (DiRouteTable) super.clone();
            cloned.startNodeIndex = startNodeIndex;
            // RouteInfo 배열의 깊은 복사
            cloned.dist = new RouteInfo[this.dist.length];
            for (int i = 0; i < this.dist.length; i++) {
                cloned.dist[i] = (this.dist[i] != null) ? this.dist[i].clone() : null;
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // 이 경우는 일어나지 않을 것이므로 AssertionError로 처리
        }
    }

    @Override
    public MapNode getStartNode() {
        return this.mapGraph.getNode(startNodeIndex);
    }

    @Override
    public double getCost(int search_dest_id) {
        return this.dist[search_dest_id].minCost;
    }
    @Override
    public RouteInfo getRouteInfo(int search_dest_id) {
        return this.dist[search_dest_id];
    }
}
