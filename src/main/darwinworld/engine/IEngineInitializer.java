package main.darwinworld.engine;

import main.darwinworld.map.IWorldMap;
import main.darwinworld.objects.Animal;
import main.darwinworld.objects.IMapElement;

import java.util.Collection;

public interface IEngineInitializer {
    void init(Collection<Animal> engineAnimalsCollection, IWorldMap map, int nStartingAnimals, float startingAnimalEnergy);
}
