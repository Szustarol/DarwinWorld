package main.darwinworld.engine;

import main.darwinworld.model.Genotype;
import main.darwinworld.objects.Animal;

public interface IEngine {
    void run();

    boolean step();

    int getAnimalCount();

    int getOtherElementsCount();

    int getEpoch();

    float getAverageEnergy();

    double getAverageLifespan();

    Genotype mostFrequentGenotype();

    double getAverageChildren();

    boolean isAnimalDominant(Animal a);
}
