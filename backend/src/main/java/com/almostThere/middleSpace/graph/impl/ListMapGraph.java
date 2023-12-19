package com.almostThere.middleSpace.graph.impl;

import com.almostThere.middleSpace.graph.edge.OwnEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.util.GIS;
import java.util.List;
import java.util.Map;

public class ListMapGraph implements MapGraph {
    private final List<MapNode> actualNode;
    private final Map<Long, Integer> map_to_id;
    private final List<OwnEdge>[] adjacentGraph;

    public ListMapGraph(
            List<MapNode> actualNode,
            Map<Long, Integer> map_to_id,
            List<OwnEdge>[] adjacentGraph
        ) {
        this.actualNode = actualNode;
        this.map_to_id = map_to_id;
        this.adjacentGraph = adjacentGraph;
    }

    @Override
    public List<OwnEdge> getAdjacentNodes(int index) {
        return this.adjacentGraph[index];
    }

    @Override
    public int getNodeNum() {
        return this.adjacentGraph.length;
    }

    @Override
    public Integer findSearchId(Long mapId) {
        return this.map_to_id.get(mapId);
    }

    @Override
    public MapNode findMapNode(Integer searchId) {
        return this.actualNode.get(searchId);
    }

    /**
     * 주어진 위도 경도와 가장 가까운 도보 노드의 id를 반환
     * @param latitude 위도
     * @param longitude 경도
     * @return 가장 가까운 도보 id
     */
    @Override
    public Integer findNearestId(Double latitude, Double longitude) {
        double length = Double.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0 ; i < adjacentGraph.length ; i++) {
            MapNode mapNode = this.actualNode.get(i);
            if (mapNode.getMap_id() < 1000000 || mapNode.getMap_id() > 1000000000)
                continue;
            double distance = GIS.getDistance(latitude, longitude, mapNode);
            if (distance < length) {
                length = distance;
                minIndex = i;
            }
        }
        return minIndex;
    }

    @Override
    public MapNode getNode(Integer searchId) {
        return this.actualNode.get(searchId);
    }
}
