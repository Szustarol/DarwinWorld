package main.darwinworld.view.misc;

import jdk.nashorn.internal.scripts.JD;
import main.darwinworld.Translations;
import main.darwinworld.objects.Animal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class BirthTracer {
    public Animal animal;
    public int nEpochs;
    public int epochsRemaining;
    public int childrenWhenTracingStarted;
    public int descendantsWhenTracingStarted;

    private static final LinkedList<BirthTracer> birthTracers = new LinkedList<>();

    public static JFrame parent;

    private BirthTracer(){

    }

    public static void addBirthTracer(Animal a, int nEpochs) {
        BirthTracer t = new BirthTracer();
        t.animal = a;
        t.nEpochs = nEpochs;
        t.epochsRemaining = nEpochs;
        t.childrenWhenTracingStarted = a.nChildren;
        t.descendantsWhenTracingStarted = a.nDescendantsAfter;
        birthTracers.add(t);
    }

    public static void update(){ //called whenever an epoch passe
        birthTracers.forEach(birthTracer -> {
            birthTracer.epochsRemaining--;
            if(birthTracer.epochsRemaining == 0){
                birthTracer.animal.tracingBirths = false;
                JDialog traceDialog = new JDialog(parent, Translations.getTranslation("tracing_result"));
                JPanel dialogPanel = new JPanel();
                traceDialog.add(dialogPanel);
                BoxLayout bl = new BoxLayout(dialogPanel, BoxLayout.Y_AXIS);
                dialogPanel.setLayout(bl);
                int ch = birthTracer.animal.nChildren-birthTracer.childrenWhenTracingStarted;
                int dc = birthTracer.animal.nDescendantsAfter-birthTracer.descendantsWhenTracingStarted;
                dialogPanel.add(new JLabel(Translations.getTranslation("tracing_result")));
                dialogPanel.add(new JLabel(Translations.getTranslation("n_epochs") + ": " + birthTracer.nEpochs));
                dialogPanel.add(new JLabel(Translations.getTranslation("n_children") + ": " + ch));
                dialogPanel.add(new JLabel(Translations.getTranslation("n_descendants") + ": " + dc));
                if(birthTracer.animal.getEnergy() == 0){
                    dialogPanel.add(new JLabel(
                       Translations.getTranslation("death_epoch") + ": " + birthTracer.animal.deathEpoch
                    ));
                }
                traceDialog.setSize(new Dimension(300, 140));
                traceDialog.setVisible(true);
            }
        });
        birthTracers.removeIf(birthTracer -> birthTracer.epochsRemaining == 0);
    }

    public static void getFromUser(Animal a) {
        for(BirthTracer bt : birthTracers){
            if(bt.animal == a){
                JDialog jd = new JDialog(parent, Translations.getTranslation("animal_already_traced"));
                jd.setSize(new Dimension(250, 70));
                jd.add(new JLabel(Translations.getTranslation("animal_already_traced")));
                jd.setVisible(true);
                return;
            }
        }

        JDialog getDialog = new JDialog(parent, Translations.getTranslation("select_epoch_n"));
        JLabel label = new JLabel(Translations.getTranslation("select_epoch_n_desc"));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(100, 10, 1300, 1));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> {
            int value = (int)spinner.getValue();
            a.tracingBirths = true;
            addBirthTracer(a, value);
            getDialog.dispose();
        });
        getDialog.setLayout(new FlowLayout());
        getDialog.setSize(new Dimension(500, 100));
        getDialog.add(label);
        getDialog.add(spinner);
        getDialog.add(okButton);

        getDialog.setVisible(true);
    }
}
