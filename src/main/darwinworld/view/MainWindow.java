package main.darwinworld.view;

import main.darwinworld.Translations;
import main.darwinworld.engine.DarwinEngine;
import main.darwinworld.engine.IEngine;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {
    SetupPanel setupPanel;
    EnginePanel enginePanel;
    MapPanel mapPanel;

    JButton applyButton;
    JButton resetButton;

    BorderLayout mainLayout;

    IEngine engine = null;
    IWorldMap map = null;

    private void resetButtonAction(ActionEvent e){
        this.setupPanel.resetConfiguration();
    }

    private void applyButtonAction(ActionEvent e){
        SetupPanel.SetupResult s = setupPanel.getSetup();
        map = new WrappedMap(new Vector2D(s.map_width, s.map_height),
                new Vector2D(s.map_width*s.jungle_percent/100, s.map_height*s.jungle_percent/100), (float)s.food_value);
        engine = new DarwinEngine(s.n_animals, 100, map, (float)s.food_decay, 0.9f);
        mapPanel.setMap(map);
        mapPanel.repaint();
        enginePanel.setEngine(engine, mapPanel);
    }

    public MainWindow(){
        setupPanel = new SetupPanel();
        enginePanel = new EnginePanel();
        mapPanel = new MapPanel();

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
        BoxLayout lPaneLayout = new BoxLayout(lPanel, BoxLayout.Y_AXIS);
        lPanel.setLayout(lPaneLayout);

        lPanel.add(setupPanel);

        lPanel.add(applyButton);

        lPanel.add(resetButton);

        lPanel.add(enginePanel);

        add(lPanel, BorderLayout.LINE_START);
        add(mapPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
}
