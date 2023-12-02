package com.almostThere.middleSpace.service.recommendation.service;

import com.almostThere.middleSpace.domain.gis.Boundary;
import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.util.GIS;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FindWithBoundaryService extends BaseMiddleSpaceFindService {
    public FindWithBoundaryService(MapGraph mapGraph, Router router) {
        super(mapGraph, router);
    }
    @Override
    public List<AverageCost> findMiddleSpace(List<Position> startPoints) {
        Boundary boundary = GIS.toWhat3WordMapBoundary(GIS.findMaxBoundary(startPoints));
        List<RouteTable> tables = startPoints.stream()
                .map(point -> this.mapGraph.findNearestId(point.getLatitude(), point.getLongitude()))
                .map(router::getShortestPath)
                .collect(Collectors.toList());
        List<AverageCost> result = getAverageGap(tables);
        // averageCosts 변수의 Node의 좌표가 boundary에 있는지 확인
        // 위의 조건을 만족하는 AverageCost 객체를 리스트에 담아 반환
        return result.stream()
                .filter(averageCost -> boundary.include(
                        averageCost.getNode().getLatitude(), averageCost.getNode().getLongitude()))
                .sorted(Comparator.comparingDouble(AverageCost::getCost))
                .collect(Collectors.toList());
    }
    @Override
    public Result findMiddleSpaceTest(List<Position> startPoints) {
        List<AverageCost> averageGap = this.findMiddleSpace(startPoints);
        return Result.builder()
                .result(averageGap)
                .cost(0.0)
                .middle(new Position(0.0,0.0))
                .alpha(0.0)
                .build();
    }
}
