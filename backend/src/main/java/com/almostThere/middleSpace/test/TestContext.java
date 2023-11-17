package com.almostThere.middleSpace.test;

import com.almostThere.middleSpace.test.context.middle.MiddleContext;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.config.MapGraphConfig;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 * State-Context 패턴을 적용해서 만든 실험용 main 모듈
 * 쉽게 메뉴를 추가하기 위해 해당 패턴을 적용함
 */
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
