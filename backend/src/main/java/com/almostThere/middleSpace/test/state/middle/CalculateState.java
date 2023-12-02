package com.almostThere.middleSpace.test.state.middle;

import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.service.recommendation.MapGraphService;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.service.routing.impl.DiRouter;
import com.almostThere.middleSpace.test.state.TestState;
import com.almostThere.middleSpace.test.context.middle.MiddleContext;
import com.almostThere.middleSpace.graph.MapGraph;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * 중간 지점 선정 알고리즘을 실험 하기 위한 State
 */
@RequiredArgsConstructor
public class CalculateState implements TestState {
    private final MiddleContext context;
    private final MapGraphService service;
    private final Router router;

    public CalculateState(MiddleContext context, MapGraph mapGraph) {
        this.context = context;
        this.router = new DiRouter(mapGraph);
        this.service = new MapGraphService(mapGraph, router);
    }

    @Override
    public void action() {
        List<RouteTable> tables = context.getInputPoints().stream()
                .map(point -> this.context.getMapGraph().findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());

        Result middleSpace = service.findMiddleSpaceWithTablesAndCenter(tables);

        this.context.updateMiddleSpace(middleSpace.getResult());
        this.context.updateRouteTables(tables);
        this.context.setState("VisualizeState");
    }
}
