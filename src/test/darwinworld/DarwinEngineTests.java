package test.darwinworld;

import main.darwinworld.engine.DarwinEngine;
import main.darwinworld.map.WrappedMap;
import main.darwinworld.model.Vector2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DarwinEngineTests {
    @Test
    public void initialConditionsTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(10, 10), new Vector2D(3, 3), 0.1f);
        DarwinEngine engine = new DarwinEngine(2, 100, wm, 0.1f, 0.5f, null);

        assertEquals(engine.getAnimalCount(), 2);
        assertEquals(engine.getAverageChildren(), 0f, 0.01f);
        assertEquals(engine.getAverageLifespan(), 0f, 0.01f);
        assertEquals(engine.getEpoch(), 1);
    }

    @Test
    public void animalExtinctionTest(){
        WrappedMap wm = new WrappedMap(new Vector2D(10, 10), new Vector2D(3, 3), 0);
        DarwinEngine engine = new DarwinEngine(10, 6, wm, 0.1f, 0.5f, null);
        engine.run();
        assertEquals(engine.getAnimalCount(), 0);
        assertEquals(engine.getAverageLifespan(), 6f, 0.01f);
    }
}
