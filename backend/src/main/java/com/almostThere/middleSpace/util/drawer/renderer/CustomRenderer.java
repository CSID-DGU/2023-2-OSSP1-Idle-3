package com.almostThere.middleSpace.util.drawer.renderer;

import com.almostThere.middleSpace.util.drawer.XYZWDataset;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class CustomRenderer extends XYLineAndShapeRenderer {
    protected XYZWDataset dataset;
    public CustomRenderer(XYZWDataset dataset) {
        this.dataset = dataset;
    }
}