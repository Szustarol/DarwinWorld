package main.darwinworld.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.darwinworld.Translations;
import main.darwinworld.view.misc.SetupResult;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

public class SetupPanel extends JPanel {

    private final LinkedList<JSpinner> spinners;
    private final LinkedList<Double> spinnerDefaults;

    private final JCheckBox twoMap;

    private void createSpinnerWithLabel(int gridx, int gridy, SpinnerNumberModel model, String labelText){
        JSpinner spinner = new JSpinner(model);
        JLabel label = new JLabel(Translations.getTranslation(labelText));
        label.setLabelFor(spinner);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1,4, 1, 4);
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.WEST;
        add(label, gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.EAST;
        spinners.add(spinner);
        add(spinner, gbc);
        if(model.getValue() instanceof Integer)
            spinnerDefaults.add(((Integer)model.getValue()).doubleValue());
        else
            spinnerDefaults.add(((Double)model.getValue()));
    }

    public boolean useTwoMaps() {
        return twoMap.isSelected();
    }

    public void resetConfiguration(){
        Iterator<Double> def = spinnerDefaults.iterator();
        Iterator<JSpinner> val = spinners.iterator();

        int n = spinners.size();

        while(def.hasNext()){
            if(--n <= 2)
                val.next().setValue(def.next());
            else
                val.next().setValue(def.next().intValue());
        }
    }


    public SetupResult getSetup(){
        return new SetupResult(
                (int)spinners.get(0).getValue(),
                (int)spinners.get(1).getValue(),
                (int)spinners.get(2).getValue(),
                (int)spinners.get(3).getValue(),
                (double)spinners.get(4).getValue(),
                (double)spinners.get(5).getValue(),
                (double)spinners.get(6).getValue()
        );
    }


    public SetupPanel(){
        spinners = new LinkedList<>();
        spinnerDefaults = new LinkedList<>();

        twoMap = new JCheckBox(Translations.getTranslation("two_map"));

        SetupResult defaultSetup;
        try{
            ObjectMapper om = new ObjectMapper();
            defaultSetup = om.readValue(new File("parameters.json"), SetupResult.class);
        }
        catch(Exception e){
            System.out.println("Error while reading config: " + e.toString());
            defaultSetup = new SetupResult(4, 20, 20, 60, 0.15, 0.05, 0.7);
        }
        setLayout(new GridBagLayout());

        setBorder(BorderFactory.createTitledBorder(Translations.getTranslation("initial_setup")));

        createSpinnerWithLabel(0, 0,
                new SpinnerNumberModel(defaultSetup.n_animals, 2, 20, 1), "n_animals");

        createSpinnerWithLabel(0, 1,
                new SpinnerNumberModel(defaultSetup.map_width, 11, 200, 1), "map_width");

        createSpinnerWithLabel(0, 2,
                new SpinnerNumberModel(defaultSetup.map_height, 11, 200, 1), "map_height");

        createSpinnerWithLabel(0, 3,
                new SpinnerNumberModel(defaultSetup.jungle_percent, 10, 60, 1), "jungle_percent");

        createSpinnerWithLabel(0, 4,
                new SpinnerNumberModel(defaultSetup.food_value, 0.01, 0.49, 0.01), "food_value");

        createSpinnerWithLabel(0, 5,
                new SpinnerNumberModel(defaultSetup.food_decay, 0.001, 0.49, 0.001), "food_decay");

        createSpinnerWithLabel(0, 6,
                new SpinnerNumberModel(defaultSetup.food_starting, 0.05, 1, 0.05), "food_starting");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=7;
        add(twoMap, gbc);
    }
}
