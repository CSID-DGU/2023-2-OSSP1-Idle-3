package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
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

    public FinalTestResult findMiddleSpaceWithRouter(List<RouteTable> routeTables, List<Position> startPoints){
        List<AverageCost> averageGap = getAverageGap(routeTables);

        List<AverageCost> normalizedList = normalize(averageGap);

        List<AverageCost> sortedCostList = sortWithAlpha(routeTables, startPoints, normalizedList);
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
    public Position findMiddleSpace(List<Position> startPoints) {
        List<RouteTable> routeTables = this.router.getRouteTables(startPoints);
        List<AverageCost> sortWithAlpha = sortWithAlpha(routeTables, startPoints);
        MapNode node = sortWithAlpha.get(0).getNode();
        return new Position(node.getLatitude(), node.getLongitude());
    }

    /**
     * 시작점 좌표 기준으로 무게 중심 좌표를 구하는 함수
     * @param startPoints 출발지 좌표 리스트
     * @return 출발지 좌표의 평균인 무게중심
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

    /**
     * alpha를 구하는 함수
     * @param nearestId 무게중심과 가장 가까운 출발점 노드 id
     * @param routeTables 길찾기 한 결과 리스트
     * @return 최소 시간 / (최소 시간 + 최대 시간)인 alpha 값
     */
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

    /**
     * test 데이터를 뽑기 위한 함수
     * 정규화된 후보군 리스트가 이미 있는 경우 alpha을 구해 cost를 만들어
     * cost를 오름차순으로 정렬한 리스트를 만든다.
     * @param routeTables 길찾기 한 결과 리스트
     * @param startPoints 시작점들
     * @param normalizedResults 정규화된 데이터 리스트
     * @return cost 기준으로 정렬된 정규화 데이터 리스트
     */
    private List<AverageCost> sortWithAlpha(List<RouteTable> routeTables, List<Position> startPoints,
                                            List<AverageCost> normalizedResults) {
        Position center = getCenterOfPosition(startPoints);
        Integer nearestNodeId = this.mapGraph.findNearestId(center.getLatitude(), center.getLongitude());
        double alpha = getAlpha(nearestNodeId, routeTables);
        // score 기준 가장 작은 노드 찾기
        return normalizedResults.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());
    }

    /**
     * alpha를 구하고 이를 이용해 cost를 만들어 이 기준으로 오름차순으로 정렬한 리스트 반환
     * 
     * @param routeTables 길찾기 한 결과 리스트
     * @param startPoints 시작점들
     * @return cost 기준으로 정렬한 정규화 데이터 리스트
     */
    private List<AverageCost> sortWithAlpha(List<RouteTable> routeTables, List<Position> startPoints) {
        Position center = getCenterOfPosition(startPoints);
        Integer nearestNodeId = this.mapGraph.findNearestId(center.getLatitude(), center.getLongitude());
        double alpha = getAlpha(nearestNodeId, routeTables);
        List<AverageCost> candidates = getAverageGap(routeTables);
        normalize(candidates);
        // score 기준 가장 작은 노드 찾기
        return candidates.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());
    }
}
