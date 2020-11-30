package test.darwinworld;

import main.darwinworld.map.WrappedMap;
import main.darwinworld.math.Vector2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WrappedMapTests {

    @Test
    public void fillWholeMapTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(10, 10), new Vector2D(3, 3), 0.01f);
        for(int i = 0; i < 100; i++)
            wm.makeStep();
        for(int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++){
                assertTrue(wm.isOccupied(new Vector2D(x, y)));
            }
        }
    }

    @Test
    public void coordinatesMappingTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(10, 10), new Vector2D(3, 3), 0.01f);
        assertEquals(wm.targetPositionMapping(new Vector2D(10, 10)), new Vector2D(0, 0));
        assertEquals(wm.targetPositionMapping(new Vector2D(101, 0)), new Vector2D(1, 0));
    }
}
