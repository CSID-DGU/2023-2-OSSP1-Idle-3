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
}
