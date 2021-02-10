package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Game {
    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    public Waka waka;
    private Ghost ghost;
    private int counter;
    private int timer;
    private Waka closedWaka;
    private PImage playerClosed;
    public PImage playerLeft;
    private PImage playerRight;
    private PImage playerUp;
    private PImage playerDown;
    private PImage frightenedGhost;
    private PImage Chaser;
    private PImage Ambusher;
    private PImage Ignorant;
    private PImage Whim;
    ArrayList<GameObject> walls;
    ArrayList<Ghost> ghosts;
    ArrayList<Fruit> fruit;
    ArrayList<GameObject> lives;
    ArrayList<SuperFruit> superFruit;
    PFont font;
    App app;

    public Game(App app) {
        this.walls = new ArrayList<GameObject>();
        this.ghosts = new ArrayList<Ghost>();
        this.fruit = new ArrayList<Fruit>();
        this.superFruit = new ArrayList<SuperFruit>();
        this.lives = new ArrayList<GameObject>();
        this.counter = 8;
        this.timer = 600;
        this.app = app;
    }

    /**
     * Loads the map and all objects in the map
     */
    public void setup() {
        this.playerRight = app.loadImage("src/main/resources/playerRight.png");
        this.playerLeft = app.loadImage("src/main/resources/playerLeft.png");
        this.playerDown = app.loadImage("src/main/resources/playerDown.png");
        this.playerUp = app.loadImage("src/main/resources/playerUp.png");
        this.playerClosed = app.loadImage("src/main/resources/playerClosed.png");
        this.frightenedGhost = app.loadImage("src/main/resources/frightened.png");
        this.Chaser = app.loadImage("src/main/resources/chaser.png");
        this.Ambusher = app.loadImage("src/main/resources/ambusher.png");
        this.Ignorant = app.loadImage("src/main/resources/ignorant.png");
        this.Whim = app.loadImage("src/main/resources/whim.png");

        ArrayList<String[]> startingPositions = Map.getStartingPositions();
        for (String[] obj : startingPositions) {
            if (obj[2].equals("p")) {
                this.waka = new Waka(Integer.parseInt(obj[0]) - 4, Integer.parseInt(obj[1]) - 5, this.playerLeft, walls, fruit, ghosts, superFruit);
            } 
            if (obj[2].equals("a")) {
                Ghost ghost = new Ghost(Integer.parseInt(obj[0]) - 6, Integer.parseInt(obj[1]) - 6, this.Ambusher, walls, "a");
                ghost.initialSprite = this.Ambusher;
                ghost.setTargetScatter();
                ghosts.add(ghost);
            } 
            if (obj[2].equals("c")) {
                Ghost ghost = new Ghost(Integer.parseInt(obj[0]) - 6, Integer.parseInt(obj[1]) - 6, this.Chaser, walls, "c");
                ghost.initialSprite = this.Chaser;
                ghosts.add(ghost);
            } 
            if (obj[2].equals("i")) {
                Ghost ghost = new Ghost(Integer.parseInt(obj[0]) - 6, Integer.parseInt(obj[1]) - 6, this.Ignorant, walls, "i");
                ghost.initialSprite = this.Ignorant;
                ghost.setTargetScatter();
                ghosts.add(ghost);
            } 
            if (obj[2].equals("w")) {
                Ghost ghost = new Ghost(Integer.parseInt(obj[0]) - 6, Integer.parseInt(obj[1]) - 6, this.Whim, walls, "w");
                ghost.initialSprite = this.Whim;
                ghosts.add(ghost);
            } 
        }
        int numLives = this.waka.lives;
        int i = 0;
        while (numLives > 0) {
            GameObject life = new GameObject(5 + i, 545, app.loadImage("src/main/resources/playerRight.png"));
            i += 27; lives.add(life); numLives -= 1;
        }
        ArrayList<String[]> map = Map.getMap();
        for (String[] obj : map) {
            if (obj[2].equals("fruit")) {
                Fruit fruitObj = new Fruit(Integer.parseInt(obj[0]), Integer.parseInt(obj[1]), app.loadImage("src/main/resources/fruit.png"));
                this.fruit.add(fruitObj);
            } else if (obj[2].equals("super fruit")) {
                SuperFruit superFruitObj = new SuperFruit(Integer.parseInt(obj[0]), Integer.parseInt(obj[1]), app.loadImage("src/main/resources/superFruit.png"));
                this.superFruit.add(superFruitObj);
            } else if (obj[2].equals("empty cell")) {
                GameObject emptyCell = new GameObject(Integer.parseInt(obj[0]), Integer.parseInt(obj[1]), null);
                walls.add(emptyCell);
            } else {
                Walls wall = new Walls(Integer.parseInt(obj[0]), Integer.parseInt(obj[1]), app.loadImage(obj[2]));
                walls.add(wall); 
            }
        }
    }

    /**
     * Checks whether all the fruit and super fruit objects have been eaten. 
     * @return If all fruit and superfruit have been eaten then return true, else return false
     */
    public boolean gameWin() {
        int counter = 0;
        for (Fruit f : fruit) if (f.notEaten == true) counter++;
        for (SuperFruit f : superFruit) if (f.notEaten == true) counter++;
        if (counter == 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the waka has no more lives remaining.
     * @return If no lives are remaining then return true, else return false
     */
    public boolean gameOver() {
        if (this.waka.lives == 0) {
            return true;
        }
        return false;
    }

    /**
     * Resets the game to its initial state.
     * All fruit objects are set back to not eaten.
     * Waka and ghosts are back to their initial positions and the lives of the Waka are set back to their initial amount.
     */
    public void reset() {
        Fruit.reset(fruit);
        this.waka.reset();
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
        this.waka.lives = this.waka.initialLives;
        this.waka.sprite = playerLeft;
    }

    /**
     * Acts as the operating system for the game.
     * Checks whether there has  been a ghost collision, and resets the waka and ghost if true
     * Draws all the objects in the map which were loaded in the setup method.
     * Alternates the sprite of the waka between open and closed mouth every 8 frames.
     * Sets the sprite of the waka based on its current direction.
     */
    public void tick() {
        if (this.waka.ghostCollision == true) {
            this.waka.reset();
            for (Ghost ghost : ghosts) {
                ghost.reset();
            }
            this.waka.sprite = playerLeft;
            this.waka.ghostCollision = false;
        }
        else {
            this.waka.tick();
            app.rect(-1, -1, WIDTH + 2, HEIGHT + 2);
            app.background(0, 0, 0);
            for (Ghost ghost : ghosts) {
                ghost.tick();
                if (ghost.isAlive) {
                    if (ghost.isFrightened == true) ghost.sprite = frightenedGhost;
                    else ghost.sprite = ghost.initialSprite;
                    ghost.draw(app);
                }
            }
            for (GameObject wall : walls) {
                if (wall instanceof Walls) wall.draw(app);
            }
            for (Fruit fruit : fruit) {
                if (fruit.notEaten == true ) fruit.draw(app);
            }
            for (SuperFruit superFruit : superFruit) {
                if (superFruit.notEaten == true ) superFruit.draw(app);
            }
            for (int i = 0; i < this.waka.lives; i++) lives.get(i).draw(app);
            this.counter -= 1;
            if (this.counter < 0 && this.counter > -9) {
                this.closedWaka = new Waka(this.waka.x, this.waka.y, this.playerClosed, walls, fruit, ghosts, superFruit);
                this.closedWaka.draw(app);
            } 
            else if (this.counter == -9) this.counter = 8;
            else this.waka.draw(app);
            this.waka.draw(app);
            if (this.waka.currentDirection.equals("left")) this.waka.sprite = this.playerLeft;
            if (this.waka.currentDirection.equals("right")) this.waka.sprite = this.playerRight;
            if (this.waka.currentDirection.equals("up")) this.waka.sprite = this.playerUp;
            if (this.waka.currentDirection.equals("down")) this.waka.sprite = this.playerDown;
        }
    }

    /**
     * Updates the next move
     */
    public void move(String direction) {
        this.waka.nextMove = direction;
    } 
}