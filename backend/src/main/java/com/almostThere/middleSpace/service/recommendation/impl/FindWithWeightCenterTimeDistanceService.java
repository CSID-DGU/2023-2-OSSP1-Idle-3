package com.almostThere.middleSpace.service.recommendation.impl;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithWeightCenterTimeDistanceService extends FindWithWeightCenterService{
    public FindWithWeightCenterTimeDistanceService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }

    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
        Position center = super.getCenterOfPosition(startPoints);
        Integer nearestId = this.mapGraph.findNearestId(center.getLatitude(), center.getLongitude());

        List<RouteTable> routeTables = router.getRouteTables(startPoints);
        List<AverageCost> results = getAverageGap(routeTables);
        // 최대 최소 시간 구하기
        List<Double> times = routeTables.stream()
                .mapToDouble(routeTable -> routeTable.getCost(nearestId))
                .boxed().collect(Collectors.toList());
        Double minTimes = times.stream().min(Double::compareTo)
                .orElseThrow(NoSuchElementException::new);
        Double maxTimes = times.stream().max(Double::compareTo)
                .orElseThrow(NoSuchElementException::new);
        double alpha = minTimes / (maxTimes + minTimes);
        // 정규화하기
        normalize(results);
        // 최소 코스트 자체 구하기
        double minCost = results.stream()
                .mapToDouble(result -> cost(result.getSum(), result.getCost(), alpha))
                .min()
                .orElseThrow(NoSuchElementException::new);
        // 코스트 기준으로 정렬하기
        List<AverageCost> sortedCostList = results.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());
        return Result.builder().result(sortedCostList)
                .middle(center)
                .alpha(alpha)
                .cost(minCost)
                .build();
    }
}