package com.almostThere.middleSpace.util.drawer;

import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import java.awt.Color;
import java.awt.Paint;
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

public class CustomScatterPlot {
    static class CustomRender extends XYLineAndShapeRenderer {
        protected XYZWDataset dataset;
        public CustomRender(XYZWDataset dataset) {
            this.dataset = dataset;
        }
    }

    static class CustomSumRender extends CustomRender {
        public CustomSumRender(XYZWDataset dataset) {
            super(dataset);
        }
        @Override
        public Paint getItemPaint(int series, int item) {
            double zValue = dataset.getW(series, item).doubleValue();
            // Z와 W 값에 따라 색상 결정
            float blueIntensity = (float)zValue; // 예시: Z 값에 따라 파란색 강도 조절
            return new Color(1.0f - blueIntensity, 0.0f, blueIntensity);
        }
    }

    static class CustomGapRender extends CustomRender {
        public CustomGapRender(XYZWDataset dataset) {
            super(dataset);
        }
        @Override
        public Paint getItemPaint(int series, int item) {
            double zValue = dataset.getZ(series, item).doubleValue();
            // Z와 W 값에 따라 색상 결정
            float blueIntensity = (float)zValue; // 예시: Z 값에 따라 파란색 강도 조절
            return new Color(1.0f - blueIntensity, 0.0f, blueIntensity);
        }
    }

    static class CustomBlackRender extends XYLineAndShapeRenderer {
        private final XYZWDataset dataset;
        public CustomBlackRender(XYZWDataset dataset) {
            this.dataset = dataset;
        }
        @Override
        public Paint getItemPaint(int series, int item) {
            // Z와 W 값에 따라 색상 결정
            return new Color(0.0f, 0.0f, 0.0f);
        }
    }
    public static void plotNodes(MapGraph mapGraph) {
        XYZWDataset xyzwDataset = new XYZWDataset();
        int nodeNum = mapGraph.getNodeNum();
        for (int i = 0 ; i < nodeNum; i++) {
            MapNode node = mapGraph.getNode(i);
            xyzwDataset.add(
                    node.getLongitude(),
                    node.getLatitude(),
                    0.0,
                    0.0
            );
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Gradient Color Scatter Plot",
                "longitude", "latitude",
                xyzwDataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        XYPlot xyPlot = chart.getXYPlot();


        final double size = 1.0;
        Shape shape = new Ellipse2D.Double(-size /2 , -size / 2, size, size);

        CustomBlackRender customBlackRender = new CustomBlackRender(xyzwDataset);
        customBlackRender.setSeriesLinesVisible(0, false);
        customBlackRender.setSeriesShapesVisible(0, true); // 0번째 시리즈에 대해 모양(점) 활성화

        render(customBlackRender, chart, xyPlot, shape);
    }

    public static void plotGapData(List<AverageCost> costs) {
        XYZWDataset xyzwDataset = loadDataset(costs);
        JFreeChart chart = loadChart(xyzwDataset);
        XYPlot xyPlot = chart.getXYPlot();

        final double size = 1.0;
        Shape shape = new Ellipse2D.Double(-size /2 , -size / 2, size, size);
        CustomRender customRender = new CustomGapRender(xyzwDataset);
        customRender.setSeriesLinesVisible(0, false);
        customRender.setSeriesShapesVisible(0, true); // 0번째 시리즈에 대해 모양(점) 활성화
        render(customRender, chart, xyPlot, shape);
    }

    public static void plotSumData(List<AverageCost> costs) {
        XYZWDataset xyzwDataset = loadDataset(costs);
        JFreeChart chart = loadChart(xyzwDataset);
        XYPlot xyPlot = chart.getXYPlot();

        final double size = 1.0;
        Shape shape = new Ellipse2D.Double(-size /2 , -size / 2, size, size);
        CustomRender customRender = new CustomSumRender(xyzwDataset);
        customRender.setSeriesLinesVisible(0, false);
        customRender.setSeriesShapesVisible(0, true); // 0번째 시리즈에 대해 모양(점) 활성화
        render(customRender, chart, xyPlot, shape);
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

    private static JFreeChart loadChart(XYZWDataset xyzwDataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Gradient Color Scatter Plot",
                "longitude", "latitude",
                xyzwDataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        return chart;
    }

    private static void render(XYLineAndShapeRenderer customRender, JFreeChart chart, XYPlot xyPlot, Shape shape) {
        xyPlot.setRenderer(customRender);
        customRender.setSeriesShape(0, shape);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
