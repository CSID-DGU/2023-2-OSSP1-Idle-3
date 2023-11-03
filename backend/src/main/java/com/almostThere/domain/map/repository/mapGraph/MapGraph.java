package com.almostThere.domain.map.repository.mapGraph;

import com.almostThere.domain.map.entity.link.OwnLink;
import java.util.List;

public interface MapGraph {
    List<OwnLink> getAdjacentNodes(int index);
}
