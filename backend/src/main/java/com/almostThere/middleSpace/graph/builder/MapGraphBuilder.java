package com.almostThere.middleSpace.graph.builder;

import com.almostThere.middleSpace.graph.edge.MapEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.MapGraph;
import java.util.List;

public interface MapGraphBuilder {
    MapGraph build(List<MapNode> nodes, List<MapEdge> links);
}
