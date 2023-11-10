package com.almostThere.domain.map.Service.mapGraphService.impl;

import com.almostThere.domain.map.Service.mapGraphService.AverageCost;
import com.almostThere.domain.map.Service.mapGraphService.MapGraphService;
import com.almostThere.domain.map.Service.router.RouteInfo;
import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.Service.router.impl.DiRouter;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiMapGraphService implements MapGraphService {
    private final MapGraph mapGraph;

    @Override
    public List<AverageCost> findMiddleSpace(List<Point> startPoints) {
        final int numberOfStartPoints = startPoints.size();
        final int nodeNum = this.mapGraph.getNodeNum();
        List<Router> routers = new ArrayList<>();
        RouteInfo[][] routeTables = new RouteInfo[numberOfStartPoints][nodeNum];
        AverageCost[] avgCost = new AverageCost[nodeNum];

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
            routeTables[i] = router.getShortestPath();
        }

        for (int i = 0; i < nodeNum ; i++) {
            double sum = 0;

            int startPoint;
            for (startPoint = 0 ; startPoint < numberOfStartPoints ; startPoint++) {
                if (routeTables[startPoint][i].minCost == Double.MAX_VALUE) {
                    break;
                }
                sum = sum + routeTables[startPoint][i].minCost;
            }
            // 셋 다 모두 갈 수 있는 경우
            if (startPoint == numberOfStartPoints) {
                double averageTime = sum / numberOfStartPoints;
                double avgGap = 0;
                int innerStartPoint;
                for (innerStartPoint = 0; innerStartPoint < numberOfStartPoints; innerStartPoint++) {
                    avgGap += Math.abs(routeTables[innerStartPoint][i].minCost - averageTime);
                    avgGap = avgGap / numberOfStartPoints;
                }
                avgCost[i] = new AverageCost(i, avgGap);
            }else {
                avgCost[i] = new AverageCost(i, Double.MAX_VALUE);
            }
        }
        return Arrays.stream(avgCost)
                .filter(averageCost -> averageCost.getCost() != Double.MAX_VALUE)
                .sorted(Comparator.comparingDouble(AverageCost::getCost))
                .collect(Collectors.toList());
    }
}
