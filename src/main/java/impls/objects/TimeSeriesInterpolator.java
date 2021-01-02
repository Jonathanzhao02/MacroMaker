package main.java.impls.objects;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.LinkedList;
import java.util.List;

public class TimeSeriesInterpolator {
    private PolynomialSplineFunction xLinearInterpolator;
    private PolynomialSplineFunction yLinearInterpolator;

    private PolynomialSplineFunction xSplineInterpolator;
    private PolynomialSplineFunction ySplineInterpolator;

    public TimeSeriesInterpolator(double[] timeseries, double[] x, double[] y) {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        SplineInterpolator splineInterpolator = new SplineInterpolator();

        xLinearInterpolator = linearInterpolator.interpolate(timeseries, x);
        yLinearInterpolator = linearInterpolator.interpolate(timeseries, y);

        xSplineInterpolator = splineInterpolator.interpolate(timeseries, x);
        ySplineInterpolator = splineInterpolator.interpolate(timeseries, y);
    }

    public double[] interpolate(double value, boolean linear) {
        return linear ?
            new double[]{xLinearInterpolator.value(value), yLinearInterpolator.value(value)} :
            new double[]{xSplineInterpolator.value(value), ySplineInterpolator.value(value)};
    }

    public List<Double> getInterpolatedX(long min, long max, double interval, boolean linear) {
        return interpolateOverRange(linear ? xLinearInterpolator : xSplineInterpolator, min, max, interval);
    }

    public List<Double> getInterpolatedY(long min, long max, double interval, boolean linear) {
        return interpolateOverRange(linear ? yLinearInterpolator : ySplineInterpolator, min, max, interval);
    }

    public static List<Double> interpolateOverRange(PolynomialSplineFunction interpolator, double minVal, double maxVal, double interval) {
        double currentVal = minVal;
        List<Double> out = new LinkedList<>();

        while (currentVal < maxVal) {
            out.add(interpolator.value(currentVal));
            currentVal += interval;
        }

        return out;
    }
}
