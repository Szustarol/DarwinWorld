package main.darwinworld;

import main.darwinworld.engine.DarwinEngine;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.math.Vector2D;
import main.darwinworld.view.MainWindow;
import sun.applet.Main;

import javax.swing.*;

public class World {
    public static void main(String [] args){
        /*
        IWorldMap map = new WrappedMap(new Vector2D(100, 100), new Vector2D(20, 20), 0.1f);
        DarwinEngine engine = new DarwinEngine(2, 100, map, 0.01f);
        for(int i = 0; i < 100; i++) {
            engine.step();
        }

         */
        Translations.init("pl_PL");
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
