package ghost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public class ConfigParser {

    /**
     * Parses the configuration file
     * @return an array of objects with the data from the configuration file
     */
    public Object[] parsedConfig() { 
        JSONParser parser = new JSONParser();
        try {
            JSONObject arr = (JSONObject) parser.parse(new FileReader("config.json"));
            Object[] data = new Object[5];
            String mapName = (String) arr.get("map");
            data[0] = mapName;
            long numLives = (Long) arr.get("lives");
            data[1] = numLives;
            long speed = (Long) arr.get("speed");
            data[2] = speed;
            JSONArray modeLengths = (JSONArray) arr.get("modeLengths");
            data[3] = modeLengths;
            long frightenedLength = (Long) arr.get("frightenedLength");
            data[4] = frightenedLength;
            return data;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null; 
        } catch (ParseException e) {
            return null;
        }
    }
}