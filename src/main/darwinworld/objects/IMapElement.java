package main.darwinworld.objects;

import main.darwinworld.MapObjectImage;
import main.darwinworld.math.Vector2D;

public interface IMapElement {
    Vector2D getPosition();
    MapObjectImage getImageRepresentation();
}
