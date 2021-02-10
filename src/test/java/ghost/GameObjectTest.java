package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;


class GameObjectTest {
    @Test 
    public void simpleTest() {
        GameObject object = new GameObject(100, 100, null);
        assertNotNull(object);
    }

    @Test
    public void getterMethodsTest() {
        GameObject object = new GameObject(100, 100, null);
        assertEquals(object.getX(), 100);
        assertEquals(object.getY(), 100);
        assertEquals(object.getWidth(), -1);
        assertEquals(object.getHeight(), -1);
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        assertEquals(app.game.waka.getHeight(), 26);
        assertEquals(app.game.waka.getWidth(), 24);
    }
    
}