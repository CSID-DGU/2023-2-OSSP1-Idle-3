package com.almostThere.domain.map.Service.mapGraphService.impl;

import com.almostThere.domain.map.Service.mapGraphService.AverageCost;
import com.almostThere.domain.map.Service.mapGraphService.MapGraphService;
import com.almostThere.domain.map.Service.router.RouteInfo;
import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.Service.router.impl.DiRouter;
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
            routers.add(new DiRouter(nodeNum, startNode, mapGraph));
        }

        for (int i = 0; i < numberOfStartPoints; i++) {
            Router router = routers.get(i);
            RouteInfo[] shortestPath = router.getShortestPath();
            routeTables[i] = shortestPath;
        }

        for (int i = 0; i < nodeNum ; i++) {
            Double sum = Double.valueOf(0);
            for (int startPoint = 0 ; startPoint < numberOfStartPoints ; startPoint++) {
                if (routeTables[startPoint][i].equals(Double.MAX_VALUE)) {
                    avgCost[i] = new AverageCost(-1, Double.MAX_VALUE);
                    break;
                }
                sum = sum + routeTables[startPoint][i].minCost;
            }
            avgCost[i] = new AverageCost(i, sum / numberOfStartPoints);
        }
        Arrays.sort(avgCost, Comparator.comparingDouble(AverageCost::getCost));
        return Arrays.stream(avgCost).collect(Collectors.toList());
    }
}
