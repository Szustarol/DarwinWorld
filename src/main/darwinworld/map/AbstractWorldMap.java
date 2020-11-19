package main.darwinworld.map;

import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;
import main.darwinworld.objects.IMapElement;

import java.awt.*;
import java.util.LinkedList;
import java.util.TreeMap;

public abstract class AbstractWorldMap implements IWorldMap {

    protected TreeMap<Vector2D, LinkedList<Animal>> animalPositions;


    protected  AbstractWorldMap(){
        this.animalPositions = new TreeMap<>();
    }

    protected abstract boolean otherElementsPresent(Vector2D position);
    protected abstract IMapElement otherElementAt(Vector2D position);

    @Override
    public boolean canMoveTo(Vector2D position) {
        return true;
    }

    public abstract Vector2D[] getBounds();

    @Override
    public String toString(){
        return "";
    }

    public void unableToPlaceExceptionWrapper(Vector2D position){
        throw new IllegalArgumentException("Unable to place an object at position: " + position.toString());
    }

    private void animalInsert(Vector2D position, Animal a){
        if (!animalPositions.containsKey(position)) {
            animalPositions.put(position, new LinkedList<>());
        }
        animalPositions.get(position).add(a);
    }

    private boolean animalRemove(Animal animal, Vector2D position){
        if(!animalPositions.containsKey(position))
            return false;
        boolean removed =  animalPositions.get(position).remove(animal);
        if(animalPositions.get(position).size() == 0)
            animalPositions.remove(position);
        return removed;
    }

    @Override
    public boolean place(Animal animal) {
        Vector2D pos = animal.getPosition();
        if(canMoveTo(pos)){
            animalInsert(pos, animal);
            animal.addObserver(this);
            this.positionChanged(pos, pos, animal); // to update edible objects
            return true;
        }
        else{
            unableToPlaceExceptionWrapper(animal.getPosition());
            return false;
        }
    }

    @Override
    public void remove(Animal animal){
        animalRemove(animal, animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return animalPositions.containsKey(position) || otherElementsPresent(position);
    }

    @Override
    public boolean animalPresentAt(Vector2D position){
        return animalPositions.containsKey(position);
    }

    @Override
    public IMapElement[] objectsAt(Vector2D position) {
        LinkedList<IMapElement> objs = new LinkedList<>();
        if(!isOccupied(position))
            return null;
        if (animalPositions.containsKey(position))
            objs.addAll(animalPositions.get(position));
        if(otherElementsPresent(position))
            objs.add(otherElementAt(position));
        return  objs.toArray(new IMapElement[0]);
    }

    @Override
    public void positionChanged(Vector2D oldPosition, Vector2D newPosition, IMapElement sender) {
        if(sender instanceof  Animal){
            boolean rem = animalRemove((Animal)sender, oldPosition);
            if(rem){
                animalInsert(newPosition, (Animal)sender);
            }
        }
    }

    public Vector2D targetPositionMapping(Vector2D position){
        return position;
    }

    public Animal[][] getBreedablePairs(){
        LinkedList<Animal[]> breedable = new LinkedList<>();
        for(Vector2D key : animalPositions.keySet()){
            // test if can breed
            if(animalPositions.get(key).size() > 1) {
                LinkedList<Animal> anims = animalPositions.get(key);
                anims.sort((animal, t1) -> {if(t1 == animal) return 0; if(t1.getEnergy() <= animal.getEnergy()) return 1; return -1;});
                if(anims.getLast().getEnergy() > 0.5 && anims.get(anims.size()-2).getEnergy() > 0.5){
                    breedable.add(
                            new Animal[]{anims.getLast(), anims.get(anims.size()-2)}
                    );
                }
            }
        }
        return breedable.toArray(new Animal[breedable.size()][]);
    }


    public abstract void makeStep();

    public abstract void updateAfterMoving();

    public Color getTileColor(Vector2D position){
        return Color.BLACK;
    }
}