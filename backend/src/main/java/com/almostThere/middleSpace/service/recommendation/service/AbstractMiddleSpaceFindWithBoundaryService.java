package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.routing.Router;

public abstract class AbstractMiddleSpaceFindWithBoundaryService extends AbstractMiddleSpaceFindService{
    public AbstractMiddleSpaceFindWithBoundaryService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }
}
