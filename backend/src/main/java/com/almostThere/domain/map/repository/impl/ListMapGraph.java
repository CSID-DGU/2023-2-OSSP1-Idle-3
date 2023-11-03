package com.almostThere.domain.map.repository.impl;

import com.almostThere.domain.map.entity.link.OwnLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.MapGraph;
import java.util.List;
import java.util.Map;

public class ListMapGraph implements MapGraph {
    private final List<MapNode> actualNode;
    private final Map<Integer, Integer> map_to_id;
    private final List<List<OwnLink>> adjacentGraph;

    public ListMapGraph(
            List<MapNode> actualNode,
            Map<Integer, Integer> map_to_id,
            List<List<OwnLink>> adjacentGraph
        ) {
        this.actualNode = actualNode;
        this.map_to_id = map_to_id;
        this.adjacentGraph = adjacentGraph;
    }

    @Override
    public List<OwnLink> getAdjacentNodes(int index) {
        return this.adjacentGraph.get(index);
    }
}
