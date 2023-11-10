package com.almostThere.domain.map.repository.mapGraph;

import com.almostThere.domain.map.entity.link.OwnLink;
import com.almostThere.domain.map.entity.node.MapNode;
import java.util.List;

public interface MapGraph {
    List<OwnLink> getAdjacentNodes(int index);
    int getNodeNum();
    Integer findSearchId(Long mapId);
    Long findMapId(Integer searchId);
    MapNode findMapNode(Integer searchId);
    Integer findNearestId(Double latitude, Double longitude);
    MapNode getNode(Integer searchId);
}
