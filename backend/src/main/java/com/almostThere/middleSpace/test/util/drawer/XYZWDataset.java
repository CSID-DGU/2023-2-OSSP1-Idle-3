package com.almostThere.middleSpace.test.util.drawer;

import com.almostThere.middleSpace.util.NormUtil;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.xy.AbstractXYZDataset;

public class XYZWDataset extends AbstractXYZDataset {
    private final List<Double> longitudes;
    private final List<Double> latitudes;
    private List<Double> averageDeviations;
    private List<Double> sums;

    private Double targetLongitude;
    private Double targetLatitude;

    public XYZWDataset() {
        longitudes = new ArrayList<>();
        latitudes = new ArrayList<>();
        averageDeviations = new ArrayList<>();
        sums = new ArrayList<>();
    }

    public void add(Double longitude, Double latitude, Double averageDeviation, Double sum) {
        this.longitudes.add(longitude);
        this.latitudes.add(latitude);
        this.averageDeviations.add(averageDeviation);
        this.sums.add(sum);
    }
    public void setTargetPosition(Double targetLongitude, Double targetLatitude) {
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
    }
    public Double getTargetLongitude() {return this.targetLongitude;}
    public Double getTargetLatitude() {return this.targetLatitude;}

    public void normalize() {
        this.averageDeviations = NormUtil.normalize_with_max(this.averageDeviations);
        this.sums = NormUtil.normalize_with_max(this.sums);
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return "Series1";
    }

    @Override
    public Number getZ(int series, int item) {
        return this.averageDeviations.get(item);
    }

    @Override
    public int getItemCount(int series) {
        return this.longitudes.size();
    }

    @Override
    public Number getX(int series, int item) {
        return this.longitudes.get(item);
    }

    @Override
    public Number getY(int series, int item) {
        return this.latitudes.get(item);
    }

    public Number getW(int series, int item) {
        return this.sums.get(item);
    }
}
