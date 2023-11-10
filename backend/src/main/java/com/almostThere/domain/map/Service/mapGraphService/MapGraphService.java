package com.almostThere.domain.map.Service.mapGraphService;

import com.almostThere.domain.map.Service.router.Router;
import java.util.List;
import org.springframework.data.geo.Point;

public interface MapGraphService {

    List<AverageCost> findMiddleSpace(List<Point> startPoints);
    List<AverageCost> findMiddleSpaceWithRouter(List<Router> routers);
}
