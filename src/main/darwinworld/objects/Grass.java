package main.darwinworld.objects;

import main.darwinworld.MapObjectImage;
import main.darwinworld.model.Vector2D;

public class Grass implements IMapElement {
    private final Vector2D position;
    private static MapObjectImage mapImage = null;
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

    public void consume(Animal [] consumers){
        float energyToSpare = energy/consumers.length;
        for(Animal oneOfBest : consumers){
            oneOfBest.setEnergy(oneOfBest.getEnergy()+energyToSpare);
        }
    }
}

