package com.almostThere.middleSpace.service.recommendation.impl;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.BaseMiddleSpaceFindService;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.util.GIS;
import com.almostThere.middleSpace.util.NormUtil;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithWeightCenterService extends BaseMiddleSpaceFindService {
    public FindWithWeightCenterService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }
    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        Result result = this.findMiddleSpaceTest(startPoints);
        return result.getResult();
    }

    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
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

    /**
     * 편차와 이동거리 합 모두 정규화
     * @param results
     */
    protected static void normalize(List<AverageCost> results) {
        // 정규화
        List<Double> sumList = results.stream().mapToDouble(AverageCost::getSum).boxed().collect(Collectors.toList());
        List<Double> gapList = results.stream().mapToDouble(AverageCost::getCost).boxed().collect(Collectors.toList());
        double sumMean = NormUtil.calculateMean(sumList);
        double sumStd = NormUtil.calculateStandardDeviation(sumList, sumMean);
        double gapMean = NormUtil.calculateMean(gapList);
        double gapStd = NormUtil.calculateStandardDeviation(gapList, gapMean);
        results.stream().forEach(result-> {
            Double gap = result.getCost(), sum = result.getSum();
            result.setCost((gap - gapMean) / gapStd);
            result.setSum((sum - sumMean) / sumStd);
        });
    }

    /**
     *
     * @param sum 그 지점까지 가는데 걸리는 시간의 평균
     * @param gap 그 지점까지 가는데 걸리는 시간의 편차의 평균
     * @param alpha 편차를 고려하는 정도 [0, 1]
     * @return
     */
    protected Double cost(Double sum, Double gap, double alpha) {
        return alpha * gap + (1 - alpha) * sum;
    }
}
