package com.almostThere.middleSpace.util;

import com.almostThere.middleSpace.graph.node.MapNode;

/**
 * GIS 관련 계산 공식을 모와서 관리하는 클래스 static 함수로만 이루어짐
 * */
public class GIS {
    private static final double EARTH_RADIUS = 6371000; // 지구 반지름 (미터 단위)
    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    // 두 좌표 사이의 거리를 미터 단위로 계산하는 함수
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
