package com.almostThere.middleSpace.service.routing;

import com.almostThere.middleSpace.domain.routetable.RouteTable;

public interface Router {
    RouteTable getShortestPath(int startNodeIndex);
}
