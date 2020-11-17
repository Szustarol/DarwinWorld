package main.darwinworld.objects;

import main.darwinworld.MapObjectImage;
import main.darwinworld.math.Vector2D;

public interface IMapElement {
    //for animals - their energy, for grass - energy if consumed
    float getEnergy();
    Vector2D getPosition();
    boolean consumedByAnimal();
    MapObjectImage getImageRepresentation();
}
