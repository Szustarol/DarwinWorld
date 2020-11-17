package main.darwinworld.map;

import main.darwinworld.IPositionChangeObserver;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;
import main.darwinworld.objects.IMapElement;

import java.awt.*;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */

public interface IWorldMap extends IPositionChangeObserver {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2D position);

    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);

    //remove an animal from map
    void remove(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2D position);

    boolean animalPresentAt(Vector2D position);

    void updateAfterMoving();

    void makeStep();

    Color getTileColor(Vector2D position);

    Animal[][] getBreedablePairs();

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
    IMapElement[] objectsAt(Vector2D position);

    Vector2D targetPositionMapping(Vector2D position);

    Vector2D[] getBounds();
}
