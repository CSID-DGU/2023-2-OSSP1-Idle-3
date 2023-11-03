package com.almostThere.domain.map.repository;

import com.almostThere.domain.map.entity.MapNode;
import java.util.List;

public interface MapGraph {
    List<MapNode> getAdjacentNodes(int index);
}
