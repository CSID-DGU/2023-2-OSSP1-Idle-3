package com.almostThere.middleSpace.domain.routetable.impl;

import com.almostThere.middleSpace.domain.gis.Edge;
import com.almostThere.middleSpace.domain.gis.Path;
import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteInfo;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class DiRouteTable implements RouteTable {
    private final int startNodeIndex;
    private final RouteInfo[] dist;
    private final MapGraph mapGraph;

    public DiRouteTable(int startNodeIndex, RouteInfo[] dist, MapGraph mapGraph) {
        this.startNodeIndex = startNodeIndex;
        this.dist = dist;
        this.mapGraph = mapGraph;
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
    public void showPath(Long map_dest_id) {
        Integer search_dest_id = this.mapGraph.findSearchId(map_dest_id);
        double cost = dist[search_dest_id].minCost;
        while (!search_dest_id.equals(startNodeIndex)) {
            MapNode destNode = this.mapGraph.findMapNode(search_dest_id);
            int fromSearchIndex = dist[search_dest_id].fromIndex;
            String line = dist[search_dest_id].line;
            if (fromSearchIndex == -1) {
                System.out.printf("%s에는 갈 수 없습니다.\n", destNode.getName());
                break ;
            }else {
                MapNode srcNode = this.mapGraph.findMapNode(fromSearchIndex);
                System.out.printf("%s - [%s] -> %s\n", srcNode.getName(), line, destNode.getName());
                search_dest_id = fromSearchIndex;
            }
        }
        System.out.printf("최단 시간은 %f초 입니다.\n", cost);
    }

    @Override
    public void showUnReachableNodeCount() {
        System.out.printf("가지 못하는 노드의 개수 : %d\n",
                Arrays.stream(dist)
                        .filter(routeInfo -> routeInfo.minCost == Double.MAX_VALUE)
                        .count());
    }

    @Override
    public Path extractPath(Integer dest_search_id) {
        List<Edge> result_route = new ArrayList<>();
        MapNode startNode = this.mapGraph.findMapNode(startNodeIndex);
        int temp = dest_search_id;
        RouteInfo routeInfo = dist[temp];
        double cost = routeInfo.minCost;
        while (temp != startNodeIndex) {
//            routeInfo = dist[temp];
            MapNode toNode = this.mapGraph.findMapNode(temp);
            MapNode fromNode = this.mapGraph.findMapNode(dist[temp].fromIndex);
            result_route.add(new Edge(
                    new Position(fromNode.getLatitude(), fromNode.getLongitude()),
                    new Position(toNode.getLatitude(), toNode.getLongitude())
//                    routeInfo.line
//                    routeInfo.type
            ));
            temp = dist[temp].fromIndex;
        }
        Collections.reverse(result_route);
        return Path.builder()
                .routes(result_route)
                .startPoint(new Position(startNode.getLatitude(), startNode.getLongitude()))
                .cost(cost)
                .build();
    }
}
