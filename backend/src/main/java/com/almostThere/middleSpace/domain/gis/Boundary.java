package com.almostThere.middleSpace.domain.gis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Boundary {
    public Double minLatitude;
    public Double maxLatitude;
    public Double minLongitude;
    public Double maxLongitude;
    public boolean include(Position position) {
        return minLongitude <= position.getLongitude() && position.getLongitude() <= maxLongitude &&
                minLatitude <= position.getLatitude() && position.getLatitude() <= maxLatitude;
    }
    public boolean include(Double longitude, Double latitude) {
        return include(new Position(longitude, latitude));
    }
}
