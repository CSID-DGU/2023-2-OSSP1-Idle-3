package com.almostThere.domain.map.repository.impl;

import com.almostThere.domain.map.entity.MapNode;
import com.almostThere.domain.map.repository.MapGraph;
import java.util.List;

public class ListMapGraph implements MapGraph {
    private List<List<MapNode>> adjacentGraph;

    public ListMapGraph(List<List<MapNode>> graph) {
        this.adjacentGraph = graph;
    }

    @Override
    public List<MapNode> getAdjacentNodes(int index) {
        return this.adjacentGraph.get(index);
    }
}
