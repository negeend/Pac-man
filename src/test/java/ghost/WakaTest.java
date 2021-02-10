package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import processing.core.PApplet;


class WakaTest {
    @Test 
    public void simpleTest() {
        Waka waka = new Waka(100, 100, null, null, null, null, null);
        assertNotNull(waka);
    }

    @Test
    public void resetTest() { //Checks that the waka is correctly set back to its initial state with one less life 
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(100, 100, null));
        Waka waka = new Waka(100, 100, null, walls, null, null, null);
        waka.reset();
        assertEquals(waka.x, 203); //this assumes the use of the original map file provided 'map.txt'
        assertEquals(waka.y, 315); //so does this
        assertEquals(waka.Yspeed, 0);
        assertEquals(waka.nextMove, "left");
        assertEquals(waka.lives, 2);
    }

    @Test
    public void hasCollidedTest() {
        App app = new App();
        Game gameEngine = new Game(app);
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(100, 100, null));
        gameEngine.waka = new Waka(100, 100, null, walls, null, null, null);
        assertEquals(gameEngine.waka.hasCollided(gameEngine.waka.x, gameEngine.waka.y), "no collision");
        walls.add(new Walls(105, 120, null));
        assertEquals(gameEngine.waka.hasCollided(gameEngine.waka.x, gameEngine.waka.y), "down");
        gameEngine.waka.x = 200; gameEngine.waka.y = 200;
        walls.add(new Walls(219, 205, null));
        assertEquals(gameEngine.waka.hasCollided(gameEngine.waka.x, gameEngine.waka.y), "right");
        gameEngine.waka.x = 300; gameEngine.waka.y = 300;
        walls.add(new Walls(289, 305, null));
        assertEquals(gameEngine.waka.hasCollided(gameEngine.waka.x, gameEngine.waka.y), "left");   
        gameEngine.waka.x = 400; gameEngine.waka.y = 400;
        walls.add(new Walls(400, 390, null));
        assertEquals(gameEngine.waka.hasCollided(gameEngine.waka.x, gameEngine.waka.y), "up");   
    }

    @Test void helperTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(219, 205, null));
        Waka waka = new Waka(200, 200, null, walls, null, null, null);
        waka.helper();
        assertEquals(waka.Xspeed, 0);
        assertEquals(waka.x, 199);
        waka.x = 300; waka.y = 300;
        walls.add(new Walls(289, 305, null));
        waka.helper();
        assertEquals(waka.Xspeed, 0);
        assertEquals(waka.x, 301);
        waka.x = 400; waka.y = 400;
        walls.add(new Walls(400, 390, null));
        waka.helper();
        assertEquals(waka.Yspeed, 0);
        assertEquals(waka.y, 401);
        waka.x = 100; waka.y = 100;
        walls.add(new Walls(105, 120, null));
        waka.helper();
        assertEquals(waka.Yspeed, 0);
        assertEquals(waka.y, 99);
    }

    @Test
    public void checkCollisionTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(221, 205, null));
        Waka waka = new Waka(199, 200, null, walls, null, null, null);
        assertTrue(waka.checkCollision("right"));
        waka.x = 300; waka.y = 300;
        walls.add(new Walls(289, 305, null));
        assertTrue(waka.checkCollision("left"));
        waka.x = 400; waka.y = 400;
        walls.add(new Walls(400, 390, null));
        assertTrue(waka.checkCollision("up"));
        waka.x = 100; waka.y = 100;
        walls.add(new Walls(105, 122, null));
        assertTrue(waka.checkCollision("down"));
        waka.x = 10; waka.y = 11;
        assertFalse(waka.checkCollision("right"));
        assertFalse(waka.checkCollision("up"));
        assertFalse(waka.checkCollision("left"));
        assertFalse(waka.checkCollision("down"));
    }


    @Test
    public void eatFruitTest() {
        ArrayList<Fruit> fruit = new ArrayList<Fruit>();
        fruit.add(new Fruit(104, 105, null));
        Waka waka = new Waka(100, 100, null, null, fruit, null, null);
        waka.eatFruit();
        for (Fruit fruitObj : waka.fruit) {
            assertFalse(fruitObj.notEaten);
        }
        ArrayList<Fruit> fruit2 = new ArrayList<Fruit>();
        fruit2.add(new Fruit(104, 105, null));
        Waka waka2 = new Waka(200, 200, null, null, fruit2, null, null);
        waka2.eatFruit();
        for (Fruit fruitObj : waka2.fruit) {
            assertTrue(fruitObj.notEaten);
        }
    }

    @Test
    public void eatSuperFruitTest() {
        ArrayList<SuperFruit> superFruit = new ArrayList<SuperFruit>();
        superFruit.add(new SuperFruit(104, 105, null));
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(new Ghost(50, 50, null, null, "a"));
        Waka waka = new Waka(100, 100, null, null, null, ghosts, superFruit);
        waka.eatSuperFruit();
        for (SuperFruit supfruitObj : waka.superFruit) {
            assertFalse(supfruitObj.notEaten);
        }
        for (Ghost ghost : waka.ghosts) {
            assertTrue(ghost.isFrightened);
        }
        ArrayList<SuperFruit> superFruit2 = new ArrayList<SuperFruit>();
        superFruit2.add(new SuperFruit(104, 105, null));
        ArrayList<Ghost> ghosts2 = new ArrayList<Ghost>();
        ghosts2.add(new Ghost(50, 50, null, null, "a"));
        Waka waka2 = new Waka(200, 200, null, null, null, ghosts2, superFruit2);
        waka2.eatSuperFruit();
        for (SuperFruit supfruitObj : waka2.superFruit) {
            assertTrue(supfruitObj.notEaten);
        }
        for (Ghost ghost : waka2.ghosts) {
            assertFalse(ghost.isFrightened);
        }
        ArrayList<SuperFruit> superFruit3 = new ArrayList<SuperFruit>();
        superFruit3.add(new SuperFruit(104, 105, null));
        ArrayList<Ghost> ghosts3 = new ArrayList<Ghost>();
        ghosts3.add(new Ghost(50, 50, null, null, "a"));
        for (SuperFruit supFruit : superFruit3) {
            supFruit.notEaten = false;
        }
        Waka waka3 = new Waka(200, 200, null, null, null, ghosts3, superFruit3);
        waka2.eatSuperFruit();
        for (Ghost ghost : waka3.ghosts) {
            assertFalse(ghost.isFrightened);
        }
    }

    @Test
    public void ghostCollisionTest() {
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(new Ghost(200, 200, null, null, "a"));
        Waka waka = new Waka(100, 100, null, null, null, ghosts, null);
        assertFalse(waka.ghostCollision());
        assertFalse(waka.ghostCollision);
        Waka waka2 = new Waka(200, 200, null, null, null, ghosts, null);
        assertTrue(waka2.ghostCollision());
        assertTrue(waka2.ghostCollision);
        for (Ghost ghost : ghosts) {
            ghost.isFrightened = true;
        }
        assertFalse(waka2.ghostCollision());
        for (Ghost ghost : ghosts) {
            assertFalse(ghost.isAlive);
        }
    }

    @Test
    public void tickTest() {
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(new Ghost(100, 100, null, null, "a"));
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(105, 120, null));
        ArrayList<SuperFruit> superFruit = new ArrayList<SuperFruit>();
        superFruit.add(new SuperFruit(104, 105, null));
        ArrayList<Fruit> fruit = new ArrayList<Fruit>();
        fruit.add(new Fruit(104, 105, null));
        Waka waka = new Waka(100, 100, null, walls, fruit, ghosts, superFruit);
        assertEquals(waka.hasCollided(waka.x, waka.y), "down");
        waka.tick();
        assertEquals(waka.Yspeed, 0);
        for (Ghost ghost : ghosts) {
            assertTrue(ghost.isAlive);
        }
    }

    @Test
    public void moveTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(105, 120, null));
        Waka waka = new Waka(200, 200, null, walls, null, null, null);
        waka.move("right");
        assertEquals(waka.Xspeed, waka.speed);
        waka.move("left");
        assertEquals(waka.Xspeed, -1*waka.speed);
        waka.move("up");
        assertEquals(waka.Yspeed, -1*waka.speed);
        waka.move("down");
        assertEquals(waka.Yspeed, waka.speed);
    }
}