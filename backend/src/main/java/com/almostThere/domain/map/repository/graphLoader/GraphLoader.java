package com.almostThere.domain.map.repository.graphLoader;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import java.util.List;

public interface GraphLoader {
    List<MapNode> loadNodes(GRAPH_FILE graphFile);
    List<MapLink> loadEdges(GRAPH_FILE graphFile);
}
