package main.darwinworld.view;

import main.darwinworld.Translations;
import main.darwinworld.engine.DarwinEngine;
import main.darwinworld.engine.IEngine;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.model.Vector2D;
import main.darwinworld.view.misc.BirthTracer;
import main.darwinworld.view.misc.SetupResult;
import main.darwinworld.view.misc.StatTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

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

    public MainWindow(){
        BirthTracer.parent = this;
        StatTracer.parent = this;

        setTitle("Darwin world");
        mainLayout = new BorderLayout();
        setLayout(mainLayout);

        createPanels();

        layoutLeftPanel();

        layoutMainMapPanel();


        secondEnginePanel.setVisible(false);
        secondMapPanel.setVisible(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    private void createPanels(){
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
    }

    private void layoutLeftPanel(){
        JPanel lPanel = new JPanel();
        BoxLayout lPaneLayout = new BoxLayout(lPanel, BoxLayout.Y_AXIS);
        lPanel.setLayout(lPaneLayout);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);

        lPanel.add(setupPanel);

        lPanel.add(buttonPanel);

        lPanel.add(enginePanel);

        lPanel.add(secondEnginePanel);
        add(lPanel, BorderLayout.LINE_START);

    }

    private void layoutMainMapPanel(){
        JPanel middlePanel = new JPanel();
        BoxLayout mapLayout = new BoxLayout(middlePanel, BoxLayout.X_AXIS);
        middlePanel.setLayout(mapLayout);

        middlePanel.add(mapPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(3, 1)));
        middlePanel.add(secondMapPanel);

        add(middlePanel, BorderLayout.CENTER);

    }


    private void resetButtonAction(ActionEvent e){
        this.setupPanel.resetConfiguration();
    }

    private IWorldMap newMapFromConfig(SetupResult s){
        return new WrappedMap(new Vector2D(s.map_width, s.map_height),
                new Vector2D(s.map_width*s.jungle_percent/100, s.map_height*s.jungle_percent/100), (float)s.food_value);
    }

    private IEngine newEngineFromConfig(SetupResult s, IWorldMap map){
        return new DarwinEngine(s.n_animals, 100, map, (float)s.food_decay, (float)s.food_starting, null);
    }

    private void mapPanelInitialize(MapPanel mapPanel, IEngine engine, IWorldMap map){
        mapPanel.setMap(map);
        mapPanel.setEngine(engine);
        mapPanel.repaint();
    }

    private void applyButtonAction(ActionEvent e){
        StatTracer.reset();

        enginePanel.forceStop();
        SetupResult s = setupPanel.getSetup();
        map = newMapFromConfig(s);
        engine = newEngineFromConfig(s, map);
        mapPanelInitialize(mapPanel, engine, map);
        enginePanel.setEngine(engine, mapPanel);

        if(setupPanel.useTwoMaps()){
            secondEnginePanel.forceStop();
            secondMap = newMapFromConfig(s);
            secondEngine = newEngineFromConfig(s, secondMap);
            mapPanelInitialize(secondMapPanel, secondEngine, secondMap);
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


}
