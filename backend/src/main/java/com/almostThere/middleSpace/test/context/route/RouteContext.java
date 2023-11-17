package com.almostThere.middleSpace.test.context.route;

import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.service.routing.impl.DiRouter;
import com.almostThere.middleSpace.test.state.TestState;
import com.almostThere.middleSpace.test.context.Context;
import com.almostThere.middleSpace.test.state.route.ShowPath;
import com.almostThere.middleSpace.test.state.route.SourceInput;
import com.almostThere.middleSpace.graph.MapGraph;
import java.util.Map;

/**
 * 길 찾기 알고리즘을 실험하기 위한 Context
 */
public class RouteContext implements Context {
    private final MapGraph mapGraph;
    private final Map<String, TestState> states;
    private TestState state;
    private RouteTable table;

    public RouteContext(MapGraph mapGraph) {
        this.mapGraph = mapGraph;
        // 한 State에서 String 으로 약하게 결함하여 다른 State으로 바꿀 수 있게함
        this.states = Map.of(
            "SourceInput", new SourceInput(this, new DiRouter(mapGraph)),
            "ShowPath", new ShowPath(this)
        );
        this.state = this.states.get("SourceInput");
    }

    @Override
    public void action() {
        this.state.action();
    }

    public MapGraph getMapGraph() {return this.mapGraph;}

    public void setTable(RouteTable table) {
        this.table = table;
    }
    public RouteTable getTable() {
        return this.table;
    }

    @Override
    public void setState(String key) {
        this.state = this.states.get(key);
    }
}
