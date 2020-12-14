package main.darwinworld.map;

import main.darwinworld.model.Vector2D;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    private static final String [] names = {"Północ", "Północny wschód", "Wschód", "Południowy wschód", "Południe",
            "Południowy zachód", "Zachód", "Północny zachód"};

    private static final Vector2D[] vectors = {new Vector2D(0, 1), new Vector2D(1, 1), new Vector2D(1, 0),
            new Vector2D(1, -1), new Vector2D(0, -1), new Vector2D(-1, -1), new Vector2D(-1, 0), new Vector2D(-1, 1)};

    public MapDirection next(){
        return MapDirection.values()[(this.ordinal()+1)%vectors.length];
    }

    public MapDirection previous(){
        return MapDirection.values()[this.ordinal() == 0 ? (vectors.length-1) : this.ordinal()-1];
    }

    @Override
    public String toString(){
        return names[this.ordinal()];
    }

    public Vector2D toUnitVector(){
        return vectors[this.ordinal()];
    }

    public float toAngle(){
        switch(this){
            case NORTH:
                return 0;
            case NORTHEAST:
                return 45;
            case EAST:
                return 90;
            case SOUTHEAST:
                return 135;
            case SOUTH:
                return 180;
            case SOUTHWEST:
                return 225;
            case WEST:
                return 270;
            case NORTHWEST:
                return 315;
        }
        return 0;
    }

}
