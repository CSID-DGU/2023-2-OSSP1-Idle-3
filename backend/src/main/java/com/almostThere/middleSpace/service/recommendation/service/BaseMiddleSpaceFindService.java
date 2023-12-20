package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AggregatedResult;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.web.dto.FinalTestResult;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BaseMiddleSpaceFindService extends AbstractMiddleSpaceFindService {
    public BaseMiddleSpaceFindService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }

    @Override
    public Position findMiddleSpace(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        List<AggregatedResult> result = aggregateAndSortWithSum(tables);
        MapNode selectedNode = result.get(0).getNode();
        return new Position(selectedNode.getLatitude(), selectedNode.getLongitude());
    }

    public FinalTestResult findMiddleSpaceWithRouterAndSum(List<RouteTable> routeTables) {
        List<AggregatedResult> averageSum = aggregateAndSortWithSum(routeTables);
        if (averageSum.isEmpty())
            throw new NoSuchElementException();
        AggregatedResult cost = averageSum.get(0);
        MapNode node = cost.getNode();
        return FinalTestResult.builder()
                .sum(cost.getSum())
                .gap(cost.getCost())
                .end(new Position(node.getLatitude(), node.getLongitude()))
                .build();
    }

    public FinalTestResult findMiddleSpaceOriginal(List<RouteTable> routeTables, List<Position> startPoints) {
        int size = startPoints.size();
        double avgLat = startPoints.stream().mapToDouble(Position::getLatitude).sum() / size;
        double avgLng = startPoints.stream().mapToDouble(Position::getLongitude).sum() / size;
        Integer nearestId = this.mapGraph.findNearestId(avgLat, avgLng);

        List<Double> times = routeTables.stream()
                .mapToDouble(routeTable -> routeTable.getCost(nearestId))
                .boxed()
                .collect(Collectors.toList());
        Double sum = times.stream().reduce(0.0, Double::sum);
        Double sumAvg = sum / size;

        List<Double> gap = times.stream()
                .map(time -> Math.abs(time - sumAvg))
                .collect(Collectors.toList());
        Double gapAvg = gap.stream().reduce(0.0, Double::sum) / size;
        MapNode mapNode = this.mapGraph.findMapNode(nearestId);
        return FinalTestResult.builder()
                .gap(gapAvg)
                .sum(sumAvg)
                .end(new Position(mapNode.getLatitude(), mapNode.getLongitude()))
                .build();
    }
}
