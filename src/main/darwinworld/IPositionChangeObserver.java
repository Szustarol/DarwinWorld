package main.darwinworld;

import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.IMapElement;

public interface IPositionChangeObserver {
    void positionChanged(Vector2D oldPosition, Vector2D newPosition, IMapElement sender);
}