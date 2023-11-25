package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.gis.Boundary;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.web.dto.AnswerPoint;
import com.almostThere.middleSpace.web.dto.MiddleSpaceResponse;
import com.almostThere.middleSpace.web.dto.MissingPoint;
import com.almostThere.middleSpace.web.dto.TestModuleResponse;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.almostThere.middleSpace.util.GIS.getWhat3WordsMapBoundaryPoint;

/**
 * MapGraphService의 구현체
 */
@RequiredArgsConstructor
@Service
public class MapGraphService {
    private final MapGraph mapGraph;
    private final Router router;

    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        return findMiddleSpaceWithTables(tables);
    }
    /**
     * 아직까진 실험 용도로 사용.
     * 실제로 서비스에 배포할 때는 위의 메서드를 사용하여 구현할 예정
     * @param tables : (출발노드 인덱스, 그 노드에서 다른 노드까지 걸리는 시간이 기록된 테이블)들
     * @return (도착노드, 소요시간 편차, 총 소요시간)
     */
    public List<AverageCost> findMiddleSpaceWithTables(List<RouteTable> tables) {
        return getAverageGap(tables);
    }

    /**
     * 경로 검증 용도
     * @param startPoints
     * @param index
     * @return (도착지 까지의 각 출발지까지의 경로와 이동 비용, 편차정보)
     */
    public MiddleSpaceResponse findMostFairMiddleSpaceWithPathIndexed(List<Position> startPoints, Integer index) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(id -> router.getShortestPath(id))
                .collect(Collectors.toList());

        List<AverageCost> middleSpace = this.findMiddleSpaceWithTables(tables);
        middleSpace = this.findMiddleSpaceWithBoundary(middleSpace, startPoints);

        AverageCost selectedNode = middleSpace.get(index);
        MapNode destNode = selectedNode.getNode();

        Integer searchId = this.mapGraph.findSearchId(destNode.getMap_id());
        return MiddleSpaceResponse.builder()
                .endLatitude(destNode.getLatitude())
                .endLongitude(destNode.getLongitude())
                .cost(selectedNode.getCost())
                .paths(tables.stream()
                        .map(table -> table.extractPath(searchId))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 랜덤 샘플 테스트용 메서드
     * @param startPoints
     * @return (정답 정보, 놓친 점의 개수)
     */
    public TestModuleResponse getTestResult(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(id -> router.getShortestPath(id))
                .collect(Collectors.toList());

        List<AverageCost> candidates = this.findMiddleSpaceWithTables(tables);
        candidates = this.findMiddleSpaceWithBoundary(candidates, startPoints);

        AverageCost answer = candidates.get(0);
        MapNode answerNode = answer.getNode();

        AnswerPoint answerPoint = AnswerPoint.builder()
                .position(new Position(answerNode.getLatitude(), answerNode.getLongitude()))
                .gap(answer.getCost())
                .sum(answer.getSum())
                .build();

        List<MissingPoint> missingPoints = candidates.stream().filter(candidate ->
                (candidate.getSum() < answer.getSum() && candidate.getCost() <= answer.getCost()) ||
                        (candidate.getSum() <= answer.getSum() && candidate.getCost() < answer.getCost())
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
                .build();
    }

    public List<AverageCost> findMiddleSpaceWithBoundary(List<AverageCost> averageCosts, List<Position> startPoints) {
        // 사용자의 위도와 경도 중 최대값 및 최소값 초기화
        double maxLatitude = Double.MIN_VALUE;
        double minLatitude = Double.MAX_VALUE;
        double maxLongitude = Double.MIN_VALUE;
        double minLongitude = Double.MAX_VALUE;

        // 시작점의 위치 값에서 최대 및 최소 값을 찾음
        for (Position point : startPoints) {
            double latitude = point.getLatitude();
            double longitude = point.getLongitude();

            // 최대 및 최소 값을 갱신
            maxLatitude = Math.max(maxLatitude, latitude);
            minLatitude = Math.min(minLatitude, latitude);
            maxLongitude = Math.max(maxLongitude, longitude);
            minLongitude = Math.min(minLongitude, longitude);
        }



        Boundary finalBoundary = getWhat3WordsMapBoundaryPoint(maxLatitude, minLatitude, maxLongitude, minLongitude);

        // 위도 경도 경계 찾음
        double finalMinLatitude = finalBoundary.minLatitude;
        double finalMinLongitude = finalBoundary.minLongitude;
        double finalMaxLatitude = finalBoundary.maxLatitude;
        double finalMaxLongitude = finalBoundary.maxLongitude;

        // averageCosts 변수의 Node의 latitude와 longitude가 finalMinLatitude, finalMinLongitude, finalMaxLatitude, finalMaxLongitude 안에 있는지 확인
        // 위의 조건을 만족하는 AverageCost 객체를 리스트에 담아 반환
        return averageCosts.stream()
                .filter(averageCost -> {
                    double latitude = averageCost.getNode().getLatitude();
                    double longitude = averageCost.getNode().getLongitude();

                    return latitude >= finalMinLatitude && latitude <= finalMaxLatitude &&
                            longitude >= finalMinLongitude && longitude <= finalMaxLongitude;
                })
                .collect(Collectors.toList());
    }

    /**
     * @param routeTables : router에서 구한 각 사용자별로 소요되는 최소 시간 테이블 (노드 별로)
     * @return 구해진 소요시간의 편차의 평균으로 정렬한 리스트를 반환
     */
    private List<AverageCost> getAverageGap(List<RouteTable> routeTables) {
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

}
