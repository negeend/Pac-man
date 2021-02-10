package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;

public class Fruit extends GameObject {
    /**
     * Marks whether the Fruit object is eaten or not
     */
    protected boolean notEaten;

    public Fruit(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.notEaten = true;
    }
    /**
     * Given an array list of Fruit objects, it sets the Fruit objects back to not eaten after the game is reset
     * @param fruit, Array list of Fruit objects
     */
    public static void reset(ArrayList<Fruit> fruit) {
        for (Fruit fruitObj : fruit) {
            fruitObj.notEaten = true;
        }
    }
}