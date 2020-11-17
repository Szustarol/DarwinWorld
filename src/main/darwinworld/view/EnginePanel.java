package main.darwinworld.view;

import main.darwinworld.Translations;
import main.darwinworld.engine.IEngine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EnginePanel extends JPanel {

    static final int min_ms = 0;
    static final int max_ms = 3000;

    Timer stepTimer;

    JSlider delay;
    JButton startButton;
    JButton pauseButton;
    JButton stepButton;
    private IEngine engine;
    private MapPanel mapPanel;


    private void step(){
        engine.step();
        if(this.mapPanel != null)
            mapPanel.repaint();
    }

    private void stepButtonAction(ActionEvent e){
        step();
    }

    private void startButtonAction(ActionEvent e){
        stepTimer = new Timer(((int) delay.getValue()), this::stepButtonAction);
        stepTimer.start();
    }

    private  void stopButtonAction(ActionEvent e){
        if(stepTimer != null)
            stepTimer.stop();
    }

    void setEngine(IEngine e, MapPanel p){
        this.engine = e;
        if (p != null)
            this.mapPanel = p;
    }

    EnginePanel(){

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        TitledBorder b = BorderFactory.createTitledBorder(Translations.getTranslation("engine_setup"));
        setBorder(b);

        JLabel sliderLabel = new JLabel(Translations.getTranslation("time_delay"));

        delay = new JSlider(JSlider.HORIZONTAL, min_ms, max_ms, 400);
        delay.setMajorTickSpacing(1000);
        delay.setMinorTickSpacing(250);
        delay.setPaintTicks(true);
        delay.setPaintLabels(true);


        startButton = new JButton(Translations.getTranslation("start_sim"));
        pauseButton = new JButton(Translations.getTranslation("pause_sim"));
        stepButton = new JButton(Translations.getTranslation("step_sim"));

        stepButton.addActionListener(this::stepButtonAction);
        startButton.addActionListener(this::startButtonAction);
        pauseButton.addActionListener(this::stopButtonAction);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0f;
        gbc.weighty = 1.0f;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(sliderLabel, gbc);
        gbc.gridy++;
        add(delay, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(startButton, gbc);
        gbc.gridy++;
        add(pauseButton, gbc);
        gbc.gridy++;
        add(stepButton, gbc);
        gbc.gridy++;

    }

}
