package main.darwinworld.objects;

import main.darwinworld.MapObjectImage;
import main.darwinworld.math.Vector2D;

public class Grass implements IMapElement {
    private final Vector2D position;
    static MapObjectImage mapImage = null;
    public  float energy;

    public Grass(Vector2D position, float energy){
        this.energy = energy;
        if(mapImage == null)
            mapImage = MapObjectImage.load("grass.png");
        this.position = new Vector2D(position.x, position.y);
    }

    public Vector2D getPosition(){
        return new Vector2D(position.x, position.y);
    }

    @Override
    public MapObjectImage getImageRepresentation() {
        return mapImage;
    }

    @Override
    public String toString(){
        return "*";
    }
}

