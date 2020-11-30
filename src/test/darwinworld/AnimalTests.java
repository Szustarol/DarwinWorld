package test.darwinworld;

import main.darwinworld.map.MapDirection;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.math.Vector2D;
import main.darwinworld.objects.Animal;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnimalTests {

    @Test
    public void wrappedMovementTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(4, 4), new Vector2D(1, 1), 0.5f);
        Animal a = new Animal(wm, new Vector2D(1, 1));
        a.move(MapDirection.NORTH);
        a.move(MapDirection.NORTHEAST);
        assertEquals(a.getPosition(), new Vector2D(2, 3));
        a.move(MapDirection.EAST);
        a.move(MapDirection.NORTH);
        assertEquals(a.getPosition(), new Vector2D(3, 0));
    }


    @Test
    public void descendantsTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(10, 10), new Vector2D(2, 2), 0.02f);
        Animal grandAnimal = new Animal(wm, new Vector2D(1, 1));
        Animal parent1 = new Animal(wm, new Vector2D(1, 1));
        Animal parent2 = new Animal(wm, new Vector2D(1, 1));
        grandAnimal.children.add(parent1);
        grandAnimal.children.add(parent2);
        Animal child1 = new Animal(wm, new Vector2D(1,1));
        parent1.children.add(child1);
        parent2.children.add(child1);
        assertEquals(grandAnimal.getDescendants().size(), 3);
    }

    @Test
    public void observerTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(10, 10), new Vector2D(2, 2), 0.02f);
        Animal animal = new Animal(wm, new Vector2D(1, 1));
        Vector2D initialPos = animal.getPosition();
        animal.move(MapDirection.NORTH);
        animal.positionChanged(initialPos, animal.getPosition());
        assertTrue(wm.isOccupied(animal.getPosition()));
        assertFalse(wm.isOccupied(initialPos));
    }

}
