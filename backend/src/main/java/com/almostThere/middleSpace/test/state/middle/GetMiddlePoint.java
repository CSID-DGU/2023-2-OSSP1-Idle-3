package com.almostThere.middleSpace.test.state.middle;

import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.test.state.TestState;
import com.almostThere.middleSpace.test.context.middle.MiddleContext;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.MapGraph;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * 중간 지점 선정 알고리즘을 실험 하기 위한 State
 */
@RequiredArgsConstructor
public class GetMiddlePoint implements TestState {
    private final MiddleContext context;
    @Override
    public void action() {
        MapGraph mapGraph = this.context.getMapGraph();

        List<RouteTable> tables = context.getInputPoints().stream()
                .map(point -> this.context.getMapGraph().findNearestId(point.getY(), point.getX()))
                .map(id -> context.getRouter().getShortestPath(id))
                .collect(Collectors.toList());

        List<AverageCost> middleSpace = this.context.getService()
                .findMiddleSpaceWithTables(tables);

        middleSpace = this.context.getService().findMiddleSpaceWithBoundary(middleSpace, context.getInputPoints());

        final int size = middleSpace.size();
        int base = 0;
        System.out.println("몇 개씩 보여드릴까요? -1울 입력하여 종료!");
        Scanner scanner = new Scanner(System.in);
        while (base < size) {
            int offset = scanner.nextInt();
            if (offset < 0)
                break;
            for (int i = base ; i < base + offset ; i++) {
                AverageCost cost = middleSpace.get(i);
                MapNode mapNode = cost.getNode();
                System.out.printf("%s (위도 %.6f, 경도 %.6f) 편차의 평균 : %f\n" ,
                    mapNode.getName(),
                    mapNode.getLatitude(),
                    mapNode.getLongitude(),
                    cost.getCost()
                );
                for (RouteTable table : tables) {
                    table.showPath(mapNode.getMap_id());
                    System.out.println();
                }
            }
            base += offset;
        }
        this.context.getInputPoints().clear();
        scanner.close();
        this.context.setState("MiddlePointsInput");
    }
}
