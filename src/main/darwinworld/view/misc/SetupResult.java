package main.darwinworld.view.misc;

public class SetupResult {
    public int n_animals;
    public int map_width;
    public int map_height;
    public int jungle_percent;
    public double food_value;
    public double food_decay;
    public double food_starting;

    public SetupResult() {

    }

    public SetupResult(int a, int b, int c, int d, double e, double f, double g) {
        n_animals = a;
        map_width = b;
        map_height = c;
        jungle_percent = d;
        food_value = e;
        food_decay = f;
        food_starting = g;
    }
}
