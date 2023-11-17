package com.almostThere.middleSpace.test.state.route;

import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.service.routing.impl.DiRouter;
import com.almostThere.middleSpace.test.context.route.RouteContext;
import com.almostThere.middleSpace.test.state.TestState;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SourceInput implements TestState {
    private final RouteContext context;
    private final Router router;
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
                this.context.setTable(router.getShortestPath(searchId));
                break;
            }
        }
        this.context.setState("DestInput");
    }
}
