package main.darwinworld;

import main.darwinworld.view.MainWindow;

public class World {
    public static void main(String [] args){
        Translations.init("en_EN");
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}
