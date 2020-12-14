package main.darwinworld.model;

public class Vector2D implements  Comparable<Vector2D>{

    public final int x, y;

    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "(" + x + "," + y +")";
    }

    @Override
    public boolean equals(Object other){
        if(this == other)
            return true;
        if(! ( other instanceof Vector2D))
            return false;
        return ((Vector2D) other).x == this.x && ((Vector2D) other).y == this.y;
    }

    @Override
    public int compareTo(Vector2D other){//required for map tree ordering\
        if(this.equals(other))
            return 0;
        if(this.x < other.x)
            return -1;
        else if(this.x == other.x)
            return this.y - other.y;
        else
            return 1;

    }

    public boolean precedes(Vector2D other){
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2D other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2D upperRight(Vector2D other){
        return new Vector2D(
                Integer.max(this.x, other.x), Integer.max(this.y, other.y)
        );
    }

    public Vector2D lowerLeft(Vector2D other){
        return new Vector2D(
                Integer.min(this.x, other.x), Integer.min(this.y, other.y)
        );
    }

    public Vector2D add(Vector2D other){
        return new Vector2D(
                this.x + other.x, this.y + other.y
        );
    }

    public Vector2D substract(Vector2D other){
        return new Vector2D(
                this.x - other.x, this.y - other.y
        );
    }

    public Vector2D opposite(){
        return new Vector2D(this.y, this.x);
    }
}
