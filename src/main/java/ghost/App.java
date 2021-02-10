package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class App extends PApplet {
    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    private int timer;
    PFont font;
    Game game;

    public App() {
        this.game = new Game(this); this.timer = 600;
    }

    public void setup() {
        frameRate(60);
        this.game.setup();
    }
    public void settings() {
        size(WIDTH, HEIGHT);
    }
    public void draw() { 
        background(0, 0, 0);
        if (this.game.gameWin()) {
            font = createFont("src/main/resources/PressStart2P-Regular.ttf", 32); textFont(font); textAlign(CENTER, CENTER); text("YOU WIN", 224, 288);
            this.timer -= 1;
        }
        else if (this.game.gameOver()) {
            font = createFont("src/main/resources/PressStart2P-Regular.ttf", 32); textFont(font); textAlign(CENTER, CENTER); text("GAME OVER", 224, 288);
            this.timer -= 1;
        } else {
            background(0, 0, 0); this.game.tick();
        }
        if (this.timer < 0) {
            this.game.reset(); this.timer = 600; 
        }
    }
    public void keyPressed() {
        if (keyCode == RIGHT) this.game.move("right");
        if (keyCode == LEFT) this.game.move("left");
        if (keyCode == UP) this.game.move("up");
        if (keyCode == DOWN) this.game.move("down");
    }
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}