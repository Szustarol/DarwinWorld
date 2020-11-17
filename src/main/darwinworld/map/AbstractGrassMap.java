package main.darwinworld.map;

import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.IMapElement;

import java.util.TreeMap;

public abstract class AbstractGrassMap extends AbstractWorldMap {

    protected TreeMap<Vector2D, IMapElement> otherElements;

    @Override
    protected IMapElement otherElementAt(Vector2D position) {
        if (!otherElementsPresent(position))
            return null;
        return otherElements.get(position);
    }

    public abstract void afterRemoval(Vector2D fromPosition, IMapElement elem);


    public AbstractGrassMap(){
        otherElements = new TreeMap<Vector2D, IMapElement>();
    }

    @Override
    protected boolean otherElementsPresent(Vector2D position) {
        return otherElements.containsKey(position);
    }
}
