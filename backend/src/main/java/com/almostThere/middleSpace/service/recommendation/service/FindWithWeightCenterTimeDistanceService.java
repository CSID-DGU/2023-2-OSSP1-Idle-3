package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.web.dto.FinalTestResult;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithWeightCenterTimeDistanceService extends AbstractMiddleSpaceFindWithCostService {
    public FindWithWeightCenterTimeDistanceService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }

    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
        Position center = getCenterOfPosition(startPoints);
        Integer nearestId = this.mapGraph.findNearestId(center.getLatitude(), center.getLongitude());

        List<RouteTable> routeTables = router.getRouteTables(startPoints);
        List<AverageCost> results = getAverageGap(routeTables);
        // 최대 최소 시간 구하기
        double alpha = getAlpha(nearestId, routeTables);
        // 정규화하기
        List<AverageCost> normalizedResults = normalize(results);
        // 최소 코스트 자체 구하기
        double minCost = normalizedResults.stream()
                .mapToDouble(result -> cost(result.getSum(), result.getCost(), alpha))
                .min()
                .orElseThrow(NoSuchElementException::new);
        // 코스트 기준으로 정렬하기
        List<AverageCost> sortedCostList = results.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());
        return Result.builder()
                .result(results)
                .normalizedResult(sortedCostList)
                .middle(center)
                .alpha(alpha)
                .cost(minCost)
                .build();
    }

    private static double getAlpha(Integer nearestId, List<RouteTable> routeTables) {
        List<Double> times = routeTables.stream()
                .mapToDouble(routeTable -> routeTable.getCost(nearestId))
                .boxed().collect(Collectors.toList());
        Double minTimes = times.stream().min(Double::compareTo)
                .orElseThrow(NoSuchElementException::new);
        Double maxTimes = times.stream().max(Double::compareTo)
                .orElseThrow(NoSuchElementException::new);
        return minTimes / (maxTimes + minTimes);
    }

    public FinalTestResult findMiddleSpaceWithRouter(List<RouteTable> routeTables, List<Position> startPoints){
        Position center = getCenterOfPosition(startPoints);
        Integer nearestId = this.mapGraph.findNearestId(center.getLatitude(), center.getLongitude());

        double alpha = getAlpha(nearestId, routeTables);
        List<AverageCost> averageGap = getAverageGap(routeTables);
        List<AverageCost> cloned = averageGap.stream()
                .map(AverageCost::clone)
                .collect(Collectors.toList());
        List<AverageCost> normalizedResults = normalize(cloned);

        // score 기준 가장 작은 노드 찾기
        List<AverageCost> sortedCostList = normalizedResults.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());
        AverageCost selectedCost = sortedCostList.get(0);
        MapNode node = selectedCost.getNode();

        // 원본 값에서 해당 node에 대한 값 찾기
        AverageCost cost = averageGap.stream()
                .filter(item -> item.getNode().getMap_id() == node.getMap_id())
                .findFirst().orElseThrow(NoSuchElementException::new);

        return FinalTestResult.builder()
                .gap(cost.getCost())
                .sum(cost.getSum())
                .end(new Position(node.getLatitude(), node.getLongitude()))
                .build();
    }

    @Override
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        Result result = this.findMiddleSpaceTest(startPoints);
        return result.getResult();
    }

    /**
     * 시작점 좌표 기준으로 무게 중심 좌표를 구하는 함수
     * @param startPoints
     * @return
     */
    protected Position getCenterOfPosition(List<Position> startPoints) {
        double latitude = 0.0f;
        double longitude = 0.0f;
        int size = startPoints.size();

        for (Position position : startPoints) {
            latitude += position.getLatitude();
            longitude += position.getLongitude();
        }
        return new Position(latitude / size, longitude / size);
    }
}
