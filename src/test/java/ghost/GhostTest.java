package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class GhostTest {
    @Test 
    public void simpleTest() {
        Ghost ghost = new Ghost(100, 100, null, null, "a");
        assertNotNull(ghost);
    }

    @Test
    public void hasCollidedTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(200, 200, null));
        Ghost ghost = new Ghost(100, 100, null, walls, "a");
        assertEquals(ghost.hasCollided(ghost.x, ghost.y), "no collision");
        walls.add(new Walls(105, 121, null));
        assertEquals(ghost.hasCollided(ghost.x, ghost.y), "down");
        ghost.x = 200; ghost.y = 200;
        walls.add(new Walls(221, 205, null));
        assertEquals(ghost.hasCollided(ghost.x, ghost.y), "right");
        ghost.x = 300; ghost.y = 300;
        walls.add(new Walls(291, 305, null));
        assertEquals(ghost.hasCollided(ghost.x, ghost.y), "left");   
        ghost.x = 400; ghost.y = 400;
        walls.add(new Walls(400, 391, null));
        assertEquals(ghost.hasCollided(ghost.x, ghost.y), "up");   
    }

    @Test
    public void setTargetScatterTest() {
        Ghost ambusher = new Ghost(100, 100, null, null, "a");
        ambusher.setTargetScatter();
        assertEquals(ambusher.targetLocation[0], 448);
        assertEquals(ambusher.targetLocation[1], 48);
        Ghost chaser = new Ghost(100, 100, null, null, "c");
        chaser.setTargetScatter();
        assertEquals(chaser.targetLocation[0], 0);
        assertEquals(chaser.targetLocation[1], 48);
        Ghost whim = new Ghost(100, 100, null, null, "w");
        whim.setTargetScatter();
        assertEquals(whim.targetLocation[0], 448);
        assertEquals(whim.targetLocation[1], 544);
        Ghost ignorant = new Ghost(100, 100, null, null, "i");
        ignorant.setTargetScatter();
        assertEquals(ignorant.targetLocation[0], 0);
        assertEquals(ignorant.targetLocation[1], 544);
    }

    @Test
    public void setTargetTest() {
        Ghost ghost = new Ghost(100, 100, null, null, "a");
        ghost.setTarget(200, 300);
        assertEquals(ghost.targetLocation[0], 200);
        assertEquals(ghost.targetLocation[1], 300);
    }

    @Test
    public void directionTest() {
        Ghost ghost = new Ghost(100, 100, null, null, "a");
        String[] result = ghost.direction(200, 100);
        String[] upLeft = {"up", "left", "right", "down"};
        assertArrayEquals(result, upLeft);
        result = ghost.direction(100, 200);
        String[] upLeft2 = {"left", "up", "right", "down"};
        assertArrayEquals(result, upLeft2);
        result = ghost.direction(-300, 200);
        String[] downLeft = {"down", "left", "up", "right"};
        assertArrayEquals(result, downLeft);
        result = ghost.direction(-100, 200);
        String[] downLeft2 = {"left", "down", "up", "right"};
        assertArrayEquals(result, downLeft2);
        result = ghost.direction(300, -200);
        String[] upRight = {"up", "right", "left", "down"};
        assertArrayEquals(result, upRight);
        result = ghost.direction(100, -200);
        String[] upRight2 = {"right", "up", "left", "down"};
        assertArrayEquals(result, upRight2);
        result = ghost.direction(-100, -200);
        String[] downRight = {"right", "up", "down", "left"};
        assertArrayEquals(result, downRight);
        result = ghost.direction(-300, -200);
        String[] downRight2 = {"down", "right", "left", "up"};
        assertArrayEquals(result, downRight2);
    }

    @Test
    public void checkCollision() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(221, 205, null));
        Ghost ghost = new Ghost(199, 200, null, walls, "a");
        assertTrue(ghost.checkCollision("right"));
        ghost.x = 300; ghost.y = 300;
        walls.add(new Walls(290, 305, null));
        assertTrue(ghost.checkCollision("left"));
        ghost.x = 400; ghost.y = 400;
        walls.add(new Walls(400, 390, null));
        assertTrue(ghost.checkCollision("up"));
        ghost.x = 100; ghost.y = 100;
        walls.add(new Walls(105, 122, null));
        assertTrue(ghost.checkCollision("down"));
        ghost.x = 10; ghost.y = 11;
        assertFalse(ghost.checkCollision("right"));
        assertFalse(ghost.checkCollision("up"));
        assertFalse(ghost.checkCollision("left"));
        assertFalse(ghost.checkCollision("down"));
    }

    @Test
    public void isIntersectionTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(400, 390, null));        
        Ghost ghost = new Ghost(400, 400, null, walls, "a");
        ghost.currentDirection = "right";
        assertFalse(ghost.isIntersection());
        ghost.currentDirection = "left";
        assertFalse(ghost.isIntersection());
        ghost.currentDirection = "up";
        assertTrue(ghost.isIntersection());
        ghost.currentDirection = "down";
        assertTrue(ghost.isIntersection());
        ghost.x = 300; ghost.y = 300;
        walls.add(new Walls(290, 305, null));
        assertFalse(ghost.isIntersection());
        walls.add(new Walls(221, 205, null));
        ghost.x = 199; ghost.y = 200;
        assertFalse(ghost.isIntersection());
        ghost.currentDirection = "up";
        assertFalse(ghost.isIntersection());
    }

    @Test
    public void moveTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(200, 200, null));        
        Ghost ghost = new Ghost(400, 400, null, walls, "a");
        ghost.previousMove = "up";
        String[] arr = {"up", "left", "right", "down"};
        ghost.moveGhost(arr);
        assertEquals(ghost.notAllowed, "down");
        assertEquals(ghost.previousMove, "up");
        assertEquals(ghost.y, 399);
        String[] arr2 = {"right", "up", "left", "down"};
        ghost.moveGhost(arr2);
        assertEquals(ghost.notAllowed, "left");
        assertEquals(ghost.previousMove, "right");
        assertEquals(ghost.x, 401);
        String[] arr3 = {"down", "left", "up", "right"};
        ghost.moveGhost(arr3);
        assertEquals(ghost.notAllowed, "up");
        assertEquals(ghost.previousMove, "down");
        assertEquals(ghost.y, 400);
    }

    @Test
    public void tickTest() {
        ArrayList<GameObject> walls = new ArrayList<GameObject>();
        walls.add(new Walls(200, 200, null));        
        Ghost ghost = new Ghost(400, 400, null, walls, "a");
        ghost.isFrightened = true;
        ghost.tick();
        assertEquals(ghost.timer, 1);
        Ghost ghost2 = new Ghost(200, 200, null, walls, "a");
        ghost2.timer = ghost2.frightenedLength;
        ghost2.tick();
        assertFalse(ghost2.isFrightened);
        assertEquals(ghost2.timer, 0);
    }
}