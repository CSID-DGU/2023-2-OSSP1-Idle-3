package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.util.GIS;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithWeightCenterService extends AbstractMiddleSpaceFindWithCostService {
    public FindWithWeightCenterService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }
    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    @Override
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        Result result = this.findMiddleSpaceTest(startPoints);
        return result.getResult();
    }

    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        List<AverageCost> results = getAverageGap(tables);

        // 무게 중심 구하기
        Position centerOfPosition = getCenterOfPosition(startPoints);
        // 최솟값, 최대값
        double minLength = Double.MAX_VALUE, maxLength = Double.MIN_VALUE;
        for (Position node : startPoints) {
            double distance = GIS.getDistance(centerOfPosition, node);
            if (minLength > distance)
                minLength = distance;
            if (maxLength < distance)
                maxLength = distance;
        }
        // 편차를 고려할 가중치
        double alpha = minLength / (maxLength + minLength);
        // 정규화
        normalize(results);
        // cost 기준 정렬하기
        double minScore = results.stream()
                .mapToDouble(result -> cost(result.getSum(), result.getCost(), alpha))
                .min()
                .orElseThrow(NoSuchElementException::new);
        List<AverageCost> sortedCostList = results.stream()
                .sorted(Comparator.comparingDouble(result -> cost(result.getSum(), result.getCost(), alpha)))
                .collect(Collectors.toList());
        return Result.builder().result(sortedCostList)
                .middle(centerOfPosition)
                .alpha(alpha)
                .cost(minScore)
                .build();
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
