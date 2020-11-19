package main.darwinworld.view.misc;

import main.darwinworld.Translations;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.util.LinkedList;


public class StatTracer {
    public double animalsNumerator;
    public double grassNumerator;
    public double avgEnergyNumerator;
    public double avgLifespanNumerator;
    public double avgChildrenNumerator;
    public double count;
    private String savePath;
    private int targetEpochs;

    public static JFrame parent;
    private static final LinkedList<StatTracer> statTracersEngine1 = new LinkedList<>();
    private static final LinkedList<StatTracer> statTracersEngine2 = new LinkedList<>();

    private StatTracer(){
        animalsNumerator = 0;
        grassNumerator = 0;
        avgEnergyNumerator = 0;
        avgLifespanNumerator = 0;
        count = 0;
        avgChildrenNumerator = 0;
    }

    public static void reset(){
        statTracersEngine1.clear();
        statTracersEngine2.clear();
    }


    public static void update(boolean firstEngine, double averageEnergy,
                              int nGrass, int nAnimals, double averageLifespan, double avgChildren){
        LinkedList<StatTracer> st = firstEngine ? statTracersEngine1 : statTracersEngine2;
        st.removeIf(statTracer -> {

           statTracer.count++;
           statTracer.avgChildrenNumerator += avgChildren;
           statTracer.avgLifespanNumerator += averageLifespan;
           statTracer.animalsNumerator += nAnimals;
           statTracer.grassNumerator += nGrass;
           statTracer.avgEnergyNumerator += averageEnergy;

           if(statTracer.count >= statTracer.targetEpochs) {
               String fileContent = "{\n" + "\t\"noEpochs\":" + statTracer.count + ",\n" +
                       "\t\"avgChildren\":" + statTracer.avgChildrenNumerator / statTracer.count + ",\n" +
                       "\t\"avgLifespan\":" + statTracer.avgLifespanNumerator / statTracer.count + ",\n" +
                       "\t\"avgGrass\":" + statTracer.grassNumerator / statTracer.count + ",\n" +
                       "\t\"avgAnimals\":" + statTracer.animalsNumerator / statTracer.count + ",\n" +
                       "\t\"avgEnergy\":" + statTracer.avgEnergyNumerator / statTracer.count + "\n}\n";
               try {
                   FileWriter resultFile = new FileWriter(statTracer.savePath);
                   resultFile.write(fileContent);
                   resultFile.close();
               } catch (Exception e) {
                   System.out.println("Invalid save path: " + statTracer.savePath);
               }
               return true;
           }
           return false;
        });

    }

    public static void addStatTracer(boolean firstEngine){
        JDialog getDialog = new JDialog(parent, Translations.getTranslation("select_epoch_n"));
        getDialog.setLayout(new BoxLayout(getDialog, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(Translations.getTranslation("select_epoch_n_desc2"));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(100, 10, 5000, 1));
        JLabel textLabel = new JLabel(Translations.getTranslation("select_save_path"));
        JFormattedTextField tf = new JFormattedTextField();
        tf.setValue("result.json");
        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> {
            int value = (int)spinner.getValue();
            StatTracer tr = new StatTracer();
            tr.savePath = tf.getValue().toString();
            tr.targetEpochs = value;
            if(firstEngine)
                statTracersEngine1.add(tr);
            else
                statTracersEngine2.add(tr);
            getDialog.dispose();
        });
        getDialog.setLayout(new FlowLayout());
        getDialog.setSize(new Dimension(500, 100));
        getDialog.add(label);
        getDialog.add(spinner);
        getDialog.add(textLabel);
        getDialog.add(tf);
        getDialog.add(okButton);

        getDialog.setVisible(true);
    }

}
