package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithStartPointIntervalTimeService extends BaseMiddleSpaceFindService {
    public FindWithStartPointIntervalTimeService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }
    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
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
    @Override
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        Result result = this.findMiddleSpaceTest(startPoints);
        return result.getResult();
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