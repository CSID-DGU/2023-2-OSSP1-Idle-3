package com.almostThere.domain.map.repository.mapGraph;

import com.almostThere.domain.map.entity.link.OwnLink;
import java.util.List;

public interface MapGraph {
    List<OwnLink> getAdjacentNodes(int index);
    int getNodeNum();
    Integer findSearchId(Long mapId);
    Long findMapId(Integer searchId);
}
