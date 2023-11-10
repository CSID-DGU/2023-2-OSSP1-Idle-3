package com.almostThere.domain.map.Service.test.state.route;

import com.almostThere.domain.map.Service.test.context.route.RouteContext;
import com.almostThere.domain.map.Service.test.state.TestState;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShowPath implements TestState {
    private final RouteContext context;
    @Override
    public void action() {
        System.out.println("Enter a destination number or -1 to go previous: ");
        Scanner scanner = new Scanner(System.in); // 콘솔 입력을 위한 Scanner 생성
        while (true) {
            if (scanner.hasNextLong()) { // long 값이 입력될 때까지 기다림
                long input = scanner.nextLong();
                if (input < 0) {
                    this.context.setState("SourceInput");
                    return ;
                }
                this.context.getRouter().showPath(input);
                break;
            }
        }
    }
}
