package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

enum mapObject {
    horizontalWall('1', "src/main/resources/horizontal.png"),
    verticalWall('2', "src/main/resources/vertical.png"),
    upLeft('3', "src/main/resources/upLeft.png"),
    upRight('4', "src/main/resources/upRight.png"),
    downLeft('5', "src/main/resources/downLeft.png"),
    downRight('6', "src/main/resources/downRight.png"),
    fruit('7', "fruit"),
    superFruit('8', "super fruit"),
    emptyCell('0', "empty cell");

    /**
     * The character representation of the mapObject on the map file
     */
    public char representation;
    /**
     * The file path to the sprite of the specific mapObject
     */
    public  String spritePath;

    mapObject(char representation, String spritePath) {
        this.representation = representation;
        this.spritePath = spritePath;
    }
}

public class Map {
    /**
     * Reads in the map given in the configuration file and parses it to find the starting positions of the Waka and Ghost objects.
     * @return An array list of String arays which contain the coordinates of the Waka or Ghost objects as strings, as well as a character to denote the type of object as said position
     */
    public static ArrayList<String[]> getStartingPositions() {
        ArrayList<String[]> positions = new ArrayList<String[]>();
        ArrayList<String> map = new ArrayList<String>();
        ConfigParser parser = new ConfigParser();
        Object[] configData = parser.parsedConfig();
        try {
            File mapFile = new File(configData[0].toString());
            Scanner scan = new Scanner(mapFile);
            while (scan.hasNextLine()) {
                String row = scan.nextLine();
                map.add(row);
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        }
        int row = 0;
        while (row < map.size()) {
            int col = 0;
            while (col < map.get(0).length()) { // All rows should be of same length as the first row
                for (mapObject obj : mapObject.values()) {

                }
                if (map.get(row).charAt(col) == 'p') {
                    String[] wakaStartingPos = {Integer.toString(col*16), Integer.toString(row*16), "p"};
                    positions.add(wakaStartingPos);
                }
                if (map.get(row).charAt(col) == 'a' || map.get(row).charAt(col) == 'c'|| map.get(row).charAt(col) == 'i' || map.get(row).charAt(col) == 'w') {
                    String[] ghostStartingPos = {Integer.toString(col*16), Integer.toString(row*16), Character.toString(map.get(row).charAt(col))};
                    positions.add(ghostStartingPos);
                }
                col++;
            }
            row++;
        }
        return positions;
    }

    /**
     * Reads in the map given in the configuration fileand parses it to find any walls, fruits, super fruits or empty cells.
     * @return An array list of String arrays which contain the coordinates of the object as strings as well as the filepath for the sprite is object is a wall, else, a string which specifies the type of object
     */
    public static ArrayList<String[]> getMap() {
        ArrayList<String> map = new ArrayList<String>();
        ConfigParser parser = new ConfigParser();
        Object[] configData = parser.parsedConfig();

        try {
            File mapFile = new File(configData[0].toString());
            Scanner scan = new Scanner(mapFile);
            while (scan.hasNextLine()) {
                String row = scan.nextLine();
                map.add(row);
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        }

        ArrayList<String[]> wallMap = new ArrayList<String[]>();
        int row = 0;
        while (row < map.size()) {
            int col = 0;
            while (col < map.get(0).length()) { // All rows should be of same length as the first row
                for (mapObject  obj : mapObject.values()) {
                    if (map.get(row).charAt(col) == obj.representation) {
                        String[] staticObj = {Integer.toString(col*16), Integer.toString(row*16), obj.spritePath};
                        wallMap.add(staticObj);
                    }
                }
                col++;
            }
            row++;
        }
        return wallMap;
    }
}