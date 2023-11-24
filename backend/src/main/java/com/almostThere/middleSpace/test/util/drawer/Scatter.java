package com.almostThere.middleSpace.test.util.drawer;

import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.test.util.drawer.renderer.CustomGapRenderer;
import com.almostThere.middleSpace.test.util.drawer.renderer.CustomRenderer;
import com.almostThere.middleSpace.test.util.drawer.renderer.CustomSumRenderer;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class Scatter {
    private static final double CIRCLE_SIZE = 1.0;
    public static void plotNodes(MapGraph mapGraph) {
        XYZWDataset xyzwDataset = loadMapGraphDataset(mapGraph);
        JFreeChart chart = loadChart(xyzwDataset);
        CustomRenderer customBlackRender = new CustomRenderer(xyzwDataset);
        renderDotOnly(customBlackRender, chart);
    }

    public static void plotGapData(List<AverageCost> costs) {
        XYZWDataset xyzwDataset = loadDataset(costs);
        JFreeChart chart = loadChart(xyzwDataset);
        CustomRenderer customRenderer = new CustomGapRenderer(xyzwDataset);
        renderDotOnly(customRenderer, chart);
    }

    public static void plotSumData(List<AverageCost> costs) {
        XYZWDataset xyzwDataset = loadDataset(costs);
        CustomRenderer customRenderer = new CustomSumRenderer(xyzwDataset);
        JFreeChart chart = loadChart(xyzwDataset);
        renderDotOnly(customRenderer, chart);
    }

    private static XYZWDataset loadDataset(List<AverageCost> costs) {
        XYZWDataset xyzwDataset = new XYZWDataset();
        for (AverageCost cost : costs) {
            xyzwDataset.add(
                    cost.getNode().getLongitude(),
                    cost.getNode().getLatitude(),
                    cost.getCost(),
                    cost.getSum()
            );
        }
        xyzwDataset.normalize();
        return xyzwDataset;
    }

    private static XYZWDataset loadMapGraphDataset(MapGraph mapGraph) {
        XYZWDataset xyzwDataset = new XYZWDataset();

        int nodeNum = mapGraph.getNodeNum();
        for (int i = 0 ; i < nodeNum ; i++) {
            MapNode node = mapGraph.getNode(i);
            xyzwDataset.add(node.getLongitude(), node.getLatitude(),0.0,0.0);
        }
        return xyzwDataset;
    }

    private static JFreeChart loadChart(XYZWDataset xyzwDataset) {
        return ChartFactory.createScatterPlot(
                "Gradient Color Scatter Plot",
                "longitude", "latitude",
                xyzwDataset,
                PlotOrientation.VERTICAL,
                true, true, false);
    }

    private static void renderDotOnly(XYLineAndShapeRenderer renderer, JFreeChart chart) {
        // chart에 customer renderer 설정
        XYPlot xyPlot = chart.getXYPlot();
        xyPlot.setRenderer(renderer);
        // 점만 표시하도록 설정
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true); // 0번째 시리즈에 대해 모양(점) 활성화
        // 찍을 모양 설정
        Shape shape = new Ellipse2D.Double(-CIRCLE_SIZE /2 , -CIRCLE_SIZE / 2, CIRCLE_SIZE, CIRCLE_SIZE);
        renderer.setSeriesShape(0, shape);
        // 창 옵션 설정
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
