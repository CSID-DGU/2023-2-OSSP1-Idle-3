package com.almostThere.middleSpace.test.state.route;

import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.test.context.route.RouteContext;
import com.almostThere.middleSpace.test.state.TestState;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

/**
 * 목적지를 입력받고
 * 길 찾기 경로를 출력해주는 state
 */
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
                RouteTable table = this.context.getTable();
                table.showPath(input);
                break;
            }
        }
    }
}
