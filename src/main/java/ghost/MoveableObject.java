package ghost;
import java.util.List;
import processing.core.PImage;


public abstract class MoveableObject extends GameObject {
    /**
     * The horizontal speed of the moveable object
     */
    protected int Xspeed;
    /**
     * The vertical speed of the movable object
     */
    protected int Yspeed;
    /**
     * The speed given from the configuration file
     */
    protected int speed;
    /**
     * The next move which the moveable object will attempt to me
     */
    protected String nextMove;
    /**
     * The walls on the map which allow the object to detect collisions
     */
    protected List<GameObject> walls;
    /**
     * The current direction which the object is moving
     */
    protected String currentDirection;

    public MoveableObject(int x, int y, PImage sprite, List<GameObject> walls) {
        super(x, y, sprite);
        this.walls = walls;
        ConfigParser parser = new ConfigParser();
        Object[] configData = parser.parsedConfig();
        String speedString = configData[2].toString();
        this.speed = Integer.parseInt(speedString);
        this.Xspeed = -1*this.speed;
        this.Yspeed = 0;
        this.nextMove = "left";
        this.currentDirection = "left";
    }

    /**
     * Resets the the object to its original position
     */
    public abstract void reset();

    /**
     * Given a direction, will check whether moving in that direction will result in a collsion or not.
     * If there will be a collision, returns true, otherwise returns false.
     * @param direction, the direction being attempted to move in
     * @return whether a movement in that direction will result in a collision
     */
    public boolean checkCollision(String direction) {
        if (direction.equals("right")) {
            for (int i = 0; i < 5; i++) {
                if (this.hasCollided(this.x + i, this.y + this.Yspeed).equals("right")) {
                    return true;
                }
            }
        }
        if (direction.equals("left")) {
            for (int i = 0; i < 5; i++) {
                if (this.hasCollided(this.x - i, this.y + this.Yspeed).equals("left")) {
                    return true;
                }
            }
        }
        if (direction.equals("down")) {
            for (int i = 0; i < 5; i++) {
                if (this.hasCollided(this.x + this.Xspeed, this.y + i).equals("down")) {
                    return true;
                }
            }
        }
        if (direction.equals("up")) {
            for (int i = 0; i < 10; i++) {
                if (this.hasCollided(this.x + this.Xspeed, this.y - i).equals("up")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines the direction of the current collision and sets the object back one position in the opposite direction so that it is no longer colliding
     */
    public void helper() {
        if (this.hasCollided(this.x, this.y).equals("right")) {
            this.Xspeed = 0;
            this.x -= this.speed;
        }
        else if (this.hasCollided(this.x, this.y).equals("left")) {
            this.Xspeed = 0;
            this.x += this.speed;
        }
        else if (this.hasCollided(this.x, this.y).equals("up")) {
            this.Yspeed = 0;
            this.y += this.speed;
        }
        else if (this.hasCollided(this.x, this.y).equals("down")) {
            this.Yspeed = 0;
            this.y -= this.speed;
        }
    }
    
    /**
     * Takes in a direction and sets the horizonal or vertical velocity accordingly so that the object moves in the given direction
     * @param direction, the direction which the object needs to move
     */
    public void move(String direction) {
        this.helper();
        if (direction.equals("right")) this.Xspeed = this.speed;
        if (direction.equals("left")) this.Xspeed = -1*this.speed;
        if (direction.equals("up")) this.Yspeed = -1*this.speed;
        if (direction.equals("down")) this.Yspeed = this.speed;
    }

    /**
     * Checks whether the object is currently colliding with any walls and returns the direction of the collision, or "no collision" if there is not collision
     * @param x, the horizontal position of the object
     * @param y, the vertical position of the object
     * @return if colliding: the direction of the collision. Otherwise "no collision"
     */
    public abstract String hasCollided(int x, int y);

}
