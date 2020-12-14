package main.darwinworld.engine;

import main.darwinworld.model.Genotype;
import main.darwinworld.objects.Animal;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class EngineStatsCounter {

    private float averageEnergy;
    private long deadLifespanSum = 0;
    private long nDead = 0;
    private Genotype bestGenotype = null;
    private double averageChildrenNumerator;
    private int nAliveAnimals;

    private final Map<int[], Integer> genotypeCounts;

    public EngineStatsCounter(int startingAnimals){
        nAliveAnimals = startingAnimals;
        averageChildrenNumerator = 0;
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
    }


    public Genotype getBestGenotype(){
        return bestGenotype;
    }

    public void calculateAverageEnergy(LinkedList<Animal> animals){
        float averageEnergy = 0;
        for(Animal animal : animals)
            averageEnergy += animal.getEnergy();
        this.averageEnergy = averageEnergy;
        if(animals.size() > 0)
            this.averageEnergy/=animals.size();
    }

    public void recalculateBestGenotype(){
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
        }
    }


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

    public boolean isGenotypeBest(Genotype g){
        if(bestGenotype == null)
            return false;
        return g.equals(bestGenotype);
    }


    public void childrenBorn(Animal a){
        this.averageChildrenNumerator +=2;
        addGenotype(a.genotype.asArray());
    }

    public void animalDead(Animal a){
        nDead++;
        averageChildrenNumerator -= a.children.size();
        deadLifespanSum += a.getAliveFor();
        removeGenotype(a.genotype.asArray());
    }

    public double getAverageLifespan(){
        if(nDead == 0)
            return 0;
        else
            return (double)deadLifespanSum/nDead;
    }

    public float getAverageEnergy(){
        return averageEnergy;
    }

    public double getAverageChildren(){
        if(nAliveAnimals == 0 || averageChildrenNumerator == 0)
            return 0;
        else
            return  averageChildrenNumerator /nAliveAnimals;
    }

    public void setAliveanimals(int nAliveAnimals){
        this.nAliveAnimals = nAliveAnimals;
    }
}
