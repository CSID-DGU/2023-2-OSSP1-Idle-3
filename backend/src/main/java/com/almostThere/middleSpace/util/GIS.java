package com.almostThere.middleSpace.util;

import com.almostThere.middleSpace.domain.gis.Boundary;
import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.graph.node.MapNode;

/**
 * GIS 관련 계산 공식을 모와서 관리하는 클래스 static 함수로만 이루어짐
 * */
public class GIS {
    private static final double EARTH_RADIUS = 6371000; // 지구 반지름 (미터 단위)

    private static final double THREE_METER_TO_ANGLE = 0.000027; // 3m를 각도로 변환한 값
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
    public static double getDistance(Position position, MapNode mapNode) {
        return getDistance(position.getLatitude(), position.getLongitude(), mapNode);
    }
    public static double getDistance(MapNode node1, MapNode node2) {
        return getDistance(
                node1.getLatitude(), node1.getLongitude(),
                node2.getLatitude(), node2.getLongitude()
        );
    }

    public static double getDistance(double startLat, double startLong, double endLat, double endLong) {
        double dLat  = Math.toRadians(endLat - startLat);
        double dLong = Math.toRadians(endLong - startLong);

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // 결과는 미터 단위
    }


    // 1. 3m가 위도, 경도 상으로 얼마나 차이나는지 계산
    public static Boundary getWhat3WordsMapBoundaryPoint(double maxLatitude, double minLatitude, double maxLongitude, double minLongitude){
        // what3words 가 반영된 경계값을 구하기.
        double max3WordsLatitude = Double.MIN_VALUE;
        double min3WordsLatitude = Double.MAX_VALUE;
        double max3WordsLongitude = Double.MIN_VALUE;
        double min3WordsLongitude = Double.MAX_VALUE;

        max3WordsLatitude = Math.floor(maxLatitude / THREE_METER_TO_ANGLE) * THREE_METER_TO_ANGLE + THREE_METER_TO_ANGLE;
        min3WordsLatitude = Math.floor(minLatitude / THREE_METER_TO_ANGLE) * THREE_METER_TO_ANGLE;
        max3WordsLongitude = Math.floor(maxLongitude / THREE_METER_TO_ANGLE) * THREE_METER_TO_ANGLE + THREE_METER_TO_ANGLE;
        min3WordsLongitude = Math.floor(minLongitude / THREE_METER_TO_ANGLE) * THREE_METER_TO_ANGLE;

        return new Boundary(min3WordsLatitude, max3WordsLatitude, min3WordsLongitude, max3WordsLongitude);
    }

}
