package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.gis.Boundary;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.util.GIS;
import com.almostThere.middleSpace.util.NormUtil;
import com.almostThere.middleSpace.web.dto.AnswerPoint;
import com.almostThere.middleSpace.web.dto.MiddleSpaceResponse;
import com.almostThere.middleSpace.web.dto.MissingPoint;
import com.almostThere.middleSpace.web.dto.TestModuleResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
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
     * 표준편차만 고려하여 선택한 테스트 결과를 반환함.
     */
    public Result findMiddleSpaceStdOnly(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        List<AverageCost> averageGap = getAverageGap(tables);
        return Result.builder()
                .result(averageGap)
                .cost(0.0)
                .middle(null)
                .alpha(0.0)
                .build();
    }

    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    public Result findMiddleSpaceWithCenter(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        return findMiddleSpaceWithTablesAndCenter(tables);
    }


    /**
     * @param tables : (출발노드 인덱스, 그 노드에서 다른 노드까지 걸리는 시간이 기록된 테이블)들
     * @return (도착노드, 소요시간 편차, 총 소요시간)
     */
    public Result findMiddleSpaceWithTablesAndCenter(List<RouteTable> tables) {
        List<AverageCost> results = getAverageGap(tables);
        List<MapNode> startPoints = tables.stream()
                .map(RouteTable::getStartNode)
                .collect(Collectors.toList());

        // 무게 중심 구하기
        Position centerOfPosition = getCenterOfPosition(startPoints);
        // 최솟값, 최대값
        double minLength = Double.MAX_VALUE, maxLength = Double.MIN_VALUE;
        for (MapNode node : startPoints) {
            double distance = GIS.getDistance(centerOfPosition, node);
            if (minLength > distance)
                minLength = distance;
            if (maxLength < distance)
                maxLength = distance;
        }
        // 편차를 고려할 가중치
        double alpha = minLength / (maxLength + minLength);

        System.out.println(alpha);
        normalize(results);
        // 계산한 점수를 기준으로 정렬하여 반환
        List<AverageCost> costList = results
                .stream()
                .sorted(Comparator.comparingDouble(
                        result -> cost(result.getSum(), result.getCost(), alpha))
                )
                .collect(Collectors.toList());

        double minScore = results.stream()
                .mapToDouble(result -> cost(result.getSum(), result.getCost(), alpha))
                .min()
                .orElseThrow(NoSuchElementException::new);
        return Result.builder()
                .middle(centerOfPosition)
                .result(costList)
                .alpha(alpha)
                .cost(minScore)
                .build();
    }

    public Result findMiddleSpaceWithLongestStartPointIntervalTime(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        double maxIntervalTime = getLongestStartPointIntervalTime(tables);
        for (RouteTable table : tables) {
            int nodeNum = this.mapGraph.getNodeNum();
            for (int i = 0 ; i < nodeNum ; i++) {
                if (table.getCost(i) > maxIntervalTime)
                    table.getRouteInfo(i).setMinCost(Double.MAX_VALUE);
            }
        }
        List<AverageCost> averageGap = getAverageGap(tables);
        return Result.builder()
                .middle(null)
                .result(averageGap)
                .alpha(null)
                .cost(null)
                .build();
    }

    /**
     * 출발지들 사이의 이동시간 중 cost가 가장 큰 cost 구하기
     * @param tables 출발 좌표들
     * @return 가장 큰 cost 값
     */
    public double getLongestStartPointIntervalTime(List<RouteTable> tables){
          List<MapNode> startPoints = tables.stream()
                .map(RouteTable::getStartNode)
                .collect(Collectors.toList());

        int size = startPoints.size();

        double maxCost = 0.0;

        for (int i = 0; i < size; i++) {
            MapNode startNode = startPoints.get(i);
            for (int j = 0; j < size; j++) {
                if (i == j) continue;
                MapNode endNode = startPoints.get(j);
                double cost = getIntervalTimeBetweenNodes(startNode.getMap_id(), endNode.getMap_id(), tables);
                if (maxCost < cost) {
                    maxCost = cost;
                }
            }
        }

        return maxCost;
    }

    private double getIntervalTimeBetweenNodes(long startNodeIndex, long endNodeIndex, List<RouteTable> tables) {
        int searchId = this.mapGraph.findSearchId(endNodeIndex);
        for (RouteTable table : tables) {
            if (table.getStartNode().getMap_id() == startNodeIndex) {
                return table.getCost(searchId);
            }
        }
        // 못가는 경우는 고려하지 않는다.
        return 0.0;
    }


    /**
     * 출발지들 사이의 이동시간 중 cost가 가장 큰 cost 구하기
     * @param startPoints 출발 좌표들
     * @return 가장 큰 cost 값
     */
    public double getLongestStartPointIntervalTime(List<RouteTable> tables){
          List<MapNode> startPoints = tables.stream()
                .map(RouteTable::getStartNode)
                .collect(Collectors.toList());

        int size = startPoints.size();

        double maxCost = 0.0;

        for (int i = 0; i < size; i++) {
            MapNode startNode = startPoints.get(i);
            for (int j = 0; j < size; j++) {
                if (i == j) continue;
                MapNode endNode = startPoints.get(j);
                double cost = getIntervalTimeBetweenNodes(startNode.getMap_id(), endNode.getMap_id(), tables);
                if (maxCost < cost) {
                    maxCost = cost;
                }
            }
        }

        return maxCost;
    }

    private double getIntervalTimeBetweenNodes(long startNodeIndex, long endNodeIndex, List<RouteTable> tables) {
        int searchId = this.mapGraph.findSearchId(endNodeIndex);
        for (RouteTable table : tables) {
            if (table.getStartNode().getMap_id() == startNodeIndex) {
                return table.getCost(searchId);
            }
        }
        return 0.0; // 해당 경로를 찾지 못할 경우
    }


    /**
     * 시간 무게를 적용한 무게 중심
     * @param startPoints 출발 좌표들
     * @return
     */
    public Result findMiddleSpaceWithWeightedPosition(List<Position> startPoints) {
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
     * 3*3을 적용한 결과
     * @param startPoints
     * @return
     */
    public List<AverageCost> findMiddleSpaceWithBoundary(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        List<AverageCost> results = getAverageGap(tables);
        List<AverageCost> middleSpaceWithBoundary = findMiddleSpaceWithBoundary(results, startPoints);
        return middleSpaceWithBoundary;
    }

    private static void normalize(List<AverageCost> results) {
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
     * 시작점 좌표 기준으로 무게 중심 좌표를 구하는 함수
     * @param startPoints
     * @return
     */
    private Position getCenterOfPosition(List<MapNode> startPoints) {
        double latitude = 0.0f;
        double longitude = 0.0f;
        int size = startPoints.size();

        for (MapNode mapNode : startPoints) {
            latitude += mapNode.getLatitude();
            longitude += mapNode.getLongitude();
        }
        return new Position(latitude / size, longitude / size);
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

    /**
     *
     * @param sum 그 지점까지 가는데 걸리는 시간의 평균
     * @param gap 그 지점까지 가는데 걸리는 시간의 편차의 평균
     * @param alpha 편차를 고려하는 정도 [0, 1]
     * @return
     */
    private Double cost(Double sum, Double gap, double alpha) {
        return alpha * gap + (1 - alpha) * sum;
//        return sum;
//        return gap;
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

        Result middleSpace = this.findMiddleSpaceWithTablesAndCenter(tables);
        List<AverageCost> candidate = this.findMiddleSpaceWithBoundary(middleSpace.getResult(),
                startPoints);

        AverageCost selectedNode = candidate.get(index);
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
                .sorted(Comparator.comparingDouble(AverageCost::getCost))
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
