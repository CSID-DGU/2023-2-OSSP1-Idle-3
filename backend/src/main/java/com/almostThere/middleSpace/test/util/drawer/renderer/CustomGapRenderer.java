package com.almostThere.middleSpace.test.util.drawer.renderer;

import com.almostThere.middleSpace.test.util.drawer.XYZWDataset;
import java.awt.Color;
import java.awt.Paint;

public class CustomGapRenderer extends CustomRenderer {
    public CustomGapRenderer(XYZWDataset dataset) {
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