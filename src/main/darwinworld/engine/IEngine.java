package main.darwinworld.engine;

import main.darwinworld.logic.Genotype;

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
}
