package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithTimeWeightCenterService extends AbstractMiddleSpaceFindWithCostService {
    public FindWithTimeWeightCenterService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }
    @Override
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        Result result = this.findMiddleSpaceTest(startPoints);
        return result.getResult();
    }
    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        List<AverageCost> results = getAverageGap(tables);
        // 중심을 구함
        Position center = getCenterOfTimeWeightCenter(tables);
        // 해당 중심을 기준으로 가중치를 구함
        MapNode nearestWalkNode = this.mapGraph.findNearestWalkNode(center.getLatitude(), center.getLongitude());
        Integer searchId = this.mapGraph.findSearchId(nearestWalkNode.getMap_id());
        double maxTime = tables.stream().mapToDouble(table -> table.getCost(searchId))
                .max().orElseThrow(NoSuchElementException::new);
        double minTime = tables.stream().mapToDouble(table -> table.getCost(searchId))
                .min().orElseThrow(NoSuchElementException::new);
        double alpha = minTime / (maxTime + minTime);
        System.out.println(alpha);
        // 정규화
        normalize(results);
        // cost 기준 정렬하기
        double minScore = results.stream()
                .mapToDouble(result -> cost(result.getSum(), result.getCost(), alpha))
                .min()
                .orElseThrow(NoSuchElementException::new);
        List<AverageCost> costList = results.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());

        return Result.builder().result(costList)
                .middle(center)
                .alpha(alpha)
                .cost(minScore)
                .build();
    }


    /**
     * 시작점 좌표 + 각 시작점 사이의 가는 거리를 가중치로 하는 무게 중심좌표
     * @param routeTables
     * @return
     */
    private Position getCenterOfTimeWeightCenter(List<RouteTable> routeTables) {
        List<MapNode> startPoints = routeTables.stream()
                .map(RouteTable::getStartNode)
                .collect(Collectors.toList());
        int size = startPoints.size();

        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < size ; i++) {
            RouteTable routeTable = routeTables.get(i);
            double sum = 0.0f;
            for (int j = 0 ; j < size ; j++) {
                if (i != j) continue;
                MapNode endNode = startPoints.get(j);
                double distance = routeTable.getCost(this.mapGraph.findSearchId(endNode.getMap_id()));
                sum += distance;
            }
            weights.add(sum / size);
        }

        double lat = 0.0, log = 0.0;
        for (int i = 0 ; i < size; i++) {
            MapNode node = startPoints.get(i);
            lat += (weights.get(i) * node.getLatitude());
            log += (weights.get(i) * node.getLongitude());
        }
        return new Position(lat / size, log / size);
    }

}
