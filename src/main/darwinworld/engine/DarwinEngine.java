package main.darwinworld.engine;

import main.darwinworld.logic.Genotype;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.MapDirection;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;

import java.util.*;

public class DarwinEngine implements IEngine{

    private final int runSteps;
    private final LinkedList<Animal> animals;
    private final Random generator;
    private final IWorldMap map;
    private int epoch;
    private final float energyDecline;
    private float averageEnergy;
    private long deadLifespanSum = 0;
    private long nDead = 0;
    private Genotype bestGenotype = null;
    private double averageChildrenNominator = 0;

    private final Map<int[], Integer> genotypeCounts;

    private void addGenotype(int [] genotype){
        if(genotypeCounts.containsKey(genotype))
            genotypeCounts.put(genotype, genotypeCounts.get(genotype)+1);
        else{
            genotypeCounts.put(genotype, 1);
        }
    }

    private void removeGenotype(int [] genotype){
        if(genotypeCounts.containsKey(genotype)){
            int cnt = genotypeCounts.get(genotype);
            if(--cnt > 0)
                genotypeCounts.put(genotype, cnt);
            else
                genotypeCounts.remove(genotype);
        }
    }

    public Genotype mostFrequentGenotype(){
        return bestGenotype;
    }

    @Override
    public double getAverageChildren() {
        if(animals.size() == 0) return 0;
        return averageChildrenNominator/animals.size();
    }

    public float getAverageEnergy(){
        return averageEnergy;
    }

    public int getEpoch(){
        return epoch;
    }

    public double getAverageLifespan(){
        if(nDead==0) return 0.0;
        else return ((double)deadLifespanSum)/nDead;
    }

    @Override
    public void run() {
        for(int i = 0; i < runSteps; i++)
            step();
    }

    public DarwinEngine(int numStartingAnimals, int runSteps, IWorldMap map, float energyDecline, float startingEnergy){
        genotypeCounts = new TreeMap<>((genotype1, genotype2) -> {
            if(genotype1.length < genotype2.length) return -1;
            for (int i = 0; i < genotype1.length; i++) {
                if (genotype1[i] > genotype2[i]) {
                    return 1;
                } else if (genotype1[i] < genotype2[i]) {
                    return -1;
                }
            }
            return 0;
        });

        this.energyDecline = energyDecline;
        this.runSteps = runSteps;
        this.animals = new LinkedList<>();
        this.map = map;
        epoch = 1;
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
            Animal newAnimal = new Animal(map, map.targetPositionMapping(animalPos));
            addGenotype(newAnimal.genotype.asArray());
            newAnimal.setEnergy(startingEnergy);
            animals.add(newAnimal);
        }
    }

    private void stepHunger(){
        animals.forEach(animal -> animal.setEnergy(animal.getEnergy()-energyDecline));
    }

    //returns average animal energy
    private float stepRemoveDead(){
        float energy = 0;
        animals.removeIf(animal -> {
            boolean dead = animal.getEnergy() <= 0;
            if(dead) {
                averageChildrenNominator-=animal.children.size();
                map.remove(animal);
                deadLifespanSum += animal.aliveFor;
                removeGenotype(animal.genotype.asArray());
                nDead ++;
                animal.deathEpoch = epoch;
            }
            return dead;});

        for(Animal a : animals){
            energy += a.getEnergy();
        }
        if(animals.size() > 0)
            energy /= animals.size();
        else
            energy = 0;
        return energy;
    }

    //returns a random free spot on map around a given position
    //or a random position if no spot is available at all
    private Vector2D getFreeSpot(Vector2D around){
        MapDirection targetDirection = MapDirection.NORTH;
        int rot = Math.floorMod(generator.nextInt(), 7);
        for(int i = 0; i < rot; i++){
            targetDirection = targetDirection.next();
        }
        MapDirection base = targetDirection;
        Vector2D possibleSpot = null;
        while(targetDirection!=base.previous()) {
            if(!map.animalPresentAt(map.targetPositionMapping(around.add(targetDirection.toUnitVector())))) {
                possibleSpot = map.targetPositionMapping(around.add(targetDirection.toUnitVector()));
                break;
            }
            targetDirection = targetDirection.next();
        }
        if(possibleSpot == null)
            possibleSpot = map.targetPositionMapping(around.add(targetDirection.toUnitVector()));
        return possibleSpot;
    }

    //returns true if something was born
    private boolean stepBreed(){
        boolean born = false;
        Animal[][] pairs = map.getBreedablePairs();
        for(Animal [] pair : pairs){
            born = true;
            Animal firstParent = pair[0] , secondParent = pair[1];
            //find free spot around firstParent and secondParent
            Vector2D possibleBirthPlace = getFreeSpot(firstParent.getPosition());

            Genotype bornGenotype = firstParent.genotype.giveBirth(secondParent.genotype);
            Animal child = new Animal(map, possibleBirthPlace, bornGenotype);

            firstParent.setEnergy(firstParent.getEnergy()-0.25f);
            secondParent.setEnergy(firstParent.getEnergy()-0.25f);

            firstParent.children.add(child);
            secondParent.children.add(child);

            child.setEnergy(0.5f);
            addGenotype(child.genotype.asArray());

            animals.add(child);
            averageChildrenNominator+=2;
        }
        return born;
    }

    private void stepWorld(){
        map.makeStep();
        epoch++;
    }

    private void recalculateBestGenotype(){
        if(genotypeCounts.size() > 0) {
            Integer highestCount = -1;
            int[] bGenes = null;
            for (Map.Entry<int[], Integer> current : genotypeCounts.entrySet()) {
                if (current.getValue() > highestCount) {
                    highestCount = current.getValue();
                    bGenes = current.getKey();
                }
            }

            bestGenotype = Genotype.fromArray(bGenes);

            animals.forEach(animal -> animal.best = animal.genotype.equals(bestGenotype));
        }
    }

    private void stepMove(){
        animals.forEach(animal->{
            animal.aliveFor++;
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
        averageEnergy = stepRemoveDead();
        //make animals move
        stepMove();
        //make animals eat
        map.updateAfterMoving();
        //make animals breed
        stepBreed();
        //make the grass grow
        stepWorld();

        recalculateBestGenotype();

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
