package com.almostThere.middleSpace.graph;

import com.almostThere.middleSpace.graph.edge.OwnEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import java.util.List;

public interface MapGraph {
    List<OwnEdge> getAdjacentNodes(int index);
    int getNodeNum();
    Integer findSearchId(Long mapId);
    MapNode findMapNode(Integer searchId);
    Integer findNearestId(Double latitude, Double longitude);
    MapNode getNode(Integer searchId);
}
