package com.almostThere.middleSpace.domain.routetable;

/**
 * 도착 노드까지 걸리는 최소 시간과
 * 무슨 매체를 이동해서 갔는지 저장하는 객체
 * cost를 이용해 priority queue에서 정렬된다.
 */
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