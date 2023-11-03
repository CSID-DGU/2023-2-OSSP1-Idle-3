package com.almostThere.domain.map.repository;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import java.util.List;

public interface MapGraph {
    List<MapLink> getAdjacentNodes(int index);
}
