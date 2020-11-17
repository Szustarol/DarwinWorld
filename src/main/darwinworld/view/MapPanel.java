package main.darwinworld.view;

import main.darwinworld.MapObjectImage;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.IMapElement;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    IWorldMap map=null;

    public MapPanel(){
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        if(map == null)
            return;

        Vector2D lowerLeft = map.getBounds()[0];
        Vector2D upperRight = map.getBounds()[1];

        int width = Math.abs(lowerLeft.x-upperRight.x)+1;
        int height = Math.abs(lowerLeft.y-upperRight.y)+1;
        double sqwidth = ((double)Math.min(getWidth(), getHeight()))/Math.max(width, height);
        if(sqwidth < 1) return;
        double xoffset = (getWidth()-sqwidth*width)/2;
        double yoffset = (getHeight()-sqwidth*height)/2;


        for(int x = lowerLeft.x; x <= upperRight.x; x++) {
            for (int y = lowerLeft.y; y <= upperRight.y; y++) {
                g.setColor(map.getTileColor(new Vector2D(x, y)));
                double xready = xoffset + (x - lowerLeft.x) * sqwidth;
                double yready = yoffset + (height - (y - lowerLeft.y)-1) * sqwidth;
                g.fillRect((int) xready, (int) yready, (int)sqwidth, (int)sqwidth);
                g.setColor(Color.black);
                g.drawRect((int) xready, (int) yready, (int)sqwidth, (int)sqwidth);

                if(!map.isOccupied(new Vector2D(x, y)))
                    continue;
                IMapElement me = map.objectsAt(new Vector2D(x, y))[0];
                MapObjectImage be = me.getImageRepresentation();
                Image bs = be.getScaledInstance((int)sqwidth, (int)sqwidth, Image.SCALE_SMOOTH);
                g.drawImage(bs, (int)xready, (int)yready, this);
            }
        }
    }

    public void setMap(IWorldMap map){
        this.map = map;
    }
}
