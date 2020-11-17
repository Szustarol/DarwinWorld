package main.darwinworld.engine;

import main.darwinworld.logic.Genotype;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.MapDirection;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;

import java.util.*;

public class DarwinEngine implements IEngine{

    int runSteps;
    private LinkedList<Animal> animals;
    private Random generator;
    private IWorldMap map;
    private float foodEnergy;
    private float energyDecline;

    @Override
    public void run() {
        for(int i = 0; i < runSteps; i++)
            run();
    }

    public DarwinEngine(int numStartingAnimals, int runSteps, IWorldMap map, float energyDecline, float startingEnergy){
        this.foodEnergy = foodEnergy;
        this.energyDecline = energyDecline;
        this.runSteps = runSteps;
        this.animals = new LinkedList<>();
        this.map = map;
        generator = new Random();
        for(int i = 0; i < numStartingAnimals; i++){
            int x = generator.nextInt();
            int y = generator.nextInt();
            Vector2D animalPos = new Vector2D(x, y);
            while(map.isOccupied(map.targetPositionMapping(animalPos))){
                x += 1;
                if(x > map.getBounds()[1].x){
                    x = map.getBounds()[0].x;
                    y += 1;
                }
                if(y > map.getBounds()[1].y){
                    y = map.getBounds()[0].y;
                }
                animalPos = new Vector2D(x, y);
            }
            Animal a = new Animal(map, map.targetPositionMapping(animalPos));
            a.setEnergy(startingEnergy);
            animals.add(a);
        }
    }

    @Override
    public boolean step() {
        //make animals hungry
        animals.forEach(animal -> animal.setEnergy(animal.getEnergy()-energyDecline));
        float ener = 0;
        //make the dead rest
        animals.removeIf(animal -> {
            boolean dead = animal.getEnergy() <= 0;
            if(dead)
                map.remove(animal);
            return dead;});
        for(Animal a : animals){
            ener += a.getEnergy();
        }

        //make animals move
        animals.forEach(animal->{
                Vector2D p = animal.getPosition();
                animal.move(animal.genotype.getMove());
                animal.positionChanged(p, animal.getPosition());}
        );

        //make animals eat
        map.updateAfterMoving();

        //make animals breed
        Animal[][] pairs = map.getBreedablePairs();
        int nbirths = 0;
        for(Animal [] pair : pairs){
            Animal a = pair[0] , b = pair[1];
            //find free spot around a and b
            MapDirection md = MapDirection.NORTH;
            int rot = Math.floorMod(generator.nextInt(), 7);
            for(int i = 0; i < rot; i++){
                md = md.next();
            }
            MapDirection base = md;
            Vector2D possibleBirthPlace = null;
            while(md!=base.previous()) {
                if(!map.animalPresentAt(map.targetPositionMapping(a.getPosition().add(md.toUnitVector())))) {
                    possibleBirthPlace = map.targetPositionMapping(a.getPosition().add(md.toUnitVector()));
                    break;
                }
                md = md.next();
            }
            if(possibleBirthPlace != null){
                nbirths +=1;
                Genotype g = a.genotype.giveBirth(b.genotype);
                Animal child = new Animal(map, possibleBirthPlace, g);
                a.setEnergy(a.getEnergy()-0.25f);
                b.setEnergy(a.getEnergy()-0.25f);
                child.setEnergy(0.5f);
                animals.add(child);
            }
        }

        //make the grass grow
        map.makeStep();

        return true;
    }
}
