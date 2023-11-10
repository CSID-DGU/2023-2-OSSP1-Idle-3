package com.almostThere.domain.map.Service.test.context.middle;

import com.almostThere.domain.map.Service.mapGraphService.MapGraphService;
import com.almostThere.domain.map.Service.mapGraphService.impl.DiMapGraphService;
import com.almostThere.domain.map.Service.test.state.TestState;
import com.almostThere.domain.map.Service.test.state.middle.GetMiddlePoint;
import com.almostThere.domain.map.Service.test.state.middle.MiddlePointsInput;
import com.almostThere.domain.map.Service.test.context.Context;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.geo.Point;

public class MiddleContext implements Context {
    private TestState state;
    private final Map<String, TestState> states;
    private final MapGraph mapGraph;
    private final MapGraphService service;
    private final List<Point> inputPoints;
    public MiddleContext(MapGraph mapGraph) {
        this.states = Map.of(
                "MiddlePointsInput", new MiddlePointsInput(this),
                "GetMiddle", new GetMiddlePoint(this)
        );
        this.state = this.states.get("MiddlePointsInput");
        this.mapGraph = mapGraph;
        this.service = new DiMapGraphService(mapGraph);
        this.inputPoints = new ArrayList<>();
    }

    @Override
    public void action() {
        this.state.action();
    }

    @Override
    public void setState(String state) {
        this.state = this.states.get(state);
    }

    public List<Point> getInputPoints() {
        return inputPoints;
    }

    public MapGraph getMapGraph() {return this.mapGraph;}

    public MapGraphService getService() {
        return service;
    }
}
