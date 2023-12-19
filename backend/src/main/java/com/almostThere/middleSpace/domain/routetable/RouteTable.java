package com.almostThere.middleSpace.domain.routetable;

import com.almostThere.middleSpace.graph.node.MapNode;

public interface RouteTable extends Cloneable{
    MapNode getStartNode();

    double getCost(int search_dest_id);
    RouteInfo getRouteInfo(int search_dest_id);
    RouteTable clone();
}
