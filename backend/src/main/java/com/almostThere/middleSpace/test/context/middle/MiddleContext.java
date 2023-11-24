package com.almostThere.middleSpace.test.context.middle;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.test.state.TestState;
import com.almostThere.middleSpace.test.state.middle.CalculateState;
import com.almostThere.middleSpace.test.state.middle.InputState;
import com.almostThere.middleSpace.test.context.Context;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.test.state.middle.VisualizeState;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lombok.Getter;

@Getter
public class MiddleContext implements Context {
    private TestState state;
    private final Map<String, TestState> states;
    private final MapGraph mapGraph;
    private List<Position> inputPoints;
    private List<AverageCost> middleSpaces;
    private List<RouteTable> tables;

    public MiddleContext(MapGraph mapGraph) {
        Scanner scanner = new Scanner(System.in);
        this.mapGraph = mapGraph;
        this.states = Map.of(
                "InputState", new InputState(this, scanner),
                "CalculateState", new CalculateState(this, mapGraph),
                "VisualizeState", new VisualizeState(this, scanner)
        );
        this.state = this.states.get("MiddlePointsInput");

        this.inputPoints = null;
        this.middleSpaces = null;
        this.tables = null;
    }

    @Override
    public void action() {
        this.state.action();
    }

    @Override
    public void setState(String state) {
        this.state = this.states.get(state);
    }

    public void updateMiddleSpace(List<AverageCost> middleSpaces) {
        this.middleSpaces = middleSpaces;
    }
    public void updateRouteTables(List<RouteTable> tables) {
        this.tables = tables;
    }
    public void updateInputPoints(List<Position> inputPoints) {
        this.inputPoints = inputPoints;
    }
}
