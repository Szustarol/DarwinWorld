package main.darwinworld.view.misc;

public class SetupResult {
    public int n_animals;
    public int map_width;
    public int map_height;
    public int jungle_percent;
    public double food_value;
    public double food_decay;
    public double food_starting;

    public SetupResult(int nAnimals, int mapWidth, int mapHeight,
                       int junglePercent, double foodValue, double foodDecay, double startingFood) {
        n_animals = nAnimals;
        map_width = mapWidth;
        map_height = mapHeight;
        jungle_percent = junglePercent;
        food_value = foodValue;
        food_decay = foodDecay;
        food_starting = startingFood;
    }
}
