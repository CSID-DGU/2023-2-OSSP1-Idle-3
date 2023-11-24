package com.almostThere.middleSpace.test.util.drawer;

import static java.util.stream.Collectors.*;

import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.test.util.counter.Counter;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticsDrawer {
    public static void drawTotal(List<AverageCost> results) {
        List<Double> gapList = results.stream()
                .mapToDouble(AverageCost::getCost)
                .boxed().collect(toList());
        List<Double> sumList = results.stream()
                .mapToDouble(AverageCost::getSum)
                .boxed().collect(toList());

        ChartPanel gapPanel = getFrequencyLineChart(gapList, 10.0);
        ChartPanel sumPanel = getFrequencyLineChart(sumList, 10.0);

        JFrame frame = new JFrame("분포");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(gapPanel, BorderLayout.WEST);
        frame.add(sumPanel, BorderLayout.EAST);

        // 창 크기 설정 및 표시
        frame.setSize(1200, 800);
        frame.setVisible(true);
    }

    public static void drawGapFrequency(List<AverageCost> results, double interval) {
        JFrame jFrame = defaultFrame();
        ChartPanel gapPanel = getFrequencyLineChart(
                results.stream()
                .mapToDouble(AverageCost::getCost)
                .boxed().collect(toList()), interval);
        jFrame.add(gapPanel);
        jFrame.setVisible(true);
    }

    public static void drawSumFrequency(List<AverageCost> results, double interval) {
        JFrame jFrame = defaultFrame();
        ChartPanel gapPanel = getFrequencyLineChart(
                results.stream()
                        .mapToDouble(AverageCost::getSum)
                        .boxed().collect(toList()), interval);
        jFrame.add(gapPanel);
        jFrame.setVisible(true);
    }

    // 기본 창 세팅하기
    private static JFrame defaultFrame() {
        JFrame frame = new JFrame("분포");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setLayout(new BorderLayout());

        // 창 크기 설정 및 표시
        frame.setSize(1200, 800);
        return frame;
    }

    private static ChartPanel getFrequencyLineChart(List<Double> values, double interval) {
        return new ChartPanel(
                ChartFactory.createLineChart(
                    "Frequency Distribution",
                    "Value",
                    "Frequency",
                    loadDataset(Counter.frequencyCount(values, interval))
                )
        );
    }
    private static DefaultCategoryDataset loadDataset(Map<Double, Integer> frequency) {
        List<Map.Entry<Double, Integer>> sortedEntries = frequency.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(toList());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Double, Integer> entry : sortedEntries) {
            dataset.addValue(entry.getValue(), "Frequency", entry.getKey());
        }
        return dataset;
    }
}
