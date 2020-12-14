package main.darwinworld.objects;

import main.darwinworld.MapObjectImage;
import main.darwinworld.model.Vector2D;

public interface IMapElement {
    Vector2D getPosition();
    MapObjectImage getImageRepresentation();
}
