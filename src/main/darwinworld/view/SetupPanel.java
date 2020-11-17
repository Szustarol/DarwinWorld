package main.darwinworld.view;

import main.darwinworld.Translations;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class SetupPanel extends JPanel {

    LinkedList<JSpinner> spinners;
    LinkedList<Double> spinnerDefaults;

    private void createSpinnerWithLabel(int gridx, int gridy, SpinnerNumberModel model, String labelText){
        JSpinner spinner = new JSpinner(model);
        JLabel label = new JLabel(Translations.getTranslation(labelText));
        label.setLabelFor(spinner);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4, 4, 4);
        c.gridx = gridx;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        add(label, c);
        c.gridx++;
        c.anchor = GridBagConstraints.EAST;
        spinners.add(spinner);
        add(spinner, c);
        if(model.getValue() instanceof Integer)
            spinnerDefaults.add(((Integer)model.getValue()).doubleValue());
        else
            spinnerDefaults.add(((Double)model.getValue()));
    }

    public void resetConfiguration(){
        Iterator<Double> def = spinnerDefaults.iterator();
        Iterator<JSpinner> val = spinners.iterator();

        int n = spinners.size();

        while(def.hasNext()){
            if(--n <= 1)
                val.next().setValue(def.next());
            else
                val.next().setValue(def.next().intValue());
        }
    }

    public class SetupResult{
        public int n_animals;
        public int map_width;
        public int map_height;
        public int jungle_percent;
        public double food_value;
        public double food_decay;
    }

    public SetupResult getSetup(){
        SetupResult r = new SetupResult();
        r.n_animals = (int)spinners.get(0).getValue();
        r.map_width = (int)spinners.get(1).getValue();
        r.map_height = (int)spinners.get(2).getValue();
        r.jungle_percent = (int)spinners.get(3).getValue();
        r.food_value = (double) spinners.get(4).getValue();
        r.food_decay = (double)spinners.get(5).getValue();
        return r;
    }


    public SetupPanel(){
        spinners = new LinkedList<>();
        spinnerDefaults = new LinkedList<>();

        setLayout(new GridBagLayout());

        setBorder(BorderFactory.createTitledBorder(Translations.getTranslation("initial_setup")));

        createSpinnerWithLabel(0, 0,
                new SpinnerNumberModel(4, 2, 20, 1), "n_animals");

        createSpinnerWithLabel(0, 1,
                new SpinnerNumberModel(20, 11, 200, 1), "map_width");

        createSpinnerWithLabel(0, 2,
                new SpinnerNumberModel(20, 11, 200, 1), "map_height");

        createSpinnerWithLabel(0, 3,
                new SpinnerNumberModel(20, 10, 60, 1), "jungle_percent");

        createSpinnerWithLabel(0, 4,
                new SpinnerNumberModel(0.15, 0.01, 0.49, 0.01), "food_value");

        createSpinnerWithLabel(0, 5,
                new SpinnerNumberModel(0.05, 0.01, 0.49, 0.01), "food_decay");
    }
}
