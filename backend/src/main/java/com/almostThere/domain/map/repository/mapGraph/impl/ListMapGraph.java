package com.almostThere.domain.map.repository.mapGraph.impl;

import com.almostThere.domain.map.entity.link.OwnLink;
import com.almostThere.domain.map.entity.node.MapNode;
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
        Integer searchId = this.map_to_id.get(mapId);
        return searchId;
    }
    @Override
    public Long findMapId(Integer searchId) {
        return this.actualNode.get(searchId).getMap_id();
    }
}
