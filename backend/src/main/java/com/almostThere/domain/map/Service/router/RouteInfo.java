package com.almostThere.domain.map.Service.router;

public class RouteInfo implements Comparable<RouteInfo> {
    public int fromIndex;
    public double minCost;
    public RouteInfo() {
        this.fromIndex = -1;
        this.minCost = Double.MAX_VALUE;
    }
    public double getMinCost() {
        return minCost;
    }

    @Override
    public int compareTo(RouteInfo o) {
        return Double.compare(this.minCost, o.minCost);
    }
}