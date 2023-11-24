package com.almostThere.middleSpace.test.util.drawer.renderer;

import com.almostThere.middleSpace.test.util.drawer.XYZWDataset;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class CustomRenderer extends XYLineAndShapeRenderer {
    protected XYZWDataset dataset;
    public CustomRenderer(XYZWDataset dataset) {
        this.dataset = dataset;
    }
}