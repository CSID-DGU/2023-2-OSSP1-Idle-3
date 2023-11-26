package com.almostThere.middleSpace.test.state.middle;

import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.test.context.middle.MiddleContext;
import com.almostThere.middleSpace.test.state.TestState;
import com.almostThere.middleSpace.test.util.drawer.Scatter;
import com.almostThere.middleSpace.test.util.drawer.StatisticsDrawer;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisualizeState implements TestState {
    private final MiddleContext context;
    private final Scanner scanner;

    @Override
    public void action() {
        System.out.println("------------Menu--------------\n"
                + "1.경로 보기\n"
                + "2.편차 분포 보기\n"
                + "3.총합 분포 보기\n"
                + "4.편차 분포 그래프 보기\n"
                + "5.총합 분포 그래프 보기\n"
                + "6.편차 총합 그래프 보기\n"
                + "7.다시 입력");

        double interval;
        int menu;
        while (true){
            if (scanner.hasNextInt())
            {
                menu = scanner.nextInt();
                break ;
            }
        }
        List<AverageCost> middleSpaces = this.context.getMiddleSpaces();
        switch (menu) {
            case 1:
                showPaths(middleSpaces);
                break;
            case 2:
                Scatter.plotGapData(middleSpaces);
                break;
            case 3:
                Scatter.plotSumData(middleSpaces);
                break;
            case 4:
                while (true){
                    if (scanner.hasNextDouble())
                    {
                        interval = scanner.nextDouble();
                        break ;
                    }
                }
                StatisticsDrawer.drawGapFrequency(middleSpaces, interval);
                break;
            case 5:
                while (true){
                    if (scanner.hasNextDouble())
                    {
                        interval = scanner.nextDouble();
                        break ;
                    }
                }
                StatisticsDrawer.drawSumFrequency(middleSpaces, interval);
                break;
            case 6:
                StatisticsDrawer.drawGapSumRelation(middleSpaces);
                break;
            case 7:
                this.context.setState("InputState");
                break;
        }
    }

    private void showPaths(List<AverageCost> middleSpaces) {
        final int size = middleSpaces.size();
        List<RouteTable> tables = this.context.getTables();

        int base = 0;
        System.out.println("몇 개씩 보여드릴까요? -1울 입력하여 종료!");
        while (base < size) {
            int offset = scanner.nextInt();
            if (offset < 0)
                break;
            for (int i = base ; i < base + offset ; i++) {
                AverageCost cost = middleSpaces.get(i);
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
    }
}
