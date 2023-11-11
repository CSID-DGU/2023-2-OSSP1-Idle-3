package com.almostThere.domain.map.Service.test.state.middle;

import com.almostThere.domain.map.Service.mapGraphService.AverageCost;
import com.almostThere.domain.map.Service.router.Router;
import com.almostThere.domain.map.Service.routerFactory.RouterFactory;
import com.almostThere.domain.map.Service.routerFactory.impl.DiRouterFactory;
import com.almostThere.domain.map.Service.test.state.TestState;
import com.almostThere.domain.map.Service.test.context.middle.MiddleContext;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMiddlePoint implements TestState {
    private final MiddleContext context;
    @Override
    public void action() {
        MapGraph mapGraph = this.context.getMapGraph();

        RouterFactory routerFactory = new DiRouterFactory(mapGraph);
        List<Router> routers = routerFactory.createRouters(this.context.getInputPoints());
        List<AverageCost> middleSpace = this.context.getService()
                .findMiddleSpaceWithRouter(routers);

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
                MapNode mapNode = mapGraph.findMapNode(cost.getIndex());
                System.out.printf("%s (위도 %.6f, 경도 %.6f) 편차의 평균 : %f\n" ,
                    mapNode.getName(),
                    mapNode.getLatitude(),
                    mapNode.getLongitude(),
                    cost.getCost()
                );
                for (Router router : routers) {
                    router.showPath(mapGraph.findMapId(cost.getIndex()));
                }
            }
            base += offset;
        }
        this.context.getInputPoints().clear();
        this.context.setState("MiddlePointsInput");
    }
}
