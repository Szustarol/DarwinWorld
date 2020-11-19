package main.darwinworld.view;

import main.darwinworld.MapObjectImage;
import main.darwinworld.Translations;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;
import main.darwinworld.objects.IMapElement;
import main.darwinworld.view.misc.BirthTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class MapPanel extends JPanel {
    IWorldMap map=null;

    Vector2D clickPosition = null;
    Animal clickedAnimal = null;

    public MapPanel(){
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                clickPosition = new Vector2D(me.getPoint().x, me.getPoint().y);
                repaint();
            }
        });
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
        int roundWidth = (int)sqwidth;
        if(sqwidth < 1) return;
        double xoffset = (getWidth()-sqwidth*width)/2;
        double yoffset = (getHeight()-sqwidth*height)/2;

        for(int x = lowerLeft.x; x <= upperRight.x; x++) {
            for (int y = lowerLeft.y; y <= upperRight.y; y++) {
                g.setColor(map.getTileColor(new Vector2D(x, y)));
                double xready = xoffset + (x - lowerLeft.x) * sqwidth;
                double yready = yoffset + (height - (y - lowerLeft.y)-1) * sqwidth;
                int x_cast = (int)xready;
                int y_cast = (int)yready;
                g.fillRect(x_cast, y_cast, roundWidth, roundWidth);
                g.setColor(Color.black);
                g.drawRect(x_cast, y_cast, roundWidth, roundWidth);


                if(!map.isOccupied(new Vector2D(x, y)))
                    continue;



                IMapElement me = map.objectsAt(new Vector2D(x, y))[0];

                //check if click point is inside draw area
                boolean clicked = false;
                if(clickPosition != null){
                    Vector2D start = new Vector2D(x_cast, y_cast);
                    Vector2D end = new Vector2D((int)(x_cast + sqwidth), (int)(y_cast + sqwidth));
                    if(clickPosition.lowerLeft(start).equals(start) && clickPosition.upperRight(end).equals(end)){
                        clicked = true;
                        clickPosition = null;
                    }
                }

                MapObjectImage be = me.getImageRepresentation();
                Image bs = be.getScaledInstance(roundWidth, roundWidth, Image.SCALE_SMOOTH);
                g.drawImage(bs, x_cast, y_cast, this);

                if(me instanceof  Animal){
                    Animal a = (Animal) me;

                    if(clicked)
                        clickedAnimal = a;
                    int x_energybar = x_cast + (int)(0.1*sqwidth);
                    int y_energybar = y_cast + (int)(0.7*sqwidth);
                    int barWidth = (int)(0.8*sqwidth);
                    int barHeight = (int)(0.2*sqwidth);

                    if(a.best)
                        g.setColor(Color.yellow);
                    else
                        g.setColor(Color.blue);
                    g.fillRect(x_energybar, y_energybar, (int)(barWidth*a.getEnergy()), barHeight);
                    g.setColor(Color.black);
                    g.drawRect(x_energybar, y_energybar, barWidth, barHeight);
                }
            }
        }
        if(clickedAnimal != null){
            g.setColor(Color.black);
            g.drawRect(0, 0, 300, 100);
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, 300, 100);
            g.setColor(Color.white);
            String genotype = clickedAnimal.genotype.toString();
            g.drawString(Translations.getTranslation("animal_energy") + " " + clickedAnimal.getEnergy(), 10, 15);
            g.drawString(Translations.getTranslation("animal_position") + " " + clickedAnimal.getPosition().toString(), 10, 35);
            g.drawString(Translations.getTranslation("animal_genotype") + " " + genotype.substring(0, 20), 10, 55);
            g.drawString(genotype.substring(21), 10, 70);

            g.setColor(Color.darkGray);
            g.fillRect(10, 70, 220, 25);
            g.setColor(Color.lightGray);
            g.drawRect(10, 70, 220, 25);
            g.setColor(Color.white);
            g.drawString(Translations.getTranslation("click_trace"), 15, 85);

            if(clickPosition != null){
                Vector2D clickStart = new Vector2D(10, 70);
                Vector2D clickEnd = new Vector2D(230, 95);
                if(clickStart.lowerLeft(clickPosition).equals(clickStart)
                && clickEnd.upperRight(clickPosition).equals(clickEnd)){
                    BirthTracer.getFromUser(clickedAnimal);
                }
            }
        }
        if(clickPosition != null){
            clickPosition = null;
            clickedAnimal = null;
        }

    }

    public void setMap(IWorldMap map){
        this.map = map;
    }
}
