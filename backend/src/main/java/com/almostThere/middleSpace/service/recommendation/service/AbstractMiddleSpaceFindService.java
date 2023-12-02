package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.web.dto.AnswerPoint;
import com.almostThere.middleSpace.web.dto.MissingPoint;
import com.almostThere.middleSpace.web.dto.TestModuleResponse;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMiddleSpaceFindService {
    protected final MapGraph mapGraph;
    protected final Router router;
    public AbstractMiddleSpaceFindService(MapGraph mapGraph, Router router) {
        this.mapGraph = mapGraph;
        this.router = router;
    }
    abstract public List<AverageCost> findMiddleSpace(List<Position> startPoints);
    abstract public Result findMiddleSpaceTest(List<Position> startPoints);

    /**
     * @param routeTables : router에서 구한 각 사용자별로 소요되는 최소 시간 테이블 (노드 별로)
     * @return 구해진 소요시간의 편차의 평균으로 정렬한 리스트를 반환
     */
    protected List<AverageCost> getAverageGap(List<RouteTable> routeTables) {
        final int numberOfStartPoints = routeTables.size();
        int nodeNum = this.mapGraph.getNodeNum();
        AverageCost[] avgCost = new AverageCost[nodeNum];

        for (int i = 0; i < nodeNum; i++) {
            // 출발 노드들에서 다른 노드를 가는데 걸리는 소요시간의 합을 구한다.
            double sum = 0;
            int startPoint;
            for (startPoint = 0 ; startPoint < numberOfStartPoints; startPoint++) {
                RouteTable routeTable = routeTables.get(startPoint);
                if (routeTable.getCost(i) == Double.MAX_VALUE) {
                    break;
                }
                sum += routeTable.getCost(i);
            }
            // 셋 다 모두 갈 수 있는 경우
            if (startPoint == numberOfStartPoints) {
                double averageTime = sum / numberOfStartPoints;
                double avgGap = 0;
                int innerStartPoint;

                // 편차의 합을 구하는 부분
                for (innerStartPoint = 0; innerStartPoint < numberOfStartPoints; innerStartPoint++) {
                    RouteTable routeTable = routeTables.get(innerStartPoint);
                    avgGap += Math.abs(routeTable.getCost(i) - averageTime);
                }
                // 편차의 평균을 구하고 해당 노드에 소요시간의 편차의 평균값을 할당
                avgCost[i] = new AverageCost(this.mapGraph.getNode(i), avgGap / numberOfStartPoints, averageTime);
            }
            // 갈 수 없는 경우, 평균 편차를 무한대로 설정
            else {
                avgCost[i] = new AverageCost(this.mapGraph.getNode(i));
            }
        }

        // 구해진 평균 편차를 오름차순으로 정렬함.
        // 모두가 갈 수 없어 무한대로 설정된 값들은 모두 버리고, 남은 값들에 대해서 비교하여 정렬함.
        return Arrays.stream(avgCost)
                .filter(averageCost -> averageCost.getCost() != Double.MAX_VALUE)
                .sorted(Comparator.comparingDouble(AverageCost::getCost))
                .collect(Collectors.toList());
    }


    /**
     * 랜덤 샘플 테스트용 메서드
     * @param result
     * @return (정답 정보, 놓친 점의 개수)
     */
    public TestModuleResponse getTestResult(Result result) {
        // 정답 선택
        List<AverageCost> candidates = result.getResult();

        AverageCost answer = candidates.get(0);
        MapNode answerNode = answer.getNode();

        // 반환형 만드는 부분
        AnswerPoint answerPoint = AnswerPoint.builder()
                .position(new Position(answerNode.getLatitude(), answerNode.getLongitude()))
                .gap(answer.getCost())
                .sum(answer.getSum())
                .build();

        List<MissingPoint> missingPoints = candidates.stream().filter(candidate ->
                (candidate.getSum() < answer.getSum()) && (candidate.getCost() < answer.getCost())
        ).map(candidate -> {
            MapNode node = candidate.getNode();
            return MissingPoint.builder()
                    .position(new Position(node.getLatitude(), node.getLongitude()))
                    .sumDifference(answer.getSum() - candidate.getSum())
                    .gapDifference(answer.getCost() - candidate.getCost())
                    .build();
        }).collect(Collectors.toList());

        return TestModuleResponse
                .builder()
                .answer(answerPoint)
                .missingPoints(missingPoints)
                .alpha(result.getAlpha())
                .middleSpace(result.getMiddle())
                .cost(result.getCost())
                .build();
    }
}