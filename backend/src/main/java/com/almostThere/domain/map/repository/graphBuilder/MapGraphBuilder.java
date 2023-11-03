package com.almostThere.domain.map.repository.graphBuilder;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.MapGraph;
import java.util.List;

public interface MapGraphBuilder {
    MapGraph build(List<MapNode> nodes, List<MapLink> links);
}
