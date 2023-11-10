package com.almostThere.domain.map.Service.test.context.route;

import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.Service.test.context.Context;
import com.almostThere.domain.map.Service.test.state.TestState;
import com.almostThere.domain.map.Service.test.state.route.ShowPath;
import com.almostThere.domain.map.Service.test.state.route.SourceInput;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import java.util.Map;

public class RouteContext implements Context {
    private final MapGraph mapGraph;
    private final Map<String, TestState> states;
    private Router router;
    private TestState state;

    public RouteContext(MapGraph mapGraph) {
        this.mapGraph = mapGraph;
        this.states = Map.of(
            "SourceInput", new SourceInput(this),
            "ShowPath", new ShowPath(this)
        );
        this.state = this.states.get("SourceInput");
    }

    @Override
    public void action() {
        this.state.action();
    }

    public MapGraph getMapGraph() {return this.mapGraph;}

    @Override
    public void setState(String key) {
        this.state = this.states.get(key);
    }

    public Router getRouter() {
        return this.router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
}
