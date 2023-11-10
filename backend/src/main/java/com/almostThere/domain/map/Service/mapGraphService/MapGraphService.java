package com.almostThere.domain.map.Service.mapGraphService;

import java.util.List;
import org.springframework.data.geo.Point;

public interface MapGraphService {

    public List<AverageCost> findMiddleSpace(List<Point> startPoints);
}
