package main.darwinworld.engine;

import main.darwinworld.logic.Genotype;
import main.darwinworld.map.IWorldMap;
import main.darwinworld.map.MapDirection;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;

import java.util.*;

public class DarwinEngine implements IEngine{

    int runSteps;
    private LinkedList<Animal> animals;
    private Random generator;
    private IWorldMap map;
    private int epoch;
    private float foodEnergy;
    private float energyDecline;
    private float ener;
    private long deadLifespanSum = 0;
    private long nDead = 0;
    private Genotype bestGenotype = null;
    private double averageChildrenNominator = 0;

    private Map<int[], Integer> genotypeCounts;

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
        return ener;
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
            run();
    }

    public DarwinEngine(int numStartingAnimals, int runSteps, IWorldMap map, float energyDecline, float startingEnergy){
        genotypeCounts = new TreeMap<>((o1, o2) -> {
            if(o1.length < o2.length) return -1;
            for (int i = 0; i < o1.length; i++) {
                if (o1[i] > o2[i]) {
                    return 1;
                } else if (o1[i] < o2[i]) {
                    return -1;
                }
            }
            return 0;
        });


        this.foodEnergy = foodEnergy;
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
            Animal a = new Animal(map, map.targetPositionMapping(animalPos));
            addGenotype(a.genotype.asArray());
            a.setEnergy(startingEnergy);
            animals.add(a);
        }
    }

    @Override
    public boolean step() {
        //make animals hungry
        animals.forEach(animal -> animal.setEnergy(animal.getEnergy()-energyDecline));
        ener = 0;
        //make the dead rest
        animals.removeIf(animal -> {
            boolean dead = animal.getEnergy() <= 0;
            if(dead) {
                averageChildrenNominator-=animal.nChildren;
                map.remove(animal);
                deadLifespanSum += animal.aliveFor;
                removeGenotype(animal.genotype.asArray());
                nDead ++;
                animal.deathEpoch = epoch;
            }
            return dead;});

        for(Animal a : animals){
            ener += a.getEnergy();
        }
        if(animals.size() > 0)
            ener /= animals.size();
        else
            ener = 0;

        //make animals move
        animals.forEach(animal->{
                animal.aliveFor++;
                Vector2D p = animal.getPosition();
                animal.move(animal.genotype.getMove());
                animal.positionChanged(p, animal.getPosition());
        });

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
            if(possibleBirthPlace == null)
                possibleBirthPlace = map.targetPositionMapping(a.getPosition().add(md.toUnitVector()));
            nbirths +=1;
            Genotype g = a.genotype.giveBirth(b.genotype);
            Animal child = new Animal(map, possibleBirthPlace, g);
            a.setEnergy(a.getEnergy()-0.25f);
            b.setEnergy(a.getEnergy()-0.25f);
            a.nChildren++;
            b.nChildren++;
            child.parentsTracing.add(a);
            child.parentsTracing.add(b);
            child.setEnergy(0.5f);
            addGenotype(child.genotype.asArray());
            child.notifyBirthTracers();
            animals.add(child);
            averageChildrenNominator+=2;
        }

        //make the grass grow
        map.makeStep();
        epoch++;

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

            animals.forEach(animal -> {
                if(animal.genotype.equals(bestGenotype)){
                    animal.best = true;
                }
                else{
                    animal.best = false;
                }
            });
        }


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
