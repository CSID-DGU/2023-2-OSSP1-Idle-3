package com.almostThere.domain.map.repository.gisUtil;

import com.almostThere.domain.map.entity.node.MapNode;

public class GIS {
    private static final double EARTH_RADIUS = 6371000; // 지구 반지름 (미터 단위)
    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
    public static double getDistance(double startLat, double startLong,  MapNode node) {
        double dLat  = Math.toRadians(node.getLatitude() - startLat);
        double dLong = Math.toRadians(node.getLongitude() - startLong);

        startLat = Math.toRadians(startLat);
        double endLat   = Math.toRadians(node.getLatitude());

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // 결과는 미터 단위
    }
}
