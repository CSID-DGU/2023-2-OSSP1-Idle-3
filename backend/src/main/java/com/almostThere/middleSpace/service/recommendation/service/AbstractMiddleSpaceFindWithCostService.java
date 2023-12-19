package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.recommendation.AggregatedResult;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.util.NormUtil;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMiddleSpaceFindWithCostService extends AbstractMiddleSpaceFindService{
    public AbstractMiddleSpaceFindWithCostService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }

    /**
     * 편차와 이동거리 합 모두 정규화
     * @param results 데이터 리스트
     * @return 정규화된 데이터 리스트
     */
    protected List<AggregatedResult> normalize(List<AggregatedResult> results) {
        // 정규화
        List<Double> sumList = results.stream().mapToDouble(AggregatedResult::getSum).boxed().collect(Collectors.toList());
        List<Double> gapList = results.stream().mapToDouble(AggregatedResult::getCost).boxed().collect(Collectors.toList());
        double sumMean = NormUtil.calculateMean(sumList);
        double sumStd = NormUtil.calculateStandardDeviation(sumList, sumMean);
        double gapMean = NormUtil.calculateMean(gapList);
        double gapStd = NormUtil.calculateStandardDeviation(gapList, gapMean);
        return results.stream().map(result-> {
            Double gap = result.getCost(), sum = result.getSum();
            return new AggregatedResult(result.getNode(), (gap - gapMean) / gapStd, (sum - sumMean) / sumStd);
        }).collect(Collectors.toList());
    }

    /**
     *
     * @param sum 그 지점까지 가는데 걸리는 시간의 평균
     * @param gap 그 지점까지 가는데 걸리는 시간의 편차의 평균
     * @param alpha 편차를 고려하는 정도 [0, 1]
     * @return alpha 값으로 구한 cost (alpha * gap + (1 - alpha) * sum)
     */
    protected Double cost(Double sum, Double gap, double alpha) {
        return alpha * gap + (1 - alpha) * sum;
    }
}
