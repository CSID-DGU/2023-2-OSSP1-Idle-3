package com.almostThere.domain.map.repository.graphBuilder;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.MapGraph;

public interface MapGraphBuilder {
    void addNode(MapNode node);
    void addEdge(MapLink link);
    MapGraph load();
}
