package main.darwinworld.view.charts;

import main.darwinworld.Translations;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.resources.JFreeChartResources;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class AnimalGrassChart {
    JFreeChart lineChart;
    XYSeries animalCount;
    XYSeries grassCount;
    XYSeriesCollection dataset;
    int epoch_ctr = 0;

    public AnimalGrassChart(String title){
        animalCount = new XYSeries(Translations.getTranslation("animal_count"));
        grassCount = new XYSeries(Translations.getTranslation("grass_count"));
        dataset = new XYSeriesCollection();
        dataset.addSeries(animalCount);
        dataset.addSeries(grassCount);
        lineChart = ChartFactory.createXYLineChart(title,
                Translations.getTranslation("epoch"),
                Translations.getTranslation("number"),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
    }

    public void add(int animalCount, int grassCount){
        this.grassCount.add(epoch_ctr, grassCount);
        this.animalCount.add(epoch_ctr, animalCount);
        epoch_ctr++;
        if(this.animalCount.getItemCount() > 100)
            this.animalCount.remove(0);
        if(this.grassCount.getItemCount() > 100)
            this.grassCount.remove(0);
    }

    public void reset(){
        epoch_ctr = 1;
        animalCount.clear();
        grassCount.clear();
    }

    public JFreeChart getChart(){
        return lineChart;
    }
}
