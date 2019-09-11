package com.namutomatvey.financialaccount.activity;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.List;

class PieGraph {

    GraphicalView getGraphicalView(Context context, List<String> categoryNames, List<Double> categoryAmounts) {
        CategorySeries series = new CategorySeries("");
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setShowLegend(false);
        defaultRenderer.setLabelsTextSize(32);
        defaultRenderer.setLabelsColor(Color.BLACK);
        SimpleSeriesRenderer simpleSeriesRenderer = null;
        int[] colors = {Color.BLUE, Color.GRAY, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        for (int i = 0; i < categoryNames.size(); i++) {
            series.add(categoryNames.get(i), categoryAmounts.get(i));
            simpleSeriesRenderer = new SimpleSeriesRenderer();
            simpleSeriesRenderer.setColor(colors[i]);
            defaultRenderer.addSeriesRenderer(simpleSeriesRenderer);
        }
        return ChartFactory.getPieChartView(context, series, defaultRenderer);
    }
}