package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.web.dto.MiddleSpaceResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BaseMiddleSpaceFindService extends AbstractMiddleSpaceFindService {
    public BaseMiddleSpaceFindService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }

    @Override
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        return getAverageGap(tables);
    }
    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
        List<AverageCost> averageGap = findMiddleSpace(startPoints);
        return Result.builder()
                .result(averageGap)
                .cost(0.0)
                .middle(new Position(0.0,0.0))
                .alpha(0.0)
                .build();
    }
    /**
     * 경로 검증 용도
     * @param startPoints
     * @param index
     * @return (도착지 까지의 각 출발지까지의 경로와 이동 비용, 편차정보)
     */
    public MiddleSpaceResponse findMostFairMiddleSpaceWithPathIndexed(List<Position> startPoints, Integer index) {
        List<RouteTable> tables = router.getRouteTables(startPoints);
        List<AverageCost> candidate = findMiddleSpaceWithTable(tables);
        AverageCost selectedNode = candidate.get(index);
        MapNode destNode = selectedNode.getNode();

        Integer searchId = this.mapGraph.findSearchId(destNode.getMap_id());
        return MiddleSpaceResponse.builder()
                .endLatitude(destNode.getLatitude())
                .endLongitude(destNode.getLongitude())
                .cost(selectedNode.getCost())
                .paths(tables.stream().map(table -> table.extractPath(searchId))
                        .collect(Collectors.toList()))
                .build();
    }

    protected List<AverageCost> findMiddleSpaceWithTable(List<RouteTable> routeTables) {
        return getAverageGap(routeTables);
    }
}
