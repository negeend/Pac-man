package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

public class Ghost extends MoveableObject {
    public PImage initialSprite;
    /**
     * Marks whether the ghost is in frightened mode or not
     */
    public boolean isFrightened;
    /**
     * Marks whether the ghost is alive or not
     */
    public boolean isAlive;
    /**
     * The duration for which the ghost is to remain frightened
     */
    public int frightenedLength;
    /**
     * A timer used to track how long a ghost has been in frightened mode
     */
    public int timer;
    /**
     * The mode of the ghost, that is; scatter or chase
     */
    public String mode;
    /**
     * The durations for which the ghost is to be in scatter or chase mode
     */
    private Object modeLengths;
    /**
     * The type of ghost
     */
    private String type;
    /**
     * The target location of the ghost
     */
    public int[] targetLocation;
    /**
     * The previous direction which the ghost was moving
     */
    public String previousMove;
    /**
     * The opposite direction to the direction which the ghost is currently moving in
     */
    public String notAllowed;
    
    public Ghost(int x, int y, PImage sprite, List<GameObject> walls, String type) {
        super(x, y, sprite, walls);
        this.initialSprite = initialSprite;
        this.isFrightened = false;
        this.isAlive = true;
        ConfigParser parser = new ConfigParser();
        Object[] configData = parser.parsedConfig();
        String frightenedLengthString = configData[4].toString();
        this.frightenedLength = Integer.parseInt(frightenedLengthString) * 60;
        this.modeLengths = configData[3];
        this.timer = 0;
        this.mode = "scatter";
        this.type = type;
        this.targetLocation = new int[2];
        this.currentDirection = "right";
        this.Yspeed = 1;
        this.previousMove = "";
        this.notAllowed = null;
    }

    /**
     * If the ghost has been set to frightened mode the begin the timer so that it remains in frightened mode for the length specified in the configuration file.
     * Gets the rise and run of the ghost to its target location and determines the most to least prioritised moves to reach the target.
     * Moves in the most prioritised move which does not result in a collision
     */
    public void tick() {
        if (this.isFrightened == true) {
            this.timer++;
        }
        if (this.timer == this.frightenedLength) {
            this.isFrightened = false;
            this.timer = 0;
        }
        double rise = this.getRiseRun()[0];
        double run = this.getRiseRun()[1];
        String[] potentialNext = this.direction(rise, run);
        this.moveGhost(potentialNext);
    }

    /**
     * Given a string array of directions, this method iterates the directions and if it does not result in a collision then it will move in that direction.
     * If the ghost is not at an intersection then it continues to move in the same direction.
     * If the move in the array is not allowed, i.e. it is the opposite direction to the direction which the ghost was just going, and the ghost is at an intersection then it will skip to the next move.
     * If the move is possible, update the previous move and the move which is now not allowed.
     */
    public void moveGhost(String[] orderedMoves) {
        for (String move : orderedMoves) {
            // System.out.println(move);
            // System.out.println(this.checkCollision("down"));
            
            if (this.isIntersection() == false) {
                move = this.previousMove;
            }

            if (move.equals(this.notAllowed) && this.isIntersection()) {
                continue;
            }
            // System.out.println(this.checkCollision(move));
            // System.out.println();
            if (! this.checkCollision(move)) {
                // System.out.println(move);

                if (move.equals("up")) {
                    this.y -= this.speed;
                    this.previousMove = move;
                    this.notAllowed = "down";

                } else if (move.equals("down")) {
                    this.y += this.speed;
                    this.previousMove = move;
                    this.notAllowed = "up";

                } else if (move.equals("right")) {
                    this.x += this.speed;
                    this.previousMove = move;
                    this.notAllowed = "left";

                } else {
                    this.x -= this.speed;
                    this.previousMove = move;
                    this.notAllowed = "right";
                }
                return;
            }
        }
    }

    /**
     * Determines whether the ghost is at an intersection based on its current direction.
     * @return True if it is at an intersection and false otherwise
     */
    public boolean isIntersection() {
        if (this.currentDirection.equals("right") || this.currentDirection.equals("left")) {
            if (this.hasCollided(this.x, this.y - 1).equals("up") || this.hasCollided(this.x, this.y + 1).equals("left")) {
                return false;
            }
        }
        if (this.currentDirection.equals("up") || this.currentDirection.equals("down")) {
            if (this.hasCollided(this.x - 1, this.y).equals("left") || this.hasCollided(this.x + 1, this.y).equals("right")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Given a direction, will check whether moving in that direction will result in a collsion or not.
     * If there will be a collision, returns true, otherwise returns false.
     * @param direction, the direction being attempted to move in
     * @return whether a movement in that direction will result in a collision
     */
    public boolean checkCollision(String direction) {
        if (direction.equals("right")) {
            for (int i = 1; i < 2; i++) {
                if (this.hasCollided(this.x + i, this.y).equals("right")) {
                    return true;
                }
            }
        }
        if (direction.equals("left")) {
            for (int i = 1; i < 2; i++) {
                if (this.hasCollided(this.x - i, this.y).equals("left")) {
                    return true;
                }
            }
        }
        if (direction.equals("down")) {
            for (int i = 1; i < 2; i++) {
                if (this.hasCollided(this.x, this.y + i).equals("down")) {
                    return true;
                }
            }
        }
        if (direction.equals("up")) {
            for (int i = 1; i < 2; i++) {
                if (this.hasCollided(this.x, this.y - i).equals("up")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Takes in the rise and run of the Ghost to its target location and determines the prioritised moves based on this.
     * @param rise, The difference of the heights of the Ghost and its target location
     * @param run, The difference of the horizontal positions of the Ghost and its target location.
     * @return An array of strings of the most to least prioritised moves to make.
     */
    public  String[] direction(double rise, double run) {
        if (run >= 0 && rise >= 0) {
            if (rise > run) { //up and left
                String[] directionArray = {"up", "left", "right", "down"};
                return directionArray;
            } else {
                String[] directionArray = {"left", "up", "right", "down"};
                return directionArray;
            }
        }
        else if (rise >= 0 && run < 0) { //up and right
            if (rise > Math.abs(run)) {
                String[] directionArray = {"up", "right", "left", "down"};
                return directionArray;
            } else {
                String[] directionArray = {"right", "up", "left", "down"};
                return directionArray;
            }
        }
        else if (run >= 0 && rise < 0) { //down and left
            if (Math.abs(rise) > run) {
                String[] directionArray = {"down", "left", "up", "right"};
                return directionArray;
            } else {
                String[] directionArray = {"left", "down", "up", "right"};
                return directionArray;
            }
        }
        else { // down and right
            if (Math.abs(rise) > Math.abs(run)) {
                String[] directionArray = {"down", "right", "left", "up"};
                return directionArray;
            } else {
                String[] directionArray = {"right", "up", "down", "left"};
                return directionArray;
            }
        }
    }

    /**
     * Given a horizontal and vertical position, sets the target location to that specified by the coordinates
     * @param x, The horizontal coordinate of the desired target location
     * @param y, The vertical coordinate of the desired target location
     */
    public void setTarget(int x, int y) {
        this.targetLocation[0] = x;
        this.targetLocation[1] = y;
    }

    public void setTargetScatter() {
        if (this.type.equals("c")) {
            this.targetLocation[0] = 0; this.targetLocation[1] = 48;
        }
        if (this.type.equals("a")) {
            this.targetLocation[0] = 448; this.targetLocation[1] = 48;
        }
        if (this.type.equals("i")) {
            this.targetLocation[0] = 0; this.targetLocation[1] = 544;
        }
        if (this.type.equals("w")) {
            this.targetLocation[0] = 448; this.targetLocation[1] = 544;
        }
    }

    /**
     * Calculates and returns the rise and run of the ghost to its target location
     * @return An array of doubles containing the rise and run of the ghost to its target location
     */
    public double[] getRiseRun() {
        double[] riseAndrun = new double[2];
        double rise = this.y - targetLocation[1];
        double run = this.x - targetLocation[0];
        riseAndrun[0] = rise;
        riseAndrun[1] = run;
        return riseAndrun;
    }

    /**
     * Sets the ghosts back to their original positions
     */
    public void reset() {
        ArrayList<String[]> startingPositions = Map.getStartingPositions();
        for (String[] obj : startingPositions) {
            if (obj[2].equals(this.type)) {
                this.x = Integer.parseInt(obj[0]) - 6;
                this.y = Integer.parseInt(obj[1]) - 6;
            }
        }
    }

    /**
     * If the Ghost has collided with a Wall object the the method will return which direction the collision was in, otherwise will return the string 'no collision'
     * @param x, the horizontal position of the Ghost on the map
     * @param y, the vertical position of the Ghost on the map
     * @return if collided: the direction of collision, else: no collision
     */
    public String hasCollided(int x, int y) {
        for (GameObject wall : this.walls) {
            if (wall instanceof Walls) {
                int wallLeft = wall.getX();
                int wallRight = wall.getX() + 16;
                int wallTop = wall.getY();
                int wallBottom = wall.getY() + 16;
                int objectLeft = x + 6;
                int objectRight = objectLeft + 16;
                int objectTop = y + 6;
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
}