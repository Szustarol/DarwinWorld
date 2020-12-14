package main.darwinworld.view.misc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetupResult {

    @JsonProperty("n_animals")
    public int n_animals;

    @JsonProperty("map_width")
    public int map_width;

    @JsonProperty("map_height")
    public int map_height;

    @JsonProperty("jungle_percent")
    public int jungle_percent;

    @JsonProperty("food_value")
    public double food_value;

    @JsonProperty("food_decay")
    public double food_decay;

    @JsonProperty("food_starting")
    public double food_starting;


    public SetupResult(){};

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
