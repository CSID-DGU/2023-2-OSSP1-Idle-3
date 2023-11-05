package com.almostThere.domain.map.Service.router.impl;

import com.almostThere.domain.map.Service.router.RouteInfo;
import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.entity.link.OwnLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import java.util.List;
import java.util.PriorityQueue;

public class DiRouter implements Router {
    private final RouteInfo[] dist;
//    private Boolean[] visited;
    private final Integer startNode;
    private final MapGraph mapGraph;
    private final PriorityQueue<OwnLink> pq;

    public DiRouter(int nodeNum, int startNode, MapGraph mapGraph) {
        this.pq = new PriorityQueue<>();
        this.dist = new RouteInfo[nodeNum];
        for (int i = 0 ; i < nodeNum; i++) {
            this.dist[i] = new RouteInfo();
        }
//        this.visited = new Boolean[nodeNum];
        this.startNode = startNode;
        this.mapGraph = mapGraph;
    }

    public RouteInfo[] getShortestPath() {
        dist[startNode].minCost = 0;
        dist[startNode].fromIndex = startNode;
//        visited[startNode] = true;

        pq.offer(new OwnLink(startNode, 0));
        while (!pq.isEmpty()) {
            OwnLink current = pq.poll();

            if (dist[current.getIndex()].minCost < current.getCost()) {
                continue;
            }
            List<OwnLink> adjacentNodes = mapGraph.getAdjacentNodes(current.getIndex());
            for (OwnLink next : adjacentNodes) {
                if (dist[next.getIndex()].minCost > current.getCost() + next.getCost()) {
                    dist[next.getIndex()].minCost = current.getCost() + next.getCost();
                    dist[next.getIndex()].fromIndex = current.getIndex();
                    pq.offer(new OwnLink(next.getIndex(), dist[next.getIndex()].minCost));
                }
            }
        }
        return dist;
    }

    public void showPath(Long map_dest_id) {
        Integer search_dest_id = this.mapGraph.findSearchId(map_dest_id);
        while (!search_dest_id.equals(this.startNode)) {
            MapNode destNode = this.mapGraph.findMapNode(search_dest_id);
            int fromSearchIndex = dist[search_dest_id].fromIndex;
            if (fromSearchIndex == -1) {
                System.out.printf("%s에는 갈 수 없습니다.\n", destNode.getName());
                break ;
            }else {
                MapNode srcNode = this.mapGraph.findMapNode(fromSearchIndex);
                System.out.printf("%s -> %s\n", srcNode.getName(), destNode.getName());
                search_dest_id = fromSearchIndex;
            }
        }
    }
}

