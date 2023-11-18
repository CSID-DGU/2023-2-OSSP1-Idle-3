package com.almostThere.middleSpace.service.recommendation.impl;

import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.MapGraphService;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.graph.MapGraph;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

/**
 *
 */
@RequiredArgsConstructor
@Service
public class MapGraphServiceImpl implements MapGraphService {
    private final MapGraph mapGraph;
    private final Router router;

    /**
     * @param routeTables : router에서 구한 각 사용자별로 소요되는 최소 시간 테이블 (노드 별로)
     * @return 구해진 소요시간의 편차의 평균으로 정렬한 리스트를 반환
     */
    private List<AverageCost> getAverageGap(List<RouteTable> routeTables) {
        final int numberOfStartPoints = routeTables.size();
        int nodeNum = this.mapGraph.getNodeNum();
        AverageCost[] avgCost = new AverageCost[nodeNum];

        for (int i = 0; i < nodeNum; i++) {
            double sum = 0;

            // 출발 노드들에서 다른 노드를 가는데 걸리는 소요시간의 합을 구한다.
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
                avgCost[i] = new AverageCost(this.mapGraph.getNode(i), avgGap / numberOfStartPoints);
            }
            // 갈 수 없는 경우, 평균 편차를 무한대로 설정
            else {
                avgCost[i] = new AverageCost(this.mapGraph.getNode(i), Double.MAX_VALUE);
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
     * @param startPoints : 시작점(위도, 경도) 리스트
     * @return 구해진 소요시간의 편차의 평균을 정렬한 리스트를 반환
     */
    @Override
    public List<AverageCost> findMiddleSpace(List<Point> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getY(), point.getX()))
                .map(id -> router.getShortestPath(id))
                .collect(Collectors.toList());
        return findMiddleSpaceWithTables(tables);
    }

    /**
     * @param tables : 이미 구해진 최소 소요시간의 라우트 테이블
     * @return 구해진 소요시간의 편차의 평균을 정렬한 리스트를 반환
     */
    @Override
    public List<AverageCost> findMiddleSpaceWithTables(List<RouteTable> tables) {
        return getAverageGap(tables);
    }

    @Override
    public List<AverageCost> findMiddleSpaceWithBoundary(List<AverageCost> averageCosts, List<Point> startPoints) {
        // 사용자의 위도와 경도 중 최대값 및 최소값 초기화
        double maxLatitude = Double.MIN_VALUE;
        double minLatitude = Double.MAX_VALUE;
        double maxLongitude = Double.MIN_VALUE;
        double minLongitude = Double.MAX_VALUE;

        // 시작점의 위치 값에서 최대 및 최소 값을 찾음
        for (Point point : startPoints) {
            double latitude = point.getY();
            double longitude = point.getX();

            // 최대 및 최소 값을 갱신
            maxLatitude = Math.max(maxLatitude, latitude);
            minLatitude = Math.min(minLatitude, latitude);
            maxLongitude = Math.max(maxLongitude, longitude);
            minLongitude = Math.min(minLongitude, longitude);
        }

        // 위도 경도 경계 찾음
        double finalMinLatitude = minLatitude;
        double finalMinLongitude = minLongitude;
        double finalMaxLatitude = maxLatitude;
        double finalMaxLongitude = maxLongitude;


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
}
