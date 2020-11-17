package main.darwinworld.map;

import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;
import main.darwinworld.objects.Grass;
import main.darwinworld.objects.IMapElement;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class WrappedMap extends AbstractGrassMap{

    public final Vector2D mapSize;
    public final Vector2D jungleSize;
    public final Vector2D junglePosition;

    private Random generator;
    private float foodEnergy;

    int jungleCount = 0;
    int restCount = 0;

    public boolean isInJungle(Vector2D pos){
        return pos.x >= junglePosition.x && pos.y >= junglePosition.y
                && pos.x < junglePosition.x + jungleSize.x
                && pos.y < junglePosition.y + jungleSize.y;
    }

    public int getJungleAnimalCount(){
        int otherCount = 0;
        for(int x = junglePosition.x; x < junglePosition.x + jungleSize.x; x++){
            for(int y = junglePosition.x; y < junglePosition.x + jungleSize.x; y++){
                if(animalPositions.containsKey(new Vector2D(x, y)))
                    otherCount+=1;
            }
        }
        return otherCount;
    }

    //places one grass in the remaining region
    private void placeRest(){
        if(restCount >= mapSize.x*mapSize.y-jungleSize.x*jungleSize.y- animalPositions.size()-otherElements.size()+getJungleAnimalCount()+jungleCount)
            return;
        int x = Math.floorMod(generator.nextInt(), mapSize.x);
        int y = Math.floorMod(generator.nextInt(), mapSize.y);
        Vector2D vec = new Vector2D(x, y);
        while(animalPositions.containsKey(vec) || otherElements.containsKey(vec) || isInJungle(vec)){
            x++;
            if(x == mapSize.x){
                x = 0;
                y = (y+1)%mapSize.y;
            }
            vec = new Vector2D(x, y);
        }
        otherElements.put(vec, new Grass(vec, foodEnergy));
        restCount ++;
    }

    //places one grass in the center region
    private void placeJungle(int n){
        for(int i = 0; i < n; i++) {
            int otherCount = getJungleAnimalCount();

            if(jungleCount >= jungleSize.x*jungleSize.y - otherCount)
                return;
            int x = Math.floorMod(generator.nextInt(), jungleSize.x) + junglePosition.x;
            int y = Math.floorMod(generator.nextInt(), jungleSize.y) + junglePosition.y;
            Vector2D vec = targetPositionMapping(new Vector2D(x, y));
            while (animalPositions.containsKey(vec) || otherElements.containsKey(vec)) {
                x++;
                if (x == junglePosition.x + jungleSize.x) {
                    x = junglePosition.x;
                    y++;
                    if (y == junglePosition.y + jungleSize.y) {
                        y = junglePosition.y;
                    }
                }
                vec = targetPositionMapping(new Vector2D(x, y));
            }
            otherElements.put(vec, new Grass(vec, foodEnergy));
            jungleCount++;
        }
    }

    public WrappedMap(Vector2D mapSize, Vector2D jungleSize, float foodEnergy){
        super();
        this.foodEnergy = foodEnergy;
        this.mapSize = new Vector2D(mapSize.x, mapSize.y);
        this.jungleSize = new Vector2D(jungleSize.x, jungleSize.y);
        int jX = (int) Math.round((mapSize.x-jungleSize.x)/2.0);
        int jY = (int) Math.round((mapSize.y-jungleSize.y)/2.0);
        this.junglePosition = new Vector2D(jX, jY);
        generator = new Random();
        placeJungle(1);
        placeRest();
    }

    @Override
    public void afterRemoval(Vector2D fromPosition, IMapElement elem) {
        if(isInJungle(fromPosition))
            jungleCount--;
        else
            restCount--;
    }


    @Override
    public Vector2D targetPositionMapping(Vector2D position){
        return new Vector2D(
                Math.floorMod(position.x, mapSize.x),
                Math.floorMod(position.y, mapSize.y)
        );
    }

    @Override
    public void makeStep() {
        placeJungle(1);
        placeRest();
    }

    public Color getTileColor(Vector2D position){
        if(isInJungle(position))
            return new Color(0x00, 0x6e, 0x33);
        return new Color(0x63, 0xc7, 0x92);
    }

    @Override
    public void updateAfterMoving() {
        LinkedList<Vector2D> toRemove = new LinkedList<>();
        for(IMapElement piece : otherElements.values()){
            if(piece instanceof Grass){
                Vector2D pos = piece.getPosition();
                if(animalPositions.containsKey(pos)){
                    LinkedList<Animal> an = animalPositions.get(pos);
                    an.sort((animal, t1) -> {if(t1.getEnergy() <= animal.getEnergy()) return 1; return -1;});
                    //get most healthy animals
                    Animal[] best = an.stream().filter(animal -> animal.getEnergy() == an.getLast().getEnergy()).toArray(Animal[]::new);
                    float energyToSpare = piece.getEnergy()/best.length;
                    for(Animal a : best){
                        a.setEnergy(a.getEnergy()+energyToSpare);
                    }
                    toRemove.add(pos);
                    if(isInJungle(pos))
                        jungleCount--;
                    else
                        restCount--;
                }
            }
        }
        for(Vector2D pos : toRemove){
            otherElements.remove(pos);
        }
    }

    @Override
    public Vector2D[] getBounds() {
        return new Vector2D[]{new Vector2D(0, 0), new Vector2D(mapSize.x-1, mapSize.y-1)};
    }
}