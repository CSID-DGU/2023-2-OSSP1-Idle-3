package com.almostThere.domain.map.Service.router;

public class RouteInfo implements Comparable<RouteInfo> {
    public int fromIndex;
    public String line;
    public double minCost;
    public RouteInfo() {
        this.fromIndex = -1;
        this.minCost = Double.MAX_VALUE;
        this.line = "none";
    }

    @Override
    public int compareTo(RouteInfo o) {
        return Double.compare(this.minCost, o.minCost);
    }
}