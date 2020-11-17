package main.darwinworld.objects;

import main.darwinworld.IPositionChangeObserver;
import main.darwinworld.MapObjectImage;
import main.darwinworld.logic.Genotype;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.MapDirection;
import main.darwinworld.math.Vector2D;

import java.util.LinkedList;

public class Animal implements IMapElement{
    private Vector2D position;
    private MapDirection orientation;
    private IWorldMap map;
    private LinkedList<IPositionChangeObserver> observers;
    private static MapObjectImage mapImage = null;
    private float energy;
    public Genotype genotype;

    public void setEnergy(float energy){
        if(energy > 1)
            this.energy = 1;
        else if(energy < 0)
            this.energy = 0;
        else
            this.energy = energy;
    }

    @Override
    public float getEnergy() {
        return energy;
    }

    public Vector2D getPosition(){//required for tests
        Vector2D d = new Vector2D(position.x, position.y);
        return d;
    }

    @Override
    public boolean consumedByAnimal() {
        return false;
    }

    @Override
    public MapObjectImage getImageRepresentation() {
        return mapImage.rotated(orientation.toAngle());
    }

    private void AnimalConstruct(IWorldMap map, Vector2D position){
        this.observers = new LinkedList<IPositionChangeObserver>();
        orientation = MapDirection.NORTH;
        this.map = map;
        if(mapImage == null)
            mapImage = MapObjectImage.load("animal.png");
        this.position = new Vector2D(position.x, position.y);
        map.place(this);
    }

    public Animal(IWorldMap map, Vector2D position){
        AnimalConstruct(map, position);
        this.genotype = new Genotype(32);
    }

    public Animal(IWorldMap map, Vector2D position, Genotype genotype){
        AnimalConstruct(map, position);
        this.genotype = genotype;
    }

    @Override
    public String toString(){
        return orientation.toString();
    }

    public void move(MapDirection direction){
        Vector2D newPosition = position.add(direction.toUnitVector());
        orientation = direction;
        newPosition = map.targetPositionMapping(newPosition);
        if(map.canMoveTo(newPosition)){
            this.position = newPosition;
        }
    }

    public void positionChanged(Vector2D oldPosition, Vector2D newPosition){
        observers.forEach(obs -> obs.positionChanged(oldPosition, newPosition, this));
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.removeIf(obs -> obs == observer);
    }
}