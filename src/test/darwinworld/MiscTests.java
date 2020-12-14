package test.darwinworld;

import main.darwinworld.map.MapDirection;
import main.darwinworld.model.Vector2D;
import main.darwinworld.objects.Grass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class MiscTests {
    @Test
    public void unloadedImageTest(){
        Grass g = new Grass(new Vector2D(1,1), 0.1f);
        assertNotEquals(g.getImageRepresentation(), null);
    }

    @Test
    public void mapDirectionTest(){
        MapDirection md = MapDirection.NORTH;
        MapDirection[] target = {MapDirection.NORTH, MapDirection.NORTHEAST,
        MapDirection.EAST, MapDirection.SOUTHEAST, MapDirection.SOUTH,
        MapDirection.SOUTHWEST, MapDirection.WEST, MapDirection.NORTHWEST, MapDirection.NORTH};

        for(int i = 0; i < target.length; i++){
            assertEquals(md, target[i]);
            md = md.next();
        }
    }
}
