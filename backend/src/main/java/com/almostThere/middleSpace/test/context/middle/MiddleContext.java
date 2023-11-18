package com.almostThere.middleSpace.test.context.middle;

import com.almostThere.middleSpace.service.recommendation.MapGraphService;
import com.almostThere.middleSpace.service.recommendation.impl.MapGraphServiceImpl;
import com.almostThere.middleSpace.test.state.TestState;
import com.almostThere.middleSpace.test.state.middle.GetMiddlePoint;
import com.almostThere.middleSpace.test.state.middle.MiddlePointsInput;
import com.almostThere.middleSpace.test.context.Context;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.service.routing.impl.DiRouter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lombok.Getter;
import org.springframework.data.geo.Point;

@Getter
public class MiddleContext implements Context {
    private TestState state;
    private final Map<String, TestState> states;
    private final MapGraph mapGraph;
    private final MapGraphService service;
    private final List<Point> inputPoints;
    private final Router router;
    public MiddleContext(MapGraph mapGraph) {
        Scanner scanner = new Scanner(System.in);
        this.states = Map.of(
                "MiddlePointsInput", new MiddlePointsInput(this, scanner),
                "GetMiddlePoint", new GetMiddlePoint(this, scanner)
        );
        this.state = this.states.get("MiddlePointsInput");
        this.mapGraph = mapGraph;
        this.router = new DiRouter(mapGraph);
        this.service = new MapGraphServiceImpl(mapGraph, router);
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
}
