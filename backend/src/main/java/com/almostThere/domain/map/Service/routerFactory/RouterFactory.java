package com.almostThere.domain.map.Service.routerFactory;

import com.almostThere.domain.map.Service.router.Router;
import java.util.List;
import org.springframework.data.geo.Point;

public interface RouterFactory {
    List<Router> createRouters(List<Point> startPoints);
}
