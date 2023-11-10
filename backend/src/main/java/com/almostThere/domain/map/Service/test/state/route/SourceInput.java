package com.almostThere.domain.map.Service.test.state.route;

import com.almostThere.domain.map.Service.router.impl.DiRouter;
import com.almostThere.domain.map.Service.test.context.route.RouteContext;
import com.almostThere.domain.map.Service.test.state.TestState;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SourceInput implements TestState {
    private final RouteContext context;
    @Override
    public void action() {
        System.out.println("Enter a source number :");
        Scanner scanner = new Scanner(System.in); // 콘솔 입력을 위한 Scanner 생성

        while (true) {
            if (scanner.hasNextLong()) { // long 값이 입력될 때까지 기다림
                long input = scanner.nextLong();
                Integer searchId = this.context.getMapGraph().findSearchId(input);
                if (input < 0) {
                    System.exit(0);
                    return ;
                }
                DiRouter diRouter = new DiRouter(searchId, this.context.getMapGraph().getNodeNum(), this.context.getMapGraph());
                diRouter.getShortestPath();
                this.context.setRouter(diRouter);
                break;
            }
        }
        this.context.setState("DestInput");
    }
}
