package com.almostThere.middleSpace.domain.gis;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Path {
    private final Position startPoint;
    private final List<Edge> routes;
    private final Double cost;
}
