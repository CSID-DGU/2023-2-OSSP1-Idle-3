package com.almostThere.middleSpace.domain.gis;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Path {
    private final Position startPoint;
    private final List<Edge> routes;
}
