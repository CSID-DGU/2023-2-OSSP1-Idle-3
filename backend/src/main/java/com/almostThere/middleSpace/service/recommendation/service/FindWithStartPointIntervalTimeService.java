package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AggregatedResult;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.web.dto.FinalTestResult;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithStartPointIntervalTimeService extends AbstractMiddleSpaceFindService {
    public FindWithStartPointIntervalTimeService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }

    public FinalTestResult findMiddleSpaceWithRouter(List<RouteTable> tables){
        List<AggregatedResult> averageGap = filterWithMaxIntervalTime(tables);
        AggregatedResult selected = averageGap.get(0);
        MapNode node = selected.getNode();
        return FinalTestResult.builder()
                .gap(selected.getCost())
                .sum(selected.getSum())
                .end(new Position(node.getLatitude(), node.getLongitude()))
                .build();
    }
    @Override
    public Position findMiddleSpace(List<Position> startPoints) {
        List<RouteTable> routeTables = this.router.getRouteTables(startPoints);
        List<AggregatedResult> candidates = filterWithMaxIntervalTime(routeTables);
        // 전부 걸러지면 0.0을 반환한다.
        if (candidates.isEmpty())
            return new Position(0.0 ,0.0);
        MapNode node = candidates.get(0).getNode();
        return new Position(node.getLatitude(), node.getLongitude());
    }

    /**
     * 출발지 사이 간 이동하는데 걸리는 최대 시간보다 오래걸리는 목적지를 걸러내는 함수
     * @param tables 길찾기 결과 리스트
     * @return 걸러낸 결과의 평균 편차와 평균 이동시간 리스트
     */
    private List<AggregatedResult> filterWithMaxIntervalTime(List<RouteTable> tables) {
        double maxIntervalTime = getLongestStartPointIntervalTime(tables);
        List<RouteTable> cloned = tables.stream().map(RouteTable::clone)
                .collect(Collectors.toList());
        for (RouteTable table : cloned) {
            int nodeNum = this.mapGraph.getNodeNum();
            for (int i = 0 ; i < nodeNum ; i++) {
                if (table.getCost(i) > maxIntervalTime)
                    table.getRouteInfo(i).setMinCost(Double.MAX_VALUE);
            }
        }
        List<AggregatedResult> averageGap = getAverageGap(cloned);
        if (averageGap.isEmpty())
            throw new NoSuchElementException();
        return averageGap;
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
}