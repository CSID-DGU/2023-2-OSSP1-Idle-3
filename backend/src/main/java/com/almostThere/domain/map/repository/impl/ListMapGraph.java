package com.almostThere.domain.map.repository.impl;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.MapGraph;
import java.util.List;
import java.util.Map;

public class ListMapGraph implements MapGraph {
    private List<MapNode>           actualNode;
    private Map<Integer, Integer>   map_to_id;
    private List<List<MapLink>> adjacentGraph;

    public ListMapGraph(
            List<MapNode> actualNode,
            Map<Integer, Integer> map_to_id,
            List<List<MapLink>> adjacentGraph
        ) {
        this.actualNode = actualNode;
        this.map_to_id = map_to_id;
        this.adjacentGraph = adjacentGraph;
    }

    @Override
    public List<MapLink> getAdjacentNodes(int index) {
        return this.adjacentGraph.get(index);
    }
}
