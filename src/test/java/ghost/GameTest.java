package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet; 
import java.util.ArrayList;

class GameTest {
    @Test 
    public void simpleTest() { //Checking that the Game object compiles correctly and is not null
        App app = new App();
        Game gameEngine = new Game(app);
        assertNotNull(gameEngine);
    }

    @Test
    public void gameNotWonTest() { //Checking that the game recognises that the game is not won if there are Fruit objects which aren't eaten
        App app = new App();
        Game gameEngine = new Game(app);
        gameEngine.fruit.add(new Fruit(100, 100, null));
        gameEngine.fruit.add(new Fruit(100, 100, null));
        gameEngine.fruit.add(new Fruit(100, 100, null));
        gameEngine.superFruit.add(new SuperFruit(100, 100, null));
        assertEquals(gameEngine.gameWin(), false);
    }

    @Test
    public void gameWinTest() { //Checking that game recognises that the game has been won when all the Fruit are eaten
        App app = new App();
        Game gameEngine = new Game(app);
        gameEngine.fruit.add(new Fruit(100, 100, null));
        gameEngine.fruit.add(new Fruit(100, 100, null));
        gameEngine.fruit.add(new Fruit(100, 100, null));
        gameEngine.superFruit.add(new SuperFruit(100, 100, null));
        for (Fruit fruitObj : gameEngine.fruit) {
            fruitObj.notEaten = false;
        }
        for (SuperFruit superFruitObj : gameEngine.superFruit) {
            superFruitObj.notEaten = false;
        }
        assertTrue(gameEngine.gameWin());
    } 

    @Test
    public void gameOverTest() { //Checking that the game recognises it is game over when the waka has no lives remianing
        App app = new App();
        Game gameEngine = new Game(app);
        gameEngine.waka = new Waka(100, 100, null, null, null, null, null);
        assertEquals(gameEngine.gameOver(), false);
        gameEngine.waka.lives = 0;
        assertTrue(gameEngine.gameOver());
    }

    @Test
    public void resetTest() { //Checks that the method correctly sets all Fruit to not eaten, sets Ghosts as alive, resets the Waka's lives and its sprite to the initial playerLeft sprite
        App app = new App();
        Game gameEngine = new Game(app);
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(100, 100, null));
        gameEngine.waka = new Waka(100, 100, null, walls, null, null, null);
        gameEngine.ghosts.add(new Ghost(100, 100, null, null, "a"));
        gameEngine.reset();
        for (Fruit fruitObj : gameEngine.fruit) {
            assertTrue(fruitObj.notEaten);
        }
        for (Ghost ghost : gameEngine.ghosts) {
            assertTrue(ghost.isAlive);
        }
        assertEquals(gameEngine.waka.lives, gameEngine.waka.initialLives);
        assertEquals(gameEngine.waka.sprite, gameEngine.playerLeft);
    }

    @Test
    public void moveTest() { //Checks that the method correctly sets the next move of the Waka
        App app = new App();
        Game gameEngine = new Game(app);
        gameEngine.waka = new Waka(100, 100, null, null, null, null, null);
        gameEngine.move("right");
        assertEquals(gameEngine.waka.nextMove, "right");
        gameEngine.move("left");
        assertEquals(gameEngine.waka.nextMove, "left");
        gameEngine.move("up");
        assertEquals(gameEngine.waka.nextMove, "up");
        gameEngine.move("down");
        assertEquals(gameEngine.waka.nextMove, "down");
    }

    @Test
    public void setupTest() {
        App app = new App();
        Game gameEngine = new Game(app);
        PApplet.runSketch(new String[]{"Test"}, app);
        app.setup();
    }

}