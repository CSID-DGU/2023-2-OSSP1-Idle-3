package com.almostThere.domain.map.Service.router;

public interface Router {
    RouteInfo[] getShortestPath();
    public void showPath(Long map_dest_id);
}
