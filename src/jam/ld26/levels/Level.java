package jam.ld26.levels;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level {
    private ArrayList<ArrayList<Integer>> map;
    
    public Level(String fileName) throws FileNotFoundException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(new Scanner(new File(fileName)).useDelimiter("\\Z").next());
        
        // Parse map element of level
        map = new ArrayList<ArrayList<Integer>>();
        JSONArray jsonArray = (JSONArray) obj.get("map"); 
        if (jsonArray != null) { 
            for (int i = 0; i < jsonArray.size(); i++){ 
                map.add(new ArrayList<Integer>());
                for (int j = 0; j < ((JSONArray) jsonArray.get(i)).size(); j++){ 
                    map.get(i).add(Integer.parseInt(((JSONArray) jsonArray.get(i)).get(j).toString()));
                } 
            }
        }
    }
    
    public ArrayList<ArrayList<Integer>> getMap() {
        return map;
    }

    public int getRows() {
        return map.size();
    }

    public int getCols() {
        // All rows have the same columns
        return map.get(0).size();
    }
    
}
