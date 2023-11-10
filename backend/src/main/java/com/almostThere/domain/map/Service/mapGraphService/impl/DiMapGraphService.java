package com.almostThere.domain.map.Service.mapGraphService.impl;

import com.almostThere.domain.map.Service.mapGraphService.AverageCost;
import com.almostThere.domain.map.Service.mapGraphService.MapGraphService;
import com.almostThere.domain.map.Service.router.RouteInfo;
import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.Service.routerFactory.RouterFactory;
import com.almostThere.domain.map.Service.routerFactory.impl.DiRouterFactory;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
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
    private static List<AverageCost> getAverageGap(int numberOfStartPoints, int nodeNum, RouteInfo[][] routeTables) {
        AverageCost[] avgCost = new AverageCost[nodeNum];

        for (int i = 0; i < nodeNum; i++) {
            double sum = 0;

            int startPoint;
            for (startPoint = 0 ; startPoint < numberOfStartPoints; startPoint++) {
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
    @Override
    public List<AverageCost> findMiddleSpace(List<Point> startPoints) {
        RouterFactory routerFactory = new DiRouterFactory(mapGraph);
        List<Router> routers = routerFactory.createRouters(startPoints);
        return findMiddleSpaceWithRouter(routers);
    }

    @Override
    public List<AverageCost> findMiddleSpaceWithRouter(List<Router> routers) {
        final int numberOfStartPoints = routers.size();
        final int nodeNum = this.mapGraph.getNodeNum();
        RouteInfo[][] routeTables = new RouteInfo[numberOfStartPoints][nodeNum];

        for (int i = 0; i < numberOfStartPoints; i++) {
            routeTables[i] = routers.get(i).getShortestPath();
        }
        return getAverageGap(numberOfStartPoints, nodeNum, routeTables);
    }
}
