package com.almostThere.middleSpace.service.routing;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import java.util.List;

public interface Router {
    RouteTable getShortestPath(int startNodeIndex);
    List<RouteTable> getRouteTables(List<Position> positions);
}
