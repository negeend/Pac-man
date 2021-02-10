package ghost;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class FruitTest {
    @Test 
    public void simpleTest() {
        Fruit fruitObject = new Fruit(100, 100, null);
        assertNotNull(fruitObject);
    }

    @Test
    public void eatenStatus() {
        Fruit fruitObject = new Fruit(100, 100, null);
        assertTrue(fruitObject.notEaten);
    }

    @Test
    public void hasBeenEaten() {
        Fruit fruitObject = new Fruit(100, 100, null);
        fruitObject.notEaten = false;
        assertEquals(fruitObject.notEaten, false);
    }

    @Test
    public void resetFruit() {
        ArrayList<Fruit> fruit = new ArrayList<Fruit>();
        fruit.add(new Fruit(100, 100, null));
        Fruit fruitObject = new Fruit(100, 100, null);
        fruitObject.notEaten = false;
        fruit.add(fruitObject);
        Fruit fruitObject1 = new Fruit(100, 100, null);
        fruitObject1.notEaten = false;
        fruit.add(fruitObject1);
        Fruit.reset(fruit);
        for (Fruit fruitObj : fruit) {
            assertTrue(fruitObj.notEaten);
        }
    }

    @Test
    public void superFruit() {
        SuperFruit superFruit = new SuperFruit(100, 100, null);
        assertNotNull(superFruit);
    }
}