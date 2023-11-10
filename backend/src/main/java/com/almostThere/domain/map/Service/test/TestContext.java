package com.almostThere.domain.map.Service.test;

import com.almostThere.domain.map.Service.test.context.middle.MiddleContext;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import com.almostThere.domain.map.repository.mapGraphConfig.MapGraphConfig;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public class TestContext {
    public static void main(String[] args) throws IOException, ParseException {
        MapGraphConfig config = new MapGraphConfig();
        MapGraph mapGraph = config.mapGraph();
        MiddleContext context = new MiddleContext(mapGraph);
        while (true) {
            context.action();
        }
    }
}
