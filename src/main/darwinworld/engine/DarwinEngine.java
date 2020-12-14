package main.darwinworld.engine;

import main.darwinworld.model.Genotype;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.model.Vector2D;
import main.darwinworld.objects.Animal;

import java.util.*;

public class DarwinEngine implements IEngine{

    private final int runSteps;
    private final LinkedList<Animal> animals;
    private final IWorldMap map;
    private int epoch;
    private final float energyDecline;

    private final EngineStatsCounter engineStatsCounter;

    public DarwinEngine(int numStartingAnimals, int runSteps, IWorldMap map, float energyDecline, float startingEnergy,
                        IEngineInitializer init){
        this.energyDecline = energyDecline;
        this.runSteps = runSteps;
        this.animals = new LinkedList<>();
        this.map = map;
        epoch = 1;
        engineStatsCounter = new EngineStatsCounter(numStartingAnimals);
        if(init == null)
            init = new RandomEngineInitializer();
        init.init(animals, map, numStartingAnimals, startingEnergy);
    }


    public Genotype mostFrequentGenotype(){
        return engineStatsCounter.getBestGenotype();
    }

    @Override
    public double getAverageChildren() {
        return engineStatsCounter.getAverageChildren();
    }

    public float getAverageEnergy(){
        return engineStatsCounter.getAverageEnergy();
    }

    public int getEpoch(){
        return epoch;
    }

    public double getAverageLifespan(){
        return engineStatsCounter.getAverageLifespan();
    }

    public boolean isAnimalDominant(Animal a){
        return engineStatsCounter.isGenotypeBest(a.genotype);
    }

    @Override
    public void run() {
        for(int i = 0; i < runSteps; i++)
            step();
    }


    private void stepHunger(){
        animals.forEach(animal -> animal.setEnergy(animal.getEnergy()-energyDecline));
    }

    //returns average animal energy
    private float stepRemoveDead(){
        animals.removeIf(animal -> {
            boolean dead = animal.getEnergy() <= 0;
            if(dead) {
                map.remove(animal);
                animal.deathEpoch = epoch;
                engineStatsCounter.animalDead(animal);
            }
            return dead;});


        engineStatsCounter.calculateAverageEnergy(animals);
        return engineStatsCounter.getAverageEnergy();
    }

    //returns true if something was born
    private boolean stepBreed(){
        boolean born = false;
        Animal[][] pairs = map.getBreedablePairs();
        for(Animal [] pair : pairs){
            born = true;
            Animal firstParent = pair[0] , secondParent = pair[1];

            Animal child = firstParent.breed(secondParent);

            animals.add(child);

            animals.add(child);

            engineStatsCounter.childrenBorn(child);
        }
        return born;
    }

    private void stepWorld(){
        map.makeStep();
        epoch++;
    }


    private void stepMove(){
        animals.forEach(animal->{
            Vector2D initialPosition = animal.getPosition();
            animal.move(animal.genotype.getMove());
            animal.positionChanged(initialPosition, animal.getPosition());
        });
    }

    @Override
    public boolean step() {
        //make animals hungry
        stepHunger();
        //make the dead rest
        stepRemoveDead();
        //make animals move
        stepMove();
        //make animals eat
        map.updateAfterMoving();
        //make animals breed
        stepBreed();
        //make the grass grow
        stepWorld();

        engineStatsCounter.recalculateBestGenotype();
        engineStatsCounter.setAliveanimals(animals.size());

        return true;
    }

    @Override
    public int getAnimalCount() {
        return animals.size();
    }

    @Override
    public int getOtherElementsCount() {
        return map.getOtherElementsSize();
    }
}
