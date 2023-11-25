package com.almostThere.middleSpace.service.routing.impl;

import com.almostThere.middleSpace.domain.routetable.RouteInfo;
import com.almostThere.middleSpace.graph.edge.OwnEdge;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.domain.routetable.impl.DiRouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.routing.Router;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 다익스트라 알고리즘을 이용해 최소 경로 비용 테이블을 구하는 Router
 */
public class DiRouter implements Router {
    private final MapGraph mapGraph;

    public DiRouter(MapGraph mapGraph) {
        this.mapGraph = mapGraph;
    }

    public RouteTable getShortestPath(int startNodeIndex) {
        PriorityQueue<OwnEdge> pq = new PriorityQueue<>();
        int nodeNum = this.mapGraph.getNodeNum();
        RouteInfo[] dist = new RouteInfo[nodeNum];
        for (int i = 0 ; i < this.mapGraph.getNodeNum(); i++) {
            dist[i] = new RouteInfo();
        }
        dist[startNodeIndex].minCost = 0;
        dist[startNodeIndex].fromIndex = startNodeIndex;

        pq.offer(new OwnEdge(startNodeIndex, 0, "none"));
        while (!pq.isEmpty()) {
            OwnEdge current = pq.poll();

            if (dist[current.getIndex()].minCost < current.getCost()) {
                continue;
            }
            List<OwnEdge> adjacentNodes = mapGraph.getAdjacentNodes(current.getIndex());
            for (OwnEdge next : adjacentNodes) {
                if (dist[next.getIndex()].minCost > current.getCost() + next.getCost()) {
                    dist[next.getIndex()].minCost = current.getCost() + next.getCost();
                    dist[next.getIndex()].fromIndex = current.getIndex();
                    dist[next.getIndex()].line = next.getLine();
                    pq.offer(new OwnEdge(next.getIndex(), dist[next.getIndex()].minCost, next.getLine()));
                }
            }
        }
        return new DiRouteTable(startNodeIndex, dist, mapGraph);
    }
}
