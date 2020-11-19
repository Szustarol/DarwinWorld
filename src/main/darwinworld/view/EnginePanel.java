package main.darwinworld.view;

import main.darwinworld.Translations;
import main.darwinworld.engine.IEngine;
import main.darwinworld.logic.Genotype;
import main.darwinworld.view.charts.AnimalGrassChart;
import main.darwinworld.view.misc.BirthTracer;
import main.darwinworld.view.misc.StatTracer;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EnginePanel extends JPanel {

    static final int min_ms = 0;
    static final int max_ms = 3000;

    Timer stepTimer;

    int engineIndex;

    JSlider delay;
    JButton startButton;
    JButton stopButton;
    JButton stepButton;
    JButton saveButton;
    private IEngine engine;
    private MapPanel mapPanel;

    JLabel avgLifespan, avgChildren, avgEnergy, dominantGenotype;

    private final AnimalGrassChart animalGrassChart;

    public void forceStop(){
        if(stepTimer != null){
            stepTimer.stop();
            stepTimer = null;
        }
        stopButton.setEnabled(false);
    }

    private void appendEngineData(){
        if(engine != null && mapPanel != null){
            animalGrassChart.add(engine.getAnimalCount(), engine.getOtherElementsCount());
        }
    }

    private void step(){
        BirthTracer.update();
        engine.step();
        if(this.mapPanel != null) {
            mapPanel.repaint();
        }
        appendEngineData();
        Genotype g = engine.mostFrequentGenotype();
        String genoString;
        if(g == null)
            genoString = "";
        else
            genoString = g.toString();
        avgEnergy.setText(Translations.getTranslation("avg_energy") + ": " + engine.getAverageEnergy());
        avgChildren.setText(Translations.getTranslation("avg_children") + ": " + engine.getAverageChildren());
        avgLifespan.setText(Translations.getTranslation("avg_lifespan") + ": " + engine.getAverageLifespan());
        dominantGenotype.setText(Translations.getTranslation("dominant_genotype") + ": \n" + genoString);

        StatTracer.update(engineIndex == 1, engine.getAverageEnergy(),
                engine.getOtherElementsCount(), engine.getAnimalCount(),
                engine.getAverageLifespan(), engine.getAverageChildren());
    }

    private void stepButtonAction(ActionEvent e){
        step();
    }

    private void startButtonAction(ActionEvent e){
        stepTimer = new Timer((delay.getValue()), this::stepButtonAction);
        stepTimer.start();
        stepButton.setEnabled(false);
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
    }

    private  void stopButtonAction(ActionEvent e){
        forceStop();
        stepButton.setEnabled(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void speedChanged(ChangeEvent e){
        if(stepTimer != null && stepTimer.isRunning()){
            this.stepTimer.setDelay(delay.getValue());
        }
    }

    void setEngine(IEngine e, MapPanel p){
        this.engine = e;
        if (p != null) {
            this.mapPanel = p;
            stepButton.setEnabled(true);
            startButton.setEnabled(true);
            if(animalGrassChart != null)
                animalGrassChart.reset();
            appendEngineData();
        }
    }

    EnginePanel(int engineIndex){

        this.engineIndex = engineIndex;

        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);


        TitledBorder b = BorderFactory.createTitledBorder(Translations.getTranslation("engine_setup"));
        setBorder(b);

        avgEnergy = new JLabel(Translations.getTranslation("avg_energy"));
        avgChildren = new JLabel(Translations.getTranslation("avg_children"));
        avgLifespan = new JLabel(Translations.getTranslation("avg_lifespan"));
        dominantGenotype = new JLabel(Translations.getTranslation("dominant_genotype"));

        JLabel[] labels = {avgEnergy, avgChildren, avgLifespan, dominantGenotype};

        for(JLabel l : labels)
            l.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sliderLabel = new JLabel(Translations.getTranslation("time_delay"));

        delay = new JSlider(JSlider.HORIZONTAL, min_ms, max_ms, 400);
        delay.setMajorTickSpacing(1000);
        delay.setMinorTickSpacing(250);
        delay.setPaintTicks(true);
        delay.setPaintLabels(true);

        delay.addChangeListener(this::speedChanged);


        startButton = new JButton(Translations.getTranslation("start_sim"));
        stopButton = new JButton(Translations.getTranslation("pause_sim"));
        stepButton = new JButton(Translations.getTranslation("step_sim"));
        saveButton = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));

        stepButton.addActionListener(this::stepButtonAction);
        startButton.addActionListener(this::startButtonAction);
        stopButton.addActionListener(this::stopButtonAction);
        saveButton.addActionListener(event -> StatTracer.addStatTracer(engineIndex == 1));

        add(sliderLabel);
        add(delay);

        JPanel buttonPanel = new JPanel();
        new BoxLayout(buttonPanel, BoxLayout.X_AXIS);


        JButton[] buttons = new JButton[]{stopButton,startButton,stepButton};
        for(JButton btn : buttons){
            btn.setEnabled(false);
            buttonPanel.add(btn);
        }
        buttonPanel.add(saveButton);

        animalGrassChart = new AnimalGrassChart(Translations.getTranslation("animal_grass_chart"));
        ChartPanel chartPanel = new ChartPanel(animalGrassChart.getChart());
        chartPanel.setPreferredSize(new Dimension(buttonPanel.getSize().width, chartPanel.getPreferredSize().height));
        add(chartPanel);


        add(buttonPanel);

        add(dominantGenotype);
        add(avgEnergy);
        add(avgChildren);
        add(avgLifespan);


    }

}
