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
        Translations.init("pl_PL");
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
