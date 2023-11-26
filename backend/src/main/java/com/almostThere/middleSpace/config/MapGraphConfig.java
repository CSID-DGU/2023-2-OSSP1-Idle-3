package com.almostThere.middleSpace.config;

import com.almostThere.middleSpace.graph.edge.MapEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.builder.MapGraphBuilder;
import com.almostThere.middleSpace.graph.builder.impl.ListMapGraphBuilder;
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
        List<String> nodeFiles = List.of(
                "step_node.json",

                "bus_stop_node_with_transfer.json",

                "subway_node.json"
//                "new_subway_node.json"
//                ,
//                "gate_location_in_seoul_ver2.json"

        );
        // 간선 파일 리스트
        List<String> edgeFiles = List.of(
                "step_link.json",

                "bus_router_edge_with_transfer.json",
                "bus_step_edge.json",

                "subway_edge.json",
                "subway_edge_id.json",
                "subway_edge_line9.json",
                "gate_nearest_step_edge.json",
                "gate_edge.json"
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
