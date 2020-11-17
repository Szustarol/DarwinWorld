package main.darwinworld;

import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapObjectImage extends BufferedImage {

    public MapObjectImage(int width, int height, int typeIntArgb) {
        super(width, height, typeIntArgb);
    }

    public static MapObjectImage load(String path){
        Image pi = Toolkit.getDefaultToolkit().getImage(path);

        ToolkitImage ti = (ToolkitImage)pi;

        Image temporary = new ImageIcon(ti).getImage();

        MapObjectImage mi = new MapObjectImage(ti.getWidth(), ti.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mi.createGraphics();
        g2d.drawImage(temporary, 0, 0, null);
        g2d.dispose();
        return mi;
    }

    public MapObjectImage rotated(double angle) {

        int w = this.getWidth();
        int h = this.getHeight();

        MapObjectImage rotated = new MapObjectImage(w, h, this.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(this, null, 0, 0);
        graphic.dispose();
        return rotated;
    }
}
