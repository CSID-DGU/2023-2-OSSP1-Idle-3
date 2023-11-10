package com.almostThere.domain.map.Service.routerFactory.impl;

import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.Service.router.impl.DiRouter;
import com.almostThere.domain.map.Service.routerFactory.RouterFactory;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;

@RequiredArgsConstructor
public class DiRouterFactory implements RouterFactory {
    private final MapGraph mapGraph;

    @Override
    public List<Router> createRouters(List<Point> startPoints) {
        final int numberOfStartPoints = startPoints.size();
        final int nodeNum = this.mapGraph.getNodeNum();
        List<Router> routers = new ArrayList<>();

        for (int i = 0; i < numberOfStartPoints; i++) {
            Point point = startPoints.get(i);
            Integer startNode = this.mapGraph.findNearestId(point.getY(), point.getX());
            MapNode mapNode = this.mapGraph.findMapNode(startNode);
            System.out.printf("%d(%f, %f) 와 가장 가까운 노드는 : %s (위도 : %f, 경도 : %f) 입니다.\n",
                    i,point.getY(), point.getX(),
                    mapNode.getName(), mapNode.getLatitude(), mapNode.getLongitude()
            );
            routers.add(new DiRouter(nodeNum, startNode, mapGraph));
        }

        for (int i = 0; i < numberOfStartPoints; i++) {
            Router router = routers.get(i);
            router.getShortestPath();
        }
        return routers;
    }
}
