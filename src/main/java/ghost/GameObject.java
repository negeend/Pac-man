package ghost;
import processing.core.PImage;
import processing.core.PApplet;

public class GameObject {
    /**
     * The horizontal position of the object
     */
    protected int x;
    /**
     * The vertical position of the object
     */
    protected int y;
    /**
     * The sprite of the object
     */
    protected PImage sprite;

    public GameObject(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
    /**
     * Will draw the GameObject sprite to the screen
     * @param app, A PApplet object
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }
    /**
     * Gets the horizontal position of the GameObject on the map
     * @return horizontal position
     */
    public int getX() {
        return this.x;
    }
    /**
     * Gets the vertical position of the GameObject on the map
     * @return vertical position
     */
    public int getY() {
        return this.y;
    }
    /**
     * Gets the width of the GameObject sprite
     * @return sprite width
     */
    public int getWidth() {
        if (this.sprite != null) {
            return this.sprite.width;
        }
        return -1;
    }
    /**
     * Gets the height of the GameObject sprite
     * @return sprite height
     */
    public int getHeight() {
        if (this.sprite != null) {
            return this.sprite.height;
        }
        return -1;
    }

}

