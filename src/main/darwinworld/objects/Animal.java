package main.darwinworld.objects;

import main.darwinworld.IPositionChangeObserver;
import main.darwinworld.MapObjectImage;
import main.darwinworld.model.Genotype;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.MapDirection;
import main.darwinworld.model.Vector2D;

import java.util.*;

public class Animal implements IMapElement, Comparable<Animal> {
    private Vector2D position;
    private MapDirection orientation;
    private IWorldMap map;
    private LinkedList<IPositionChangeObserver> observers;
    private static MapObjectImage mapImage = null;
    private float energy;
    private int animalID;
    private int aliveFor = 1;

    private static int newAnimalID = 0;

    public Genotype genotype;
    public int deathEpoch = -1;
    public LinkedList<Animal> children;

    public Animal breed(Animal other){
        Vector2D possibleBirthPlace = map.getFreeSpotAround(getPosition());
        Genotype bornGenotype = genotype.giveBirth(other.genotype);

        Animal child = new Animal(map, possibleBirthPlace, bornGenotype);
        child.setEnergy(getEnergy()*0.25f + other.getEnergy()*0.25f);
        children.add(child);
        other.children.add(child);

        setEnergy(0.75f*getEnergy());
        other.setEnergy(0.75f*other.getEnergy());
        return child;
    }

    public int getAliveFor(){
        return aliveFor;
    }

    public Set<Animal> getDescendants(){
        TreeSet<Animal> descendantsSet = new TreeSet<>();
        for(Animal child : children){
            descendantsSet.addAll(child.getDescendants());
            descendantsSet.add(child);
        }
        return descendantsSet;
    }


    public void setEnergy(float energy){
        if(energy > 1)
            this.energy = 1;
        else if(energy < 0)
            this.energy = 0;
        else
            this.energy = energy;
    }

    public float getEnergy() {
        return energy;
    }

    public Vector2D getPosition(){//required for tests
        return new Vector2D(position.x, position.y);
    }

    @Override
    public MapObjectImage getImageRepresentation() {
        return mapImage.rotated(orientation.toAngle());
    }

    public Animal(IWorldMap map, Vector2D position){
        this(map, position, new Genotype(32));
    }

    public Animal(IWorldMap map, Vector2D position, Genotype genotype){
        animalID = newAnimalID;
        newAnimalID++;
        children = new LinkedList<>();
        this.observers = new LinkedList<>();
        orientation = MapDirection.NORTH;
        this.map = map;
        if(mapImage == null)
            mapImage = MapObjectImage.load("animal.png");
        this.position = new Vector2D(position.x, position.y);
        map.place(this);
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
        aliveFor++;
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


    @Override
    public int compareTo(Animal animal) {
        return this.animalID - animal.animalID;
    }
}
