package main.darwinworld.engine;

import main.darwinworld.map.IWorldMap;
import main.darwinworld.model.Vector2D;
import main.darwinworld.objects.Animal;
import main.darwinworld.objects.IMapElement;

import java.util.Collection;
import java.util.Random;

public class RandomEngineInitializer implements IEngineInitializer{
    private Random generator;

    public RandomEngineInitializer(){
        generator = new Random();
    };


    @Override
    public void init(Collection<Animal> engineAnimalsCollection, IWorldMap map, int nStartingAnimals, float startingAnimalEnergy) {
        for(int i = 0; i < nStartingAnimals; i++){
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
            newAnimal.setEnergy(startingAnimalEnergy);
            engineAnimalsCollection.add(newAnimal);
        }
    }
}
