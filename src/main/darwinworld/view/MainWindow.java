package main.darwinworld.view;

import main.darwinworld.Translations;
import main.darwinworld.engine.DarwinEngine;
import main.darwinworld.engine.IEngine;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.math.Vector2D;
import main.darwinworld.view.misc.BirthTracer;
import main.darwinworld.view.misc.SetupResult;
import main.darwinworld.view.misc.StatTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {
    SetupPanel setupPanel;

    EnginePanel enginePanel;
    MapPanel mapPanel;

    EnginePanel secondEnginePanel;
    MapPanel secondMapPanel;

    JButton applyButton;
    JButton resetButton;

    BorderLayout mainLayout;

    IEngine engine = null;
    IEngine secondEngine = null;
    IWorldMap map = null;
    IWorldMap secondMap = null;


    private void resetButtonAction(ActionEvent e){
        this.setupPanel.resetConfiguration();
    }

    private void applyButtonAction(ActionEvent e){
        StatTracer.reset();

        enginePanel.forceStop();
        SetupResult s = setupPanel.getSetup();
        map = new WrappedMap(new Vector2D(s.map_width, s.map_height),
                new Vector2D(s.map_width*s.jungle_percent/100, s.map_height*s.jungle_percent/100), (float)s.food_value);
        engine = new DarwinEngine(s.n_animals, 100, map, (float)s.food_decay, (float)s.food_starting);
        mapPanel.setMap(map);
        mapPanel.repaint();
        enginePanel.setEngine(engine, mapPanel);

        if(setupPanel.useTwoMap()){
            secondEnginePanel.forceStop();
            secondMap = new WrappedMap(new Vector2D(s.map_width, s.map_height),
                    new Vector2D(s.map_width*s.jungle_percent/100, s.map_height*s.jungle_percent/100), (float)s.food_value);
            secondEngine = new DarwinEngine(s.n_animals, 100, secondMap, (float)s.food_decay, (float)s.food_starting);
            secondMapPanel.setMap(secondMap);
            secondMapPanel.repaint();
            secondEnginePanel.setEngine(secondEngine, secondMapPanel);
            secondMapPanel.setVisible(true);
            secondEnginePanel.setVisible(true);
        }
        else {
            secondMapPanel.setVisible(false);
            secondEnginePanel.setVisible(false);
            secondMap = null;
            secondEngine = null;
        }
    }

    public MainWindow(){
        BirthTracer.parent = this;
        StatTracer.parent = this;

        setupPanel = new SetupPanel();
        enginePanel = new EnginePanel(1);
        mapPanel = new MapPanel();

        secondEnginePanel = new EnginePanel(2);
        secondMapPanel = new MapPanel();

        applyButton = new JButton(Translations.getTranslation("apply_config"));
        resetButton = new JButton(Translations.getTranslation("reset_config"));

        resetButton.addActionListener(this::resetButtonAction);
        applyButton.addActionListener(this::applyButtonAction);

        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mapPanel.setSize(new Dimension(400, 400));

        setTitle("Darwin world");
        mainLayout = new BorderLayout();
        setLayout(mainLayout);

        JPanel lPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        BoxLayout lPaneLayout = new BoxLayout(lPanel, BoxLayout.Y_AXIS);
        BoxLayout mapLayout = new BoxLayout(middlePanel, BoxLayout.X_AXIS);
        middlePanel.setLayout(mapLayout);
        lPanel.setLayout(lPaneLayout);

        lPanel.add(setupPanel);

        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);

        lPanel.add(buttonPanel);

        lPanel.add(enginePanel);

        lPanel.add(secondEnginePanel);

        secondEnginePanel.setVisible(false);
        secondMapPanel.setVisible(false);

        middlePanel.add(mapPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(3, 1)));
        middlePanel.add(secondMapPanel);


        add(lPanel, BorderLayout.LINE_START);
        add(middlePanel, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}
