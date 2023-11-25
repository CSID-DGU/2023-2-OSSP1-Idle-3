package com.almostThere.middleSpace.config;

import com.almostThere.middleSpace.graph.edge.MapEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.builder.MapGraphBuilder;
import com.almostThere.middleSpace.graph.builder.impl.ListMapGraphBuilder;
import com.almostThere.middleSpace.constant.FILENAME;
import com.almostThere.middleSpace.graph.loader.GraphLoader;
import com.almostThere.middleSpace.graph.loader.impl.JSONGraphLoader;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.service.routing.impl.DiRouter;
import java.io.IOException;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 중간 지점 선정 모듈에 필요한 Bean 등록 객체
 */
@Configuration
public class MapGraphConfig {

    /**
     * 파일에서 읽어온 노드와 간선 정보를 그래프로 구축
     * @return mapGraph
     */
    @Bean
    public MapGraph mapGraph() throws IOException, ParseException {
        GraphLoader graphLoader = new JSONGraphLoader();
        MapGraphBuilder graphBuilder = new ListMapGraphBuilder();

        // 노드 파일 리스트
        List<FILENAME> nodeFiles = List.of(
                FILENAME.STEP_NODE,

                FILENAME.BUS_STOP_NODE,
                FILENAME.SUBWAY_NODE
                ,
                FILENAME.STATION_GATE_NODE
        );
        // 간선 파일 리스트
        List<FILENAME> edgeFiles = List.of(
                FILENAME.STEP_EDGE,

                FILENAME.BUS_ROUTER_EDGE,
                FILENAME.BUS_STEP_EDGE,

                FILENAME.SUBWAY_EDGE
                ,
                FILENAME.SUBWAY_GATE_EDGE
                ,
                FILENAME.GATE_STEP_EDGE
        );

        List<MapNode> map_nodes = graphLoader.loadNodes(nodeFiles);
        List<MapEdge> map_edges = graphLoader.loadEdges(edgeFiles);
        System.out.printf("node 수는 %d개 입니다.\n", map_nodes.size());
        System.out.printf("edge 수는 %d개 입니다.\n", map_edges.size());
        return graphBuilder.build(map_nodes, map_edges);
    }

    @Bean
    public Router router(MapGraph mapGraph) {
        return new DiRouter(mapGraph);
    }
}
