package test.darwinworld;

import main.darwinworld.model.Vector2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathTests {
    @Test
    public void equalsTest(){
        Vector2D vec = new Vector2D(-1, 2);
        assertEquals(vec.equals(new String("TEST")), false);
        assertEquals(vec.equals(vec), true);
        assertEquals(vec.equals(new Vector2D(3, -1)), false);
        assertEquals(vec.equals(new Vector2D(-1, 2)), true);
    }

    @Test
    public void toStringTest(){
        Vector2D vec = new Vector2D(-2, 3);
        assertEquals(vec.toString(), "(-2,3)");
        vec = new Vector2D(1,1);
        assertEquals(vec.toString(), "(1,1)");
        vec = new Vector2D(-4, -4);
        assertEquals(vec.toString(), ("(-4,-4)"));
    }

    @Test
    public void precedesTest(){
        Vector2D vec1 = new Vector2D(-2, 3);
        Vector2D vec2 = new Vector2D(-2, 5);
        assertEquals(vec1.precedes(vec2), true);
        vec1 = new Vector2D(-2, 5);
        assertEquals(vec1.precedes(vec2), true);
        vec1 = new Vector2D(3, 5);
        assertEquals(vec1.precedes(vec2), false);
    }

    @Test
    public void followsTest(){
        Vector2D vec1 = new Vector2D(-2, 3);
        Vector2D vec2 = new Vector2D(-2, 5);
        assertEquals(vec2.follows(vec1), true);
        vec1 = new Vector2D(-2, 5);
        assertEquals(vec2.follows(vec1), true);
        vec1 = new Vector2D(3, 5);
        assertEquals(vec2.follows(vec1), false);
    }

    @Test
    public void upperRightTest(){
        Vector2D vec1 = new Vector2D(-2, 3);
        Vector2D vec2 = new Vector2D(-1, 2);
        assertEquals(vec1.upperRight(vec2), new Vector2D(-1, 3));
        vec2 = new Vector2D(-3, 2);
        assertEquals(vec1.upperRight(vec2), new Vector2D(-2, 3));
    }

    @Test
    public void lowerLeftTest(){
        Vector2D vec1 = new Vector2D(-2, 3);
        Vector2D vec2 = new Vector2D(-1, 2);
        assertEquals(vec1.lowerLeft(vec2), new Vector2D(-2, 2));
        vec2 = new Vector2D(-3, 2);
        assertEquals(vec1.lowerLeft(vec2), new Vector2D(-3, 2));
    }

    @Test
    public void addTest(){
        int x1 = 2;
        int x2 = 3;
        int y1 = 4;
        int y2 = 8;
        Vector2D vec1 = new Vector2D(x1, y1);
        Vector2D vec2 = new Vector2D(x2, y2);
        assertEquals(vec1.add(vec2), new Vector2D(x1+x2, y1+y2));
    }

    @Test
    public void substractTest(){
        int x1 = 2;
        int x2 = 3;
        int y1 = 4;
        int y2 = 8;
        Vector2D vec1 = new Vector2D(x1, y1);
        Vector2D vec2 = new Vector2D(x2, y2);
        assertEquals(vec1.substract(vec2), new Vector2D(x1-x2, y1-y2));
    }

    @Test
    public void oppositeTest(){
        Vector2D vec1 = new Vector2D(-1, 1);
        assertEquals(vec1.opposite(), new Vector2D(1, -1));
    }
}
