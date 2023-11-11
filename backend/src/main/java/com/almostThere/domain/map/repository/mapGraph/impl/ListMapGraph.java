package com.almostThere.domain.map.repository.mapGraph.impl;

import com.almostThere.domain.map.entity.link.OwnLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.gisUtil.GIS;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import java.util.List;
import java.util.Map;

public class ListMapGraph implements MapGraph {
    private final List<MapNode> actualNode;
    private final Map<Long, Integer> map_to_id;
    private final List<OwnLink>[] adjacentGraph;

    public ListMapGraph(
            List<MapNode> actualNode,
            Map<Long, Integer> map_to_id,
            List<OwnLink>[] adjacentGraph
        ) {
        this.actualNode = actualNode;
        this.map_to_id = map_to_id;
        this.adjacentGraph = adjacentGraph;
    }

    @Override
    public List<OwnLink> getAdjacentNodes(int index) {
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
    public Long findMapId(Integer searchId) {
        return this.actualNode.get(searchId).getMap_id();
    }

    @Override
    public MapNode findMapNode(Integer searchId) {
        return this.actualNode.get(searchId);
    }
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
