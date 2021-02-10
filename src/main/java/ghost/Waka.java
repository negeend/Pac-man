package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

public class Waka extends MoveableObject {
    /**
     * The number of lives of the Waka - gets updates when Waka dies
     */
    public int lives;
    /**
     * The initial number of lives the Waka is to have everytime the game restarts
     */
    public int initialLives;
    /**
     * A list of all the fruits on the map so that the Waka can locate and eat them
     */
    public List<Fruit> fruit;
    /**
     * A list of all the ghosts on the map
     */
    public List<Ghost> ghosts;
    /**
     * A list of all the super fruits on the map
     */
    public List<SuperFruit> superFruit;
    /**
     * Marks whether the Waka has collided with a ghost
     */
    public boolean ghostCollision;

    public Waka(int x, int y, PImage sprite, List<GameObject> walls, List<Fruit> fruit, List<Ghost> ghosts, List<SuperFruit> superFruit) {
        super(x, y, sprite, walls);
        ConfigParser parser = new ConfigParser();
        Object[] configData = parser.parsedConfig();
        String livesString = configData[1].toString();
        this.initialLives = Integer.parseInt(livesString);
        this.lives = Integer.parseInt(livesString);
        this.fruit = fruit;
        this.ghosts = ghosts;
        this.superFruit = superFruit;
        this.ghostCollision = false;
    }    

    /**
     * Continuously checks whether the Waka is colliding with a ghost or wall and calls the method so that the waka is eating fruit objects. 
     * If collided with a ghost then all ghost are set back to alive. 
     * If no collision then continue moving in the same direction.
     * If a collision is detected then set the x and y speed to 0.
     * If the next move is possible then move in that direction and set the current direction as the next move. 
     */
    public void tick() {
        if (this.ghostCollision()) {
            for (Ghost ghost : this.ghosts) ghost.isAlive = true;
        }
        this.eatFruit();
        this.eatSuperFruit();
        if (this.hasCollided(this.x, this.y).equals("no collision")) {
            this.x += this.Xspeed;
            this.y += this.Yspeed;
        } else {
            this.Xspeed = 0;
            this.Yspeed = 0;
        }
        if (this.checkCollision(this.nextMove) == false) {
            this.currentDirection = this.nextMove;
            this.move(this.nextMove);
        }
    }

    /**
     * Sets the Waka back to the initial state it is to be in when the game first begins and deducts the lives by one
     */
    public void reset() {
        ArrayList<String[]> startingPositions = Map.getStartingPositions();
        for (String[] obj : startingPositions) {
            if (obj[2].equals("p")) {
                this.x = Integer.parseInt(obj[0]) - 5;
                this.y = Integer.parseInt(obj[1]) - 5;
                System.out.println(Integer.parseInt(obj[1]) - 5);
            }
        }
        this.Yspeed = 0;
        this.nextMove = "left";
        this.move("left");
        this.lives -= 1;
    }

    /**
     * If the Waka has collided with a Wall object the the method will return which direction the collision was in, otherwise will return the string 'no collision'
     * @param x, the horizontal position of the Waka on the map
     * @param y, the vertical position of the Waka on the map
     * @return if collided: the direction of collision, else: no collision
     */
    public String hasCollided(int x, int y) {
        for (GameObject wall : this.walls) {
            if (wall instanceof Walls) {
                int wallLeft = wall.getX();
                int wallRight = wall.getX() + 16;
                int wallTop = wall.getY();
                int wallBottom = wall.getY() + 16;
                int objectLeft = x + 4;
                int objectRight = objectLeft + 16;
                int objectTop = y + 5;
                int objectBottom = objectTop + 16;

                if (objectRight > wallLeft && objectLeft < wallRight && objectBottom > wallTop && objectTop < wallBottom) {
                    if (objectRight == wallLeft + this.speed) {
                        return "right";
                    }
                    else if (objectLeft == wallRight - this.speed) {
                        return "left";
                    }
                    else if (objectBottom == wallTop + this.speed) {
                        return "down";
                    }
                    else if (objectTop == wallBottom - this.speed) {
                        return "up";
                    }
                }
            }
        }
        return "no collision";
    }

    /**
     * Checks whether the Waka has gone past any Fruit objects, if so then it sets the Fruit object attribute 'notEaten' as false, otherwise it does nothing
     */
    public void eatFruit() {
        for (Fruit fruit : this.fruit) {
            int fruitLeft = fruit.getX();
            int fruitTop = fruit.getY();
            if (this.x + 12 == fruitLeft + 8 && this.y + 13 == fruitTop + 8) {
                fruit.notEaten = false;
            }
        }
    }

    /**
     * Checks whether the Waka has gone past any SuperFruit objects, if so then it sets the SuperFruit object attribute 'notEaten' as false, otherwise it does nothing
     */
    public void eatSuperFruit() {
        for (SuperFruit superFruitObj : this.superFruit) {
            int superfruitLeft = superFruitObj.getX();
            int superfruitTop = superFruitObj.getY();
  
            if (this.x + 12 == superfruitLeft + 8 && this.y + 13 == superfruitTop + 8 && superFruitObj.notEaten == true) {
                superFruitObj.notEaten = false;
                for (Ghost ghost : this.ghosts) ghost.isFrightened = true;
            }
        }
    }

    /**
     * Checks whether the Waka collides with any ghosts. If it has collided with a Ghost which is frightened, it sets the Ghost attribute 'isAlive' to false and returns false. 
     * If it collides with a Ghost which isn't frightened then it will return true. Otherwise returns false
     * @return true if the collision with a non-frightened ghost occurs, otherwise false.
     */
    public boolean ghostCollision() {
        for (Ghost ghost : this.ghosts) {

            int ghostLeft = ghost.getX();
            int ghostRight = ghost.getX() + 16;
            int ghostTop = ghost.getY();
            int ghostBottom = ghost.getY() + 16;
            int wakaLeft = x + 4;
            int wakaRight = wakaLeft + 16;
            int wakaTop = y + 5;
            int wakaBottom = wakaTop + 16;

            if (wakaRight > ghostLeft && wakaLeft < ghostRight && wakaBottom > ghostTop && wakaTop < ghostBottom && ghost.isFrightened == false && ghost.isAlive == true) {
                this.ghostCollision = true;
                return true;
            }
            if (wakaRight > ghostLeft && wakaLeft < ghostRight && wakaBottom > ghostTop && wakaTop < ghostBottom && ghost.isFrightened == true && ghost.isAlive == true) {
                ghost.isAlive = false;
            }
        }
        return false;             
    }
}