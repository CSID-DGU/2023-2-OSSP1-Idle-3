package com.almostThere.middleSpace.domain.routetable;

import com.almostThere.middleSpace.graph.node.MapNode;

public interface RouteTable {
    MapNode getStartNode();

    double getCost(int search_dest_id);
    void showPath(Long map_dest_id);
}
